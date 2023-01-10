package ly.david.mbjc.ui.releasegroup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ly.david.data.domain.ReleaseGroupScaffoldModel
import ly.david.data.getDisplayNames
import ly.david.data.getNameWithDisambiguation
import ly.david.data.network.MusicBrainzResource
import ly.david.data.network.api.coverart.CoverArtArchiveApiService
import ly.david.data.network.api.coverart.getSmallCoverArtUrl
import ly.david.data.persistence.history.LookupHistoryDao
import ly.david.data.persistence.releasegroup.ReleaseGroupDao
import ly.david.data.repository.ReleaseGroupRepository
import ly.david.mbjc.ui.common.MusicBrainzResourceViewModel
import ly.david.mbjc.ui.common.history.RecordLookupHistory
import ly.david.mbjc.ui.common.paging.IRelationsList
import ly.david.mbjc.ui.common.paging.RelationsList
import retrofit2.HttpException

@HiltViewModel
internal class ReleaseGroupScaffoldViewModel @Inject constructor(
    private val repository: ReleaseGroupRepository,
    override val lookupHistoryDao: LookupHistoryDao,
    private val relationsList: RelationsList,
    private val coverArtArchiveApiService: CoverArtArchiveApiService,
    private val releaseGroupDao: ReleaseGroupDao,
) : ViewModel(), MusicBrainzResourceViewModel, RecordLookupHistory,
    IRelationsList by relationsList {

    private var recordedLookup = false
    override val resource: MusicBrainzResource = MusicBrainzResource.RELEASE_GROUP
    override val title = MutableStateFlow("")
    override val isError = MutableStateFlow(false)

    val subtitle = MutableStateFlow("")
    val isFullScreenError = MutableStateFlow(false)
    val releaseGroup: MutableStateFlow<ReleaseGroupScaffoldModel?> = MutableStateFlow(null)
    val url = MutableStateFlow("")

    init {
        relationsList.scope = viewModelScope
        relationsList.repository = repository
    }

    fun onSelectedTabChange(
        releaseGroupId: String,
        selectedTab: ReleaseGroupTab
    ) {
        when (selectedTab) {
            ReleaseGroupTab.DETAILS -> {
                viewModelScope.launch {
                    try {
                        val releaseGroupListItemModel = repository.lookupReleaseGroup(releaseGroupId)
                        if (title.value.isEmpty()) {
                            title.value = releaseGroupListItemModel.getNameWithDisambiguation()
                        }
                        subtitle.value = "Release Group by ${releaseGroupListItemModel.artistCredits.getDisplayNames()}"
                        releaseGroup.value = releaseGroupListItemModel

                        getCoverArtUrl(releaseGroupId, releaseGroupListItemModel)

                        isFullScreenError.value = false
                    } catch (ex: Exception) {
                        isFullScreenError.value = true
                    }

                    if (!recordedLookup) {
                        recordLookupHistory(
                            resourceId = releaseGroupId,
                            resource = resource,
                            summary = title.value
                        )
                        recordedLookup = true
                    }
                }
            }
            ReleaseGroupTab.RELEASES -> {
                // Not handled here.
            }
            ReleaseGroupTab.RELATIONSHIPS -> loadRelations(releaseGroupId)
            ReleaseGroupTab.STATS -> {
                // Not handled here.
            }
        }
    }

    private suspend fun getCoverArtUrl(
        releaseGroupId: String,
        releaseGroupScaffoldModel: ReleaseGroupScaffoldModel
    ) {
        try {
            val coverArtUrl = releaseGroupScaffoldModel.coverArtUrl
            if (coverArtUrl != null) {
                url.value = coverArtUrl
            } else if (releaseGroupScaffoldModel.hasCoverArt != false) {
                url.value = getCoverArtUrlFromNetwork(releaseGroupId)
            }
        } catch (ex: HttpException) {
            if (ex.code() == 404) {
                releaseGroupDao.setReleaseGroupHasCoverArt(releaseGroupId, false)
            }
        }
    }

    /**
     * Returns a url to the cover art. Empty if none found.
     *
     * Also set it in the release group.
     */
    private suspend fun getCoverArtUrlFromNetwork(releaseGroupId: String): String {
        val url = coverArtArchiveApiService.getReleaseGroupCoverArts(releaseGroupId).getSmallCoverArtUrl().orEmpty()
        releaseGroupDao.withTransaction {
            releaseGroupDao.setReleaseGroupCoverArtUrl(releaseGroupId, url)
            releaseGroupDao.setReleaseGroupHasCoverArt(releaseGroupId, true)
        }
        return url
    }
}
