package ly.david.mbjc.ui.release

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.cachedIn
import androidx.paging.insertSeparators
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.IOException
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ly.david.data.common.transformThisIfNotNullOrEmpty
import ly.david.data.coverart.ReleaseImageManager
import ly.david.data.coverart.api.CoverArtArchiveApiService
import ly.david.data.domain.listitem.ListItemModel
import ly.david.data.domain.listitem.ListSeparator
import ly.david.data.domain.listitem.TrackListItemModel
import ly.david.data.domain.listitem.toTrackListItemModel
import ly.david.data.domain.paging.LookupEntityRemoteMediator
import ly.david.data.domain.paging.MusicBrainzPagingConfig
import ly.david.data.domain.release.ReleaseRepository
import ly.david.data.domain.release.ReleaseScaffoldModel
import ly.david.data.getDisplayNames
import ly.david.data.getNameWithDisambiguation
import ly.david.data.image.ImageUrlSaver
import ly.david.data.network.MusicBrainzEntity
import ly.david.data.room.history.LookupHistoryDao
import ly.david.data.room.history.RecordLookupHistory
import ly.david.data.room.release.ReleaseDao
import ly.david.data.room.release.tracks.MediumDao
import ly.david.data.room.release.tracks.MediumRoomModel
import ly.david.data.room.release.tracks.TrackDao
import ly.david.data.room.release.tracks.TrackForListItem
import ly.david.ui.common.MusicBrainzEntityViewModel
import ly.david.ui.common.paging.IRelationsList
import ly.david.ui.common.paging.RelationsList
import retrofit2.HttpException
import timber.log.Timber

@HiltViewModel
internal class ReleaseScaffoldViewModel @Inject constructor(
    private val releaseDao: ReleaseDao,
    private val mediumDao: MediumDao,
    private val trackDao: TrackDao,
    override val lookupHistoryDao: LookupHistoryDao,
    override val imageUrlSaver: ImageUrlSaver,
    override val coverArtArchiveApiService: CoverArtArchiveApiService,
    private val repository: ReleaseRepository,
    private val relationsList: RelationsList,
) : ViewModel(), MusicBrainzEntityViewModel, RecordLookupHistory,
    IRelationsList by relationsList,
    ReleaseImageManager {

    private data class ViewModelState(
        val releaseId: String = "",
        val query: String = ""
    )

    private val releaseId: MutableStateFlow<String> = MutableStateFlow("")
    override val query: MutableStateFlow<String> = MutableStateFlow("")
    private val tracksParamState = combine(releaseId, query) { releaseId, query ->
        ViewModelState(releaseId, query)
    }.distinctUntilChanged()

    private var recordedLookup = false
    override val entity: MusicBrainzEntity = MusicBrainzEntity.RELEASE
    override val title = MutableStateFlow("")
    override val isError = MutableStateFlow(false)

    val release: MutableStateFlow<ReleaseScaffoldModel?> = MutableStateFlow(null)
    val subtitle = MutableStateFlow("")
    val url = MutableStateFlow("")

    init {
        relationsList.scope = viewModelScope
        relationsList.repository = repository
    }

    private fun loadTracks(releaseId: String) {
        this.releaseId.value = releaseId
    }

    @OptIn(ExperimentalCoroutinesApi::class, ExperimentalPagingApi::class)
    val pagedTracks: Flow<PagingData<ListItemModel>> =
        tracksParamState.filterNot { it.releaseId.isEmpty() }
            .flatMapLatest { (releaseId, query) ->
                Pager(
                    config = MusicBrainzPagingConfig.pagingConfig,
                    remoteMediator = LookupEntityRemoteMediator(
                        hasEntityBeenStored = { hasReleaseTracksBeenStored(releaseId) },
                        lookupEntity = { repository.lookupRelease(releaseId) },
                        deleteLocalEntity = { releaseDao.deleteReleaseById(releaseId) }
                    ),
                    pagingSourceFactory = {
                        getPagingSource(releaseId, query)
                    }
                ).flow.map { pagingData ->
                    pagingData.map { track: TrackForListItem ->
                        track.toTrackListItemModel()
                    }.insertSeparators { before: TrackListItemModel?, after: TrackListItemModel? ->
                        if (before?.mediumId != after?.mediumId && after != null) {
                            val medium: MediumRoomModel =
                                mediumDao.getMediumForTrack(after.id) ?: return@insertSeparators null

                            ListSeparator(
                                id = "${medium.id}",
                                text = medium.format.orEmpty() +
                                    (medium.position?.toString() ?: "").transformThisIfNotNullOrEmpty { " $it" } +
                                    medium.title.transformThisIfNotNullOrEmpty { " ($it)" }
                            )
                        } else {
                            null
                        }
                    }
                }
            }
            .distinctUntilChanged()
            .cachedIn(viewModelScope)

    private suspend fun hasReleaseTracksBeenStored(releaseId: String): Boolean {
        val roomRelease = releaseDao.getReleaseWithFormatTrackCounts(releaseId)
        return !roomRelease?.formatTrackCounts.isNullOrEmpty()
    }

    private fun getPagingSource(releaseId: String, query: String): PagingSource<Int, TrackForListItem> = when {
        query.isEmpty() -> {
            trackDao.getTracksInRelease(releaseId)
        }
        else -> {
            trackDao.getTracksInReleaseFiltered(
                releaseId = releaseId,
                query = "%$query%"
            )
        }
    }

    fun loadDataForTab(
        releaseId: String,
        selectedTab: ReleaseTab
    ) {
        when (selectedTab) {
            ReleaseTab.DETAILS -> {
                viewModelScope.launch {
                    try {
                        val releaseScaffoldModel = repository.lookupRelease(releaseId)
                        if (title.value.isEmpty()) {
                            title.value = releaseScaffoldModel.getNameWithDisambiguation()
                        }
                        subtitle.value = "Release by ${releaseScaffoldModel.artistCredits.getDisplayNames()}"
                        release.value = releaseScaffoldModel

                        fetchCoverArt(releaseId, releaseScaffoldModel)

                        isError.value = false
                    } catch (ex: HttpException) {
                        Timber.e(ex)
                        isError.value = true
                    } catch (ex: IOException) {
                        Timber.e(ex)
                        isError.value = true
                    }

                    // This will still record a lookup that failed, allowing for us to quickly get back to it later.
                    // However, reloading it will not record another visit, so its title will remain empty in history.
                    // But clicking on it will update its title, so we're not fixing it right now.
                    if (!recordedLookup) {
                        recordLookupHistory(
                            entityId = releaseId,
                            entity = entity,
                            summary = title.value
                        )
                        recordedLookup = true
                    }
                }
            }
            ReleaseTab.TRACKS -> loadTracks(releaseId)
            ReleaseTab.RELATIONSHIPS -> loadRelations(releaseId)
            ReleaseTab.STATS -> {
                // Not handled here.
            }
        }
    }

    private suspend fun fetchCoverArt(
        releaseId: String,
        releaseScaffoldModel: ReleaseScaffoldModel
    ) {
        val imageUrl = releaseScaffoldModel.imageUrl
        url.value = imageUrl ?: getReleaseCoverArtUrlFromNetwork(
            releaseId = releaseId,
            thumbnail = false
        )
    }
}
