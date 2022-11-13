package ly.david.mbjc.ui.release

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.cachedIn
import androidx.paging.insertHeaderItem
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
import ly.david.data.common.transformThisIfNotNullOrEmpty
import ly.david.data.domain.Header
import ly.david.data.domain.ListSeparator
import ly.david.data.domain.ReleaseUiModel
import ly.david.data.domain.TrackUiModel
import ly.david.data.domain.UiModel
import ly.david.data.domain.toTrackUiModel
import ly.david.data.network.RelationMusicBrainzModel
import ly.david.data.network.api.LookupApi
import ly.david.data.network.api.MusicBrainzApiService
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
import ly.david.mbjc.ui.relation.IRelationsList
import ly.david.mbjc.ui.relation.RelationsList

@HiltViewModel
internal class ReleaseViewModel @Inject constructor(
    private val releaseDao: ReleaseDao,
    private val mediumDao: MediumDao,
    private val trackDao: TrackDao,
    override val lookupHistoryDao: LookupHistoryDao,
    private val coverArtArchiveApiService: CoverArtArchiveApiService,
    private val releaseRepository: ReleaseRepository,
    private val relationsList: RelationsList,
    private val musicBrainzApiService: MusicBrainzApiService,
) : ViewModel(), RecordLookupHistory,
    IRelationsList by relationsList, RelationsList.Delegate {

    private data class ViewModelState(
        val releaseId: String = "",
        val query: String = ""
    )

    private val releaseId: MutableStateFlow<String> = MutableStateFlow("")
    private val query: MutableStateFlow<String> = MutableStateFlow("")
    private val tracksParamState = combine(releaseId, query) { releaseId, query ->
        ViewModelState(releaseId, query)
    }.distinctUntilChanged()

    init {
        relationsList.scope = viewModelScope
        relationsList.delegate = this
    }

    override suspend fun lookupRelationsFromNetwork(resourceId: String): List<RelationMusicBrainzModel>? {
        return musicBrainzApiService.lookupRelease(
            releaseId = resourceId,
            include = LookupApi.INC_ALL_RELATIONS
        ).relations
    }

    /**
     * Call this to retrieve the title, subtitle, and initiate tracks paging.
     */
    suspend fun lookupReleaseThenLoadTracks(releaseId: String): ReleaseUiModel {
        return releaseRepository.getRelease(releaseId).also { loadTracks(releaseId) }
    }

    fun loadTracks(releaseId: String) {
        this.releaseId.value = releaseId
    }

    fun updateQuery(query: String) {
        this.query.value = query
    }

    // TODO: tracks refresh broken
    @OptIn(ExperimentalCoroutinesApi::class, ExperimentalPagingApi::class)
    val pagedTracks: Flow<PagingData<UiModel>> =
        tracksParamState.filterNot { it.releaseId.isEmpty() }
            .flatMapLatest { (releaseId, query) ->
                Pager(
                    config = MusicBrainzPagingConfig.pagingConfig,
                    remoteMediator = LookupResourceRemoteMediator(
                        hasResourceBeenStored = { hasReleaseTracksBeenStored(releaseId) },
                        lookupResource = { releaseRepository.getRelease(releaseId) },
                        deleteLocalResource = {
                            // TODO: invalidate cover art cache and refresh
                            // TODO: delete release_label
                            releaseDao.deleteReleaseById(releaseId)
                        }
                    ),
                    pagingSourceFactory = {
                        getPagingSource(releaseId, query)
                    }
                ).flow.map { pagingData ->
                    pagingData.map { track: TrackRoomModel ->
                        track.toTrackUiModel()
                    }.insertSeparators { before: TrackUiModel?, after: TrackUiModel? ->
                        if (before?.mediumId != after?.mediumId && after != null) {
                            // TODO: possible race condition: sometimes crashes here with null medium
                            val medium: MediumRoomModel = mediumDao.getMediumForTrack(after.id)
                            ListSeparator(
                                text = medium.format.orEmpty() +
                                    (medium.position?.toString() ?: "").transformThisIfNotNullOrEmpty { " $it" } +
                                    medium.title.transformThisIfNotNullOrEmpty { " ($it)" }
                            )
                        } else {
                            null
                        }
                    }.insertHeaderItem(item = Header)
                }
            }
            .distinctUntilChanged()
            .cachedIn(viewModelScope)

    private suspend fun hasReleaseTracksBeenStored(releaseId: String): Boolean {
        val roomRelease = releaseDao.getRelease(releaseId)
        return roomRelease?.formats != null && roomRelease.tracks != null
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
    suspend fun getCoverArtUrlFromNetwork(): String {
        val url = coverArtArchiveApiService.getReleaseCoverArts(releaseId.value).getSmallCoverArtUrl().orEmpty()
        releaseDao.setReleaseCoverArtUrl(releaseId.value, url)
        return url
    }
}
