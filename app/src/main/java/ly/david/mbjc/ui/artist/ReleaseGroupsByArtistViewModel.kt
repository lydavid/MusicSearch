package ly.david.mbjc.ui.artist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import ly.david.mbjc.data.domain.UiReleaseGroup
import ly.david.mbjc.data.domain.toUiReleaseGroup
import ly.david.mbjc.data.network.BROWSE_LIMIT
import ly.david.mbjc.data.network.MusicBrainzApiService
import ly.david.mbjc.data.persistence.ArtistDao
import ly.david.mbjc.data.persistence.ReleaseGroupArtistDao
import ly.david.mbjc.data.persistence.ReleaseGroupDao

@HiltViewModel
class ReleaseGroupsByArtistViewModel @Inject constructor(
    private val musicBrainzApiService: MusicBrainzApiService,
    private val releaseGroupDao: ReleaseGroupDao,
    private val releaseGroupArtistDao: ReleaseGroupArtistDao,
    private val artistDao: ArtistDao,
) : ViewModel() {

    private data class ViewModelState(
        val artistId: String = "",
        val query: String = ""
    )

    private val artistId: MutableStateFlow<String> = MutableStateFlow("")
    private val query: MutableStateFlow<String> = MutableStateFlow("")
    private val paramState = artistId.combine(query) { artistId, query ->
        ViewModelState(artistId, query)
    }.distinctUntilChanged()

    fun updateArtist(artistId: String) {
        this.artistId.value = artistId
    }

    fun updateQuery(query: String) {
        this.query.value = query
    }

    @OptIn(ExperimentalPagingApi::class, kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    val pagedReleaseGroups: Flow<PagingData<UiReleaseGroup>> =
        paramState.flatMapLatest { paramState ->
            Pager(
                config = PagingConfig(
                    pageSize = BROWSE_LIMIT
                ),
                remoteMediator = ReleaseGroupsRemoteMediator(
                    musicBrainzApiService = musicBrainzApiService,
                    releaseGroupDao = releaseGroupDao,
                    releaseGroupArtistDao = releaseGroupArtistDao,
                    artistDao = artistDao,
                    artistId = paramState.artistId
                ),
                pagingSourceFactory = {
                    if (paramState.query.isEmpty()) {
                        releaseGroupDao.getReleaseGroupsByArtist(paramState.artistId)
                    } else {
                        releaseGroupDao.getReleaseGroupsByArtistFiltered(
                            paramState.artistId,
                            "%${paramState.query}%"
                        )
                    }
                }
            ).flow.map { pagingData ->
                pagingData.map {
                    it.toUiReleaseGroup(releaseGroupArtistDao.getReleaseGroupArtistCredits(it.id))
                }
            }
        }
            .distinctUntilChanged()
            .cachedIn(viewModelScope)
}
