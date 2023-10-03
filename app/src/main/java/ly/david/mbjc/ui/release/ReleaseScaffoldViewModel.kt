package ly.david.mbjc.ui.release

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ly.david.data.common.network.RecoverableNetworkException
import ly.david.data.core.artist.getDisplayNames
import ly.david.data.core.getNameWithDisambiguation
import ly.david.data.core.history.LookupHistory
import ly.david.data.core.network.MusicBrainzEntity
import ly.david.data.coverart.ReleaseImageRepository
import ly.david.musicsearch.domain.history.IncrementLookupHistoryUseCase
import ly.david.musicsearch.domain.release.ReleaseRepository
import ly.david.musicsearch.domain.release.ReleaseScaffoldModel
import ly.david.ui.common.MusicBrainzEntityViewModel
import ly.david.ui.common.paging.IRelationsList
import ly.david.ui.common.paging.RelationsList
import org.koin.android.annotation.KoinViewModel
import timber.log.Timber

@KoinViewModel
internal class ReleaseScaffoldViewModel(
    private val incrementLookupHistoryUseCase: IncrementLookupHistoryUseCase,
    private val releaseImageRepository: ReleaseImageRepository,
    private val repository: ReleaseRepository,
    private val relationsList: RelationsList,
) : ViewModel(),
    MusicBrainzEntityViewModel,
    IRelationsList by relationsList {

    private var recordedLookup = false
    override val entity: MusicBrainzEntity = MusicBrainzEntity.RELEASE
    override val title = MutableStateFlow("")
    override val isError = MutableStateFlow(false)

    val release: MutableStateFlow<ReleaseScaffoldModel?> = MutableStateFlow(null)
    val subtitle = MutableStateFlow("")
    val url = MutableStateFlow("")

    init {
        relationsList.scope = viewModelScope
        relationsList.relationsListRepository = repository
    }

    fun loadDataForTab(
        releaseId: String,
        selectedTab: ReleaseTab,
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
                    } catch (ex: RecoverableNetworkException) {
                        Timber.e(ex)
                        isError.value = true
                    }

                    // This will still record a lookup that failed, allowing for us to quickly get back to it later.
                    // However, reloading it will not record another visit, so its title will remain empty in history.
                    // But clicking on it will update its title, so we're not fixing it right now.
                    if (!recordedLookup) {
                        incrementLookupHistoryUseCase(
                            LookupHistory(
                                mbid = releaseId,
                                title = title.value,
                                entity = entity,
                            )
                        )
                        recordedLookup = true
                    }
                }
            }

            ReleaseTab.RELATIONSHIPS -> loadRelations(releaseId)
            else -> {
                // Not handled here.
            }
        }
    }

    private suspend fun fetchCoverArt(
        releaseId: String,
        releaseScaffoldModel: ReleaseScaffoldModel,
    ) {
        val imageUrl = releaseScaffoldModel.imageUrl
        url.value = imageUrl ?: releaseImageRepository.getReleaseCoverArtUrlFromNetwork(
            releaseId = releaseId,
            thumbnail = false
        )
    }
}
