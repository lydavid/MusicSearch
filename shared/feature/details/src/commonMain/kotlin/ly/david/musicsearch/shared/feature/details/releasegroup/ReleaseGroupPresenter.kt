package ly.david.musicsearch.shared.feature.details.releasegroup

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.slack.circuit.foundation.NavEvent
import com.slack.circuit.foundation.onNavEvent
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import ly.david.musicsearch.core.logging.Logger
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.artist.getDisplayNames
import ly.david.musicsearch.shared.domain.error.HandledException
import ly.david.musicsearch.shared.domain.getNameWithDisambiguation
import ly.david.musicsearch.shared.domain.history.usecase.IncrementLookupHistory
import ly.david.musicsearch.shared.domain.image.ImageMetadataRepository
import ly.david.musicsearch.shared.domain.musicbrainz.usecase.GetMusicBrainzUrl
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.releasegroup.ReleaseGroupDetailsModel
import ly.david.musicsearch.shared.domain.releasegroup.ReleaseGroupRepository
import ly.david.musicsearch.shared.domain.wikimedia.WikimediaRepository
import ly.david.musicsearch.shared.feature.details.utils.filterUrlRelations
import ly.david.musicsearch.ui.common.musicbrainz.LoginPresenter
import ly.david.musicsearch.ui.common.musicbrainz.LoginUiState
import ly.david.musicsearch.ui.common.relation.RelationsPresenter
import ly.david.musicsearch.ui.common.relation.RelationsUiEvent
import ly.david.musicsearch.ui.common.relation.RelationsUiState
import ly.david.musicsearch.ui.common.release.ReleasesListPresenter
import ly.david.musicsearch.ui.common.release.ReleasesListUiEvent
import ly.david.musicsearch.ui.common.release.ReleasesListUiState
import ly.david.musicsearch.ui.common.screen.CoverArtsScreen
import ly.david.musicsearch.ui.common.screen.DetailsScreen
import ly.david.musicsearch.ui.common.screen.RecordVisit
import ly.david.musicsearch.ui.common.topappbar.Tab
import ly.david.musicsearch.ui.common.topappbar.TopAppBarFilterState
import ly.david.musicsearch.ui.common.topappbar.rememberTopAppBarFilterState

internal val releaseGroupTabs = persistentListOf(
    Tab.DETAILS,
    Tab.RELEASES,
    Tab.RELATIONSHIPS,
    Tab.STATS,
)

