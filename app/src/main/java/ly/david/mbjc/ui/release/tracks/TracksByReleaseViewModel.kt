package ly.david.mbjc.ui.release.tracks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.insertSeparators
import androidx.paging.map
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import ly.david.data.core.TrackForListItem
import ly.david.data.core.common.transformThisIfNotNullOrEmpty
import ly.david.musicsearch.data.database.dao.MediumDao
import ly.david.musicsearch.data.database.dao.ReleaseDao
import ly.david.musicsearch.data.database.dao.TrackDao
import ly.david.musicsearch.domain.listitem.ListItemModel
import ly.david.musicsearch.domain.listitem.ListSeparator
import ly.david.musicsearch.domain.listitem.TrackListItemModel
import ly.david.musicsearch.domain.listitem.toTrackListItemModel
import ly.david.musicsearch.domain.paging.LookupEntityRemoteMediator
import ly.david.musicsearch.domain.paging.MusicBrainzPagingConfig
import ly.david.musicsearch.domain.release.ReleaseRepository
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class TracksByReleaseViewModel(
    private val repository: ReleaseRepository,
    private val releaseDao: ReleaseDao,
    private val mediumDao: MediumDao,
    private val trackDao: TrackDao,
) : ViewModel() {

    private data class ViewModelState(
        val releaseId: String = "",
        val query: String = "",
    )

    private val releaseId: MutableStateFlow<String> = MutableStateFlow("")
    private val query: MutableStateFlow<String> = MutableStateFlow("")
    private val tracksParamState: Flow<ViewModelState> = combine(releaseId, query) { releaseId, query ->
        ViewModelState(releaseId, query)
    }.distinctUntilChanged()

    fun loadTracks(releaseId: String) {
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
                    remoteMediator = LookupEntityRemoteMediator(
                        hasEntityBeenStored = { hasReleaseTracksBeenStored(releaseId) },
                        lookupEntity = { repository.lookupRelease(releaseId) },
                        deleteLocalEntity = { deleteMediaAndTracksByRelease(releaseId) }
                    ),
                    pagingSourceFactory = {
                        trackDao.getTracksByRelease(
                            releaseId = releaseId,
                            query = "%$query%",
                        )
                    }
                ).flow.map { pagingData ->
                    pagingData
                        .map(TrackForListItem::toTrackListItemModel)
                        .insertSeparators { before: TrackListItemModel?, after: TrackListItemModel? ->
                            if (before?.mediumId != after?.mediumId && after != null) {
                                val medium =
                                    mediumDao.getMediumForTrack(after.id) ?: return@insertSeparators null

                                ListSeparator(
                                    id = "${medium.id}",
                                    text = medium.format.orEmpty() +
                                        (medium.position?.toString() ?: "").transformThisIfNotNullOrEmpty { " $it" } +
                                        medium.name.transformThisIfNotNullOrEmpty { " ($it)" }
                                )
                            } else {
                                null
                            }
                        }
                }
            }
            .distinctUntilChanged()
            .cachedIn(viewModelScope)

    private fun hasReleaseTracksBeenStored(releaseId: String): Boolean {
        // TODO: right now the details tab is coupled with this tracks list tab
        return releaseDao.getReleaseForDetails(releaseId) != null
    }

    private fun deleteMediaAndTracksByRelease(releaseId: String) {
        releaseDao.withTransaction {
            releaseDao.delete(releaseId)
            mediumDao.deleteMediaByRelease(releaseId)
        }
    }
}
