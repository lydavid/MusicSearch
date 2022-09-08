package ly.david.mbjc.ui.releasegroup.releases

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
import ly.david.mbjc.data.domain.ReleaseGroupUiModel
import ly.david.mbjc.data.domain.ReleaseUiModel
import ly.david.mbjc.data.domain.toReleaseUiModel
import ly.david.mbjc.data.network.MusicBrainzApiService
import ly.david.mbjc.data.persistence.release.ReleaseDao
import ly.david.mbjc.data.persistence.release.ReleaseRoomModel
import ly.david.mbjc.data.persistence.release.toReleaseRoomModel
import ly.david.mbjc.data.persistence.releasegroup.ReleaseGroupDao
import ly.david.mbjc.data.persistence.releasegroup.ReleasesReleaseGroups
import ly.david.mbjc.data.persistence.releasegroup.ReleasesReleaseGroupsDao
import ly.david.mbjc.ui.common.paging.MusicBrainzPagingConfig
import ly.david.mbjc.ui.common.paging.BrowseResourceRemoteMediator

// TODO: generalize? reuse for releases by label
//  or make abstract, and override

// TODO: for releases by release group, and releases by label, we can just literally use the same VM/screen
//  but change out what id to use, and what daos to use
@HiltViewModel
internal class ReleasesByReleaseGroupViewModel @Inject constructor(
    private val musicBrainzApiService: MusicBrainzApiService,
    private val releaseGroupRepository: ReleaseGroupRepository,
    private val releaseGroupDao: ReleaseGroupDao,
    private val releasesReleaseGroupsDao: ReleasesReleaseGroupsDao,
    private val releaseDao: ReleaseDao,
) : ViewModel() {

    private data class ViewModelState(
        val releaseGroupId: String = "",
        val query: String = ""
    )

    private val releaseGroupId: MutableStateFlow<String> = MutableStateFlow("")
    private val query: MutableStateFlow<String> = MutableStateFlow("")
    private val paramState = combine(releaseGroupId, query) { releaseGroupId, query ->
        ViewModelState(releaseGroupId, query)
    }.distinctUntilChanged()

    suspend fun lookupReleaseGroup(releaseGroupId: String): ReleaseGroupUiModel =
        releaseGroupRepository.lookupReleaseGroup(releaseGroupId)

    fun updateReleaseGroupId(releaseGroupId: String) {
        this.releaseGroupId.value = releaseGroupId
    }

    fun updateQuery(query: String) {
        this.query.value = query
    }

    @OptIn(ExperimentalPagingApi::class, kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    val pagedReleases: Flow<PagingData<ReleaseUiModel>> =
        paramState.filterNot { it.releaseGroupId.isEmpty() }
            .flatMapLatest { (releaseGroupId, query) ->
                Pager(
                    config = MusicBrainzPagingConfig.pagingConfig,
                    remoteMediator = BrowseResourceRemoteMediator(
                        getRemoteResourceCount = { releaseGroupDao.getReleaseGroup(releaseGroupId)?.releaseCount },
                        getLocalResourceCount = { releasesReleaseGroupsDao.getNumberOfReleasesInReleaseGroup(releaseGroupId) },
                        deleteLocalResource = {
                            releasesReleaseGroupsDao.deleteReleasesInReleaseGroup(releaseGroupId)
                        },
                        browseResource = { offset ->
                            browseReleasesAndStore(releaseGroupId, offset)
                        }
                    ),
                    pagingSourceFactory = { getPagingSource(releaseGroupId, query) }
                ).flow.map { pagingData ->
                    pagingData.map {
                        it.toReleaseUiModel()
                    }
                }
            }
            .distinctUntilChanged()
            .cachedIn(viewModelScope)

    private suspend fun browseReleasesAndStore(releaseGroupId: String, nextOffset: Int): Int {
        val response = musicBrainzApiService.browseReleasesByReleaseGroup(
            releaseGroupId = releaseGroupId,
            offset = nextOffset
        )

        // Only need to update it the first time we ever browse this artist's release groups.
        if (response.offset == 0) {
            releaseGroupDao.setReleaseCount(releaseGroupId, response.count)
        }

        val musicBrainzReleases = response.releases
        releaseDao.insertAll(musicBrainzReleases.map { it.toReleaseRoomModel() })
        releasesReleaseGroupsDao.insertAll(
            musicBrainzReleases.map { release ->
                ReleasesReleaseGroups(release.id, releaseGroupId)
            }
        )

        return musicBrainzReleases.size
    }

    private fun getPagingSource(releaseGroupId: String, query: String): PagingSource<Int, ReleaseRoomModel> = when {
        query.isEmpty() -> {
            releasesReleaseGroupsDao.getReleasesInReleaseGroup(releaseGroupId)
        }
        else -> {
            releasesReleaseGroupsDao.getReleasesInReleaseGroupFiltered(
                releaseGroupId = releaseGroupId,
                query = "%$query%"
            )
        }
    }
}