internal class ReleaseGroupPresenter(
    private val screen: DetailsScreen,
    private val navigator: Navigator,
    private val repository: ReleaseGroupRepository,
    override val incrementLookupHistory: IncrementLookupHistory,
    private val releasesListPresenter: ReleasesListPresenter,
    private val relationsPresenter: RelationsPresenter,
    private val imageMetadataRepository: ImageMetadataRepository,
    private val logger: Logger,
    private val loginPresenter: LoginPresenter,
    private val getMusicBrainzUrl: GetMusicBrainzUrl,
    private val wikimediaRepository: WikimediaRepository,
) : Presenter<ReleaseGroupUiState>, RecordVisit {

    @Composable
    override fun present(): ReleaseGroupUiState {
        var title by rememberSaveable { mutableStateOf(screen.title.orEmpty()) }
        var subtitle by rememberSaveable { mutableStateOf("") }
        var handledException: HandledException? by rememberSaveable { mutableStateOf(null) }
        var numberOfImages: Int? by rememberSaveable { mutableStateOf(null) }
        var releaseGroup: ReleaseGroupDetailsModel? by rememberRetained { mutableStateOf(null) }
        val tabs: ImmutableList<Tab> = releaseGroupTabs
        var selectedTab by rememberSaveable { mutableStateOf(Tab.DETAILS) }
        val topAppBarFilterState = rememberTopAppBarFilterState()
        val query = topAppBarFilterState.filterText
        var forceRefreshDetails by remember { mutableStateOf(false) }
        val detailsLazyListState = rememberLazyListState()
        var snackbarMessage: String? by rememberSaveable { mutableStateOf(null) }
        var isExternalLinksCollapsed by rememberSaveable { mutableStateOf(false) }

        val releasesByEntityUiState = releasesListPresenter.present()
        val releasesEventSink = releasesByEntityUiState.eventSink
        val relationsUiState = relationsPresenter.present()
        val relationsEventSink = relationsUiState.eventSink

        val loginUiState = loginPresenter.present()

        LaunchedEffect(forceRefreshDetails) {
            try {
                val releaseGroupDetailsModel = repository.lookupReleaseGroup(
                    screen.id,
                    forceRefreshDetails,
                )
                title = releaseGroupDetailsModel.getNameWithDisambiguation()
                subtitle = "Release Group by ${releaseGroupDetailsModel.artistCredits.getDisplayNames()}"
                releaseGroup = releaseGroupDetailsModel

                handledException = null
            } catch (ex: HandledException) {
                logger.e(ex)
                handledException = ex
            }
            forceRefreshDetails = false
        }

        RecordVisit(
            mbid = screen.id,
            title = title,
            entity = screen.entity,
            searchHint = "",
        )

        LaunchedEffect(forceRefreshDetails, releaseGroup) {
            val imageMetadataWithCount = imageMetadataRepository.getAndSaveImageMetadata(
                mbid = releaseGroup?.id ?: return@LaunchedEffect,
                entity = MusicBrainzEntity.RELEASE_GROUP,
                forceRefresh = forceRefreshDetails,
            )
            releaseGroup = releaseGroup?.copy(
                imageMetadata = imageMetadataWithCount.imageMetadata,
            )
            numberOfImages = imageMetadataWithCount.count
        }

        LaunchedEffect(forceRefreshDetails, releaseGroup) {
            wikimediaRepository.getWikipediaExtract(
                mbid = releaseGroup?.id ?: return@LaunchedEffect,
                urls = releaseGroup?.urls ?: return@LaunchedEffect,
                forceRefresh = forceRefreshDetails,
            ).onSuccess { wikipediaExtract ->
                releaseGroup = releaseGroup?.copy(
                    wikipediaExtract = wikipediaExtract,
                )
            }.onFailure {
                snackbarMessage = it.message
            }
        }

        LaunchedEffect(
            key1 = query,
            key2 = selectedTab,
        ) {
            topAppBarFilterState.show(
                selectedTab !in listOf(
                    Tab.STATS,
                ),
            )
            val browseMethod = BrowseMethod.ByEntity(
                entityId = screen.id,
                entity = screen.entity,
            )
            when (selectedTab) {
                Tab.DETAILS -> {
                    // Loaded above
                }

                Tab.RELATIONSHIPS -> {
                    relationsEventSink(
                        RelationsUiEvent.GetRelations(
                            byEntityId = screen.id,
                            byEntity = screen.entity,
                        ),
                    )
                    relationsEventSink(RelationsUiEvent.UpdateQuery(query))
                }

                Tab.RELEASES -> {
                    releasesEventSink(
                        ReleasesListUiEvent.Get(
                            browseMethod = browseMethod,
                        ),
                    )
                    releasesEventSink(ReleasesListUiEvent.UpdateQuery(query))
                }

                else -> {
                    // no-op
                }
            }
        }

        fun eventSink(event: ReleaseGroupUiEvent) {
            when (event) {
                ReleaseGroupUiEvent.NavigateUp -> {
                    navigator.pop()
                }

                is ReleaseGroupUiEvent.UpdateTab -> {
                    selectedTab = event.tab
                }

                is ReleaseGroupUiEvent.ClickItem -> {
                    navigator.onNavEvent(
                        NavEvent.GoTo(
                            DetailsScreen(
                                entity = event.entity,
                                id = event.id,
                                title = event.title,
                            ),
                        ),
                    )
                }

                ReleaseGroupUiEvent.ClickImage -> {
                    navigator.onNavEvent(
                        NavEvent.GoTo(
                            CoverArtsScreen(
                                id = screen.id,
                                entity = screen.entity,
                            ),
                        ),
                    )
                }

                ReleaseGroupUiEvent.ForceRefreshDetails -> {
                    forceRefreshDetails = true
                }

                ReleaseGroupUiEvent.ToggleCollapseExpandExternalLinks -> {
                    isExternalLinksCollapsed = !isExternalLinksCollapsed
                }
            }
        }

        return ReleaseGroupUiState(
            title = title,
            subtitle = subtitle,
            tabs = tabs,
            selectedTab = selectedTab,
            topAppBarFilterState = topAppBarFilterState,
            url = getMusicBrainzUrl(screen.entity, screen.id),
            releaseGroup = releaseGroup?.copy(
                urls = releaseGroup?.urls.filterUrlRelations(query = query),
            ),
            snackbarMessage = snackbarMessage,
            detailsUiState = ReleaseGroupDetailsUiState(
                handledException = handledException,
                lazyListState = detailsLazyListState,
                numberOfImages = numberOfImages,
                isExternalLinksCollapsed = isExternalLinksCollapsed,
            ),
            releasesListUiState = releasesByEntityUiState,
            relationsUiState = relationsUiState,
            loginUiState = loginUiState,
            eventSink = ::eventSink,
        )
    }
}

@Stable
internal data class ReleaseGroupUiState(
    val title: String,
    val subtitle: String,
    val tabs: ImmutableList<Tab>,
    val selectedTab: Tab,
    val releaseGroup: ReleaseGroupDetailsModel?,
    val detailsUiState: ReleaseGroupDetailsUiState = ReleaseGroupDetailsUiState(),
    val url: String = "",
    val topAppBarFilterState: TopAppBarFilterState = TopAppBarFilterState(),
    val snackbarMessage: String? = null,
    val releasesListUiState: ReleasesListUiState,
    val relationsUiState: RelationsUiState,
    val loginUiState: LoginUiState = LoginUiState(),
    val eventSink: (ReleaseGroupUiEvent) -> Unit,
) : CircuitUiState

internal data class ReleaseGroupDetailsUiState(
    val handledException: HandledException? = null,
    val numberOfImages: Int? = null,
    val lazyListState: LazyListState = LazyListState(),
    val isExternalLinksCollapsed: Boolean = false,
)

internal sealed interface ReleaseGroupUiEvent : CircuitUiEvent {
    data object NavigateUp : ReleaseGroupUiEvent
    data object ForceRefreshDetails : ReleaseGroupUiEvent
    data class UpdateTab(val tab: Tab) : ReleaseGroupUiEvent
    data class ClickItem(
        val entity: MusicBrainzEntity,
        val id: String,
        val title: String?,
    ) : ReleaseGroupUiEvent

    data object ClickImage : ReleaseGroupUiEvent

    data object ToggleCollapseExpandExternalLinks : ReleaseGroupUiEvent
}
