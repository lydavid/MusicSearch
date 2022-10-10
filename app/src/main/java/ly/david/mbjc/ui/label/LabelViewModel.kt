package ly.david.mbjc.ui.label

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.cachedIn
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import ly.david.mbjc.data.Label
import ly.david.mbjc.data.domain.ReleaseUiModel
import ly.david.mbjc.data.domain.toReleaseUiModel
import ly.david.mbjc.data.network.MusicBrainzApiService
import ly.david.mbjc.data.persistence.history.LookupHistoryDao
import ly.david.mbjc.data.persistence.label.LabelDao
import ly.david.mbjc.data.persistence.label.ReleasesLabels
import ly.david.mbjc.data.persistence.label.ReleasesLabelsDao
import ly.david.mbjc.data.persistence.release.ReleaseDao
import ly.david.mbjc.data.persistence.release.ReleaseRoomModel
import ly.david.mbjc.data.persistence.release.toReleaseRoomModel
import ly.david.mbjc.ui.common.history.RecordLookupHistory
import ly.david.mbjc.ui.common.paging.BrowseResourceRemoteMediator
import ly.david.mbjc.ui.common.paging.MusicBrainzPagingConfig

@HiltViewModel
internal class LabelViewModel @Inject constructor(
    private val musicBrainzApiService: MusicBrainzApiService,
    private val labelRepository: LabelRepository,
    private val labelDao: LabelDao,
    private val releasesLabelsDao: ReleasesLabelsDao,
    private val releaseDao: ReleaseDao,
    override val lookupHistoryDao: LookupHistoryDao
) : ViewModel(), RecordLookupHistory {

    private data class ViewModelState(
        val labelId: String = "",
        val query: String = ""
    )

    private val labelId: MutableStateFlow<String> = MutableStateFlow("")
    private val query: MutableStateFlow<String> = MutableStateFlow("")
    private val paramState = combine(labelId, query) { labelId, query ->
        ViewModelState(labelId, query)
    }.distinctUntilChanged()

    suspend fun lookupLabel(labelId: String): Label =
        labelRepository.lookupLabel(labelId)

    fun updateLabelId(labelId: String) {
        this.labelId.value = labelId
    }

    fun updateQuery(query: String) {
        this.query.value = query
    }

    @OptIn(ExperimentalPagingApi::class, kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    val pagedReleases: Flow<PagingData<ReleaseUiModel>> =
        paramState.filterNot { it.labelId.isEmpty() }
            .flatMapLatest { (labelId, query) ->
                Pager(
                    config = MusicBrainzPagingConfig.pagingConfig,
                    remoteMediator = BrowseResourceRemoteMediator(
                        getRemoteResourceCount = { labelDao.getLabel(labelId)?.releaseCount },
                        getLocalResourceCount = { releasesLabelsDao.getNumberOfReleasesByLabel(labelId) },
                        deleteLocalResource = { releasesLabelsDao.deleteReleasesByLabel(labelId) },
                        browseResource = { offset ->
                            browseReleasesAndStore(labelId, offset)
                        }
                    ),
                    pagingSourceFactory = { getPagingSource(labelId, query) }
                ).flow.map { pagingData ->
                    pagingData.map {
                        it.toReleaseUiModel()
                    }
                }
            }
            .distinctUntilChanged()
            .cachedIn(viewModelScope)

    private suspend fun browseReleasesAndStore(labelId: String, nextOffset: Int): Int {
        val response = musicBrainzApiService.browseReleasesByLabel(
            labelId = labelId,
            offset = nextOffset
        )

        // Only need to update it the first time we ever browse this artist's release groups.
        if (response.offset == 0) {
            labelDao.setReleaseCount(labelId, response.count)
        }

        val musicBrainzReleases = response.releases
        releaseDao.insertAll(musicBrainzReleases.map { it.toReleaseRoomModel() })
        releasesLabelsDao.insertAll(
            musicBrainzReleases.map { release ->
                ReleasesLabels(release.id, labelId)
            }
        )

        return musicBrainzReleases.size
    }

    private fun getPagingSource(labelId: String, query: String): PagingSource<Int, ReleaseRoomModel> = when {
        query.isEmpty() -> {
            releasesLabelsDao.getReleasesByLabel(labelId)
        }
        else -> {
            releasesLabelsDao.getReleasesByLabelFiltered(
                labelId = labelId,
                query = "%$query%"
            )
        }
    }
}
