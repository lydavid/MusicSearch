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
import ly.david.data.domain.ListItemModel
import ly.david.data.domain.ListSeparator
import ly.david.data.domain.ReleaseScaffoldModel
import ly.david.data.domain.TrackListItemModel
import ly.david.data.domain.toTrackListItemModel
import ly.david.data.getDisplayNames
import ly.david.data.getNameWithDisambiguation
import ly.david.data.network.MusicBrainzResource
import ly.david.data.network.api.coverart.CoverArtArchiveApiService
import ly.david.data.network.api.coverart.getSmallCoverArtUrl
import ly.david.data.paging.LookupResourceRemoteMediator
import ly.david.data.paging.MusicBrainzPagingConfig
import ly.david.data.persistence.history.LookupHistoryDao
import ly.david.data.persistence.release.MediumDao
import ly.david.data.persistence.release.MediumRoomModel
import ly.david.data.persistence.release.ReleaseDao
import ly.david.data.persistence.release.TrackDao
import ly.david.data.persistence.release.TrackRoomModel
import ly.david.data.repository.ReleaseRepository
import ly.david.mbjc.ui.common.history.RecordLookupHistory
import ly.david.mbjc.ui.common.paging.IRelationsList
import ly.david.mbjc.ui.common.paging.RelationsList

@HiltViewModel
internal class ReleaseViewModel @Inject constructor(
    private val releaseDao: ReleaseDao,
    private val mediumDao: MediumDao,
    private val trackDao: TrackDao,
    override val lookupHistoryDao: LookupHistoryDao,
    private val coverArtArchiveApiService: CoverArtArchiveApiService,
    private val repository: ReleaseRepository,
    private val relationsList: RelationsList,
) : ViewModel(), RecordLookupHistory,
    IRelationsList by relationsList {

    private data class ViewModelState(
        val releaseId: String = "",
        val query: String = ""
    )

    private val releaseId: MutableStateFlow<String> = MutableStateFlow("")
    private val query: MutableStateFlow<String> = MutableStateFlow("")
    private val tracksParamState = combine(releaseId, query) { releaseId, query ->
        ViewModelState(releaseId, query)
    }.distinctUntilChanged()

    val title = MutableStateFlow("")
    val subtitleState = MutableStateFlow("")
    var recordedLookup = false
    val isError = MutableStateFlow(false)
    val release: MutableStateFlow<ReleaseScaffoldModel?> = MutableStateFlow(null)
    val url = MutableStateFlow("")

    init {
        relationsList.scope = viewModelScope
        relationsList.repository = repository
    }

    /**
     * Call this to retrieve the title, subtitle, and initiate tracks paging.
     */
    private suspend fun lookupRelease(releaseId: String): ReleaseScaffoldModel {
        return repository.lookupRelease(releaseId)
    }

    private fun loadTracks(releaseId: String) {
        this.releaseId.value = releaseId
    }

    fun updateQuery(query: String) {
        this.query.value = query
    }

    @OptIn(ExperimentalCoroutinesApi::class, ExperimentalPagingApi::class)
    val pagedTracks: Flow<PagingData<ListItemModel>> =
        tracksParamState.filterNot { it.releaseId.isEmpty() }
            .flatMapLatest { (releaseId, query) ->
                Pager(
                    config = MusicBrainzPagingConfig.pagingConfig,
                    remoteMediator = LookupResourceRemoteMediator(
                        hasResourceBeenStored = { hasReleaseTracksBeenStored(releaseId) },
                        lookupResource = { repository.lookupRelease(releaseId) },
                        deleteLocalResource = { releaseDao.deleteReleaseById(releaseId) }
                    ),
                    pagingSourceFactory = {
                        getPagingSource(releaseId, query)
                    }
                ).flow.map { pagingData ->
                    pagingData.map { track: TrackRoomModel ->
                        track.toTrackListItemModel()
                    }.insertSeparators { before: TrackListItemModel?, after: TrackListItemModel? ->
                        if (before?.mediumId != after?.mediumId && after != null) {
                            val medium: MediumRoomModel =
                                mediumDao.getMediumForTrack(after.id) ?: return@insertSeparators null

                            ListSeparator(
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

    private fun getPagingSource(releaseId: String, query: String): PagingSource<Int, TrackRoomModel> = when {
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

    /**
     * Returns a url to the cover art. Empty if none found.
     *
     * Also set it in the release.
     */
    private suspend fun getCoverArtUrlFromNetwork(releaseId: String): String {
        val url = coverArtArchiveApiService.getReleaseCoverArts(releaseId).getSmallCoverArtUrl().orEmpty()
        releaseDao.setReleaseCoverArtUrl(releaseId, url)
        return url
    }

    fun setTitle(title: String?) {
        this.title.value = title ?: return
    }

    fun onSelectedTabChange(
        releaseId: String,
        selectedTab: ReleaseTab
    ) {
        when (selectedTab) {
            ReleaseTab.DETAILS -> {
                viewModelScope.launch {
                    try {
                        val releaseScaffoldModel = lookupRelease(releaseId)
                        val coverArtUrl = releaseScaffoldModel.coverArtUrl
                        if (coverArtUrl != null) {
                            url.value = coverArtUrl
                        } else if (releaseScaffoldModel.coverArtArchive.count > 0) {
                            url.value = getCoverArtUrlFromNetwork(releaseId)
                        }

                        if (title.value.isEmpty()) {
                            title.value = releaseScaffoldModel.getNameWithDisambiguation()
                        }
                        subtitleState.value = "Release by ${releaseScaffoldModel.artistCredits.getDisplayNames()}"
                        release.value = releaseScaffoldModel
                        isError.value = false
                    } catch (ex: Exception) {
                        isError.value = true
                    }

                    // This will still record a lookup that failed, allowing for us to quickly get back to it later.
                    // However, reloading it will not record another visit, so its title will remain empty in history.
                    // But clicking on it will update its title, so we're not fixing it right now.
                    if (!recordedLookup) {
                        recordLookupHistory(
                            resourceId = releaseId,
                            resource = MusicBrainzResource.RELEASE,
                            summary = title.value
                        )
                        recordedLookup = true
                    }
                }
            }
            ReleaseTab.TRACKS -> loadTracks(releaseId)
            ReleaseTab.RELATIONSHIPS -> loadRelations(releaseId)
            ReleaseTab.STATS -> {

            }
        }
    }
}
