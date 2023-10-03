package ly.david.mbjc.ui.releasegroup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ly.david.data.common.network.RecoverableNetworkException
import ly.david.data.core.artist.getDisplayNames
import ly.david.data.core.getNameWithDisambiguation
import ly.david.data.core.history.LookupHistory
import ly.david.data.core.network.MusicBrainzEntity
import ly.david.data.coverart.ReleaseGroupImageRepository
import ly.david.musicsearch.domain.history.IncrementLookupHistoryUseCase
import ly.david.musicsearch.domain.releasegroup.ReleaseGroupRepository
import ly.david.musicsearch.domain.releasegroup.ReleaseGroupScaffoldModel
import ly.david.ui.common.MusicBrainzEntityViewModel
import ly.david.ui.common.paging.IRelationsList
import ly.david.ui.common.paging.RelationsList
import org.koin.android.annotation.KoinViewModel
import timber.log.Timber

@KoinViewModel
internal class ReleaseGroupScaffoldViewModel(
    private val repository: ReleaseGroupRepository,
    private val incrementLookupHistoryUseCase: IncrementLookupHistoryUseCase,
    private val relationsList: RelationsList,
    private val releaseGroupImageRepository: ReleaseGroupImageRepository,
) : ViewModel(),
    MusicBrainzEntityViewModel,
    IRelationsList by relationsList {

    private var recordedLookup = false
    override val entity: MusicBrainzEntity = MusicBrainzEntity.RELEASE_GROUP
    override val title = MutableStateFlow("")
    override val isError = MutableStateFlow(false)

    val subtitle = MutableStateFlow("")
    val releaseGroup: MutableStateFlow<ReleaseGroupScaffoldModel?> = MutableStateFlow(null)
    val url = MutableStateFlow("")

    init {
        relationsList.scope = viewModelScope
        relationsList.relationsListRepository = repository
    }

    fun loadDataForTab(
        releaseGroupId: String,
        selectedTab: ReleaseGroupTab,
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

                        fetchCoverArt(releaseGroupId, releaseGroupListItemModel)

                        isError.value = false
                    } catch (ex: RecoverableNetworkException) {
                        Timber.e(ex)
                        isError.value = true
                    }

                    if (!recordedLookup) {
                        incrementLookupHistoryUseCase(
                            LookupHistory(
                                mbid = releaseGroupId,
                                title = title.value,
                                entity = entity,
                            )
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

    private suspend fun fetchCoverArt(
        releaseGroupId: String,
        releaseGroupScaffoldModel: ReleaseGroupScaffoldModel,
    ) {
        val imageUrl = releaseGroupScaffoldModel.imageUrl
        url.value = imageUrl ?: releaseGroupImageRepository.getReleaseGroupCoverArtUrlFromNetwork(
            releaseGroupId = releaseGroupId,
            thumbnail = false
        )
    }
}
