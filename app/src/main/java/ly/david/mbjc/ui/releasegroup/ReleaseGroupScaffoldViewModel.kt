package ly.david.mbjc.ui.releasegroup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.IOException
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ly.david.data.coverart.GetReleaseGroupCoverArtPath
import ly.david.data.coverart.UpdateReleaseGroupCoverArtDao
import ly.david.data.coverart.api.CoverArtArchiveApiService
import ly.david.data.coverart.buildCoverArtUrl
import ly.david.data.domain.ReleaseGroupScaffoldModel
import ly.david.data.getDisplayNames
import ly.david.data.getNameWithDisambiguation
import ly.david.data.network.MusicBrainzResource
import ly.david.data.room.history.LookupHistoryDao
import ly.david.data.room.releasegroup.ReleaseGroupDao
import ly.david.data.repository.ReleaseGroupRepository
import ly.david.ui.common.MusicBrainzResourceViewModel
import ly.david.data.room.history.RecordLookupHistory
import ly.david.ui.common.paging.IRelationsList
import ly.david.ui.common.paging.RelationsList
import retrofit2.HttpException
import timber.log.Timber

@HiltViewModel
internal class ReleaseGroupScaffoldViewModel @Inject constructor(
    private val repository: ReleaseGroupRepository,
    override val lookupHistoryDao: LookupHistoryDao,
    private val relationsList: RelationsList,
    override val coverArtArchiveApiService: CoverArtArchiveApiService,
    private val releaseGroupDao: ReleaseGroupDao,
) : ViewModel(), MusicBrainzResourceViewModel, RecordLookupHistory,
    IRelationsList by relationsList,
    GetReleaseGroupCoverArtPath {

    override val updateReleaseGroupCoverArtDao: UpdateReleaseGroupCoverArtDao
        get() = releaseGroupDao

    private var recordedLookup = false
    override val resource: MusicBrainzResource = MusicBrainzResource.RELEASE_GROUP
    override val title = MutableStateFlow("")
    override val isError = MutableStateFlow(false)

    val subtitle = MutableStateFlow("")
    val releaseGroup: MutableStateFlow<ReleaseGroupScaffoldModel?> = MutableStateFlow(null)
    val url = MutableStateFlow("")

    init {
        relationsList.scope = viewModelScope
        relationsList.repository = repository
    }

    fun loadDataForTab(
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

                        isError.value = false
                    } catch (ex: HttpException) {
                        Timber.e(ex)
                        isError.value = true
                    } catch (ex: IOException) {
                        Timber.e(ex)
                        isError.value = true
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
        val coverArtPath = releaseGroupScaffoldModel.coverArtPath
        url.value = buildCoverArtUrl(
            coverArtPath = coverArtPath ?: getReleaseGroupCoverArtPathFromNetwork(releaseGroupId),
            thumbnail = false
        )
    }
}
