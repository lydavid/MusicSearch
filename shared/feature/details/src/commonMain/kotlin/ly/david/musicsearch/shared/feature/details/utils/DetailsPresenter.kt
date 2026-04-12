package ly.david.musicsearch.shared.feature.details.utils

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.intl.Locale
import com.slack.circuit.foundation.NavEvent
import com.slack.circuit.foundation.onNavEvent
import com.slack.circuit.retained.collectAsRetainedState
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import kotlinx.collections.immutable.ImmutableList
import ly.david.musicsearch.core.logging.Logger
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.collection.CollectionRepository
import ly.david.musicsearch.shared.domain.details.MusicBrainzDetailsModel
import ly.david.musicsearch.shared.domain.details.ReleaseDetailsModel
import ly.david.musicsearch.shared.domain.error.HandledException
import ly.david.musicsearch.shared.domain.getNameWithDisambiguation
import ly.david.musicsearch.shared.domain.history.usecase.IncrementLookupHistory
import ly.david.musicsearch.shared.domain.image.ImageMetadata
import ly.david.musicsearch.shared.domain.image.ImageMetadataRepository
import ly.david.musicsearch.shared.domain.listen.ListenBrainzAuthStore
import ly.david.musicsearch.shared.domain.listitem.SelectableId
import ly.david.musicsearch.shared.domain.musicbrainz.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.musicbrainz.usecase.GetMusicBrainzUrl
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.domain.preferences.AppPreferences
import ly.david.musicsearch.shared.domain.wikimedia.WikimediaRepository
import ly.david.musicsearch.shared.domain.wikimedia.WikipediaExtract
import ly.david.musicsearch.ui.common.list.AllEntitiesListPresenter
import ly.david.musicsearch.ui.common.list.AllEntitiesListUiEvent
import ly.david.musicsearch.ui.common.list.AllEntitiesListUiState
import ly.david.musicsearch.ui.common.list.getTotalLocalCount
import ly.david.musicsearch.ui.common.locale.getAnnotatedName
import ly.david.musicsearch.ui.common.musicbrainz.MusicBrainzLoginPresenter
import ly.david.musicsearch.ui.common.musicbrainz.MusicBrainzLoginUiState
import ly.david.musicsearch.ui.common.screen.ArtistCollaborationScreen
import ly.david.musicsearch.ui.common.screen.CoverArtsScreen
import ly.david.musicsearch.ui.common.screen.DetailsScreen
import ly.david.musicsearch.ui.common.screen.RecordVisit
import ly.david.musicsearch.ui.common.topappbar.SelectionState
import ly.david.musicsearch.ui.common.topappbar.Tab
import ly.david.musicsearch.ui.common.topappbar.TopAppBarFilterState
import ly.david.musicsearch.ui.common.topappbar.rememberSelectionState
import ly.david.musicsearch.ui.common.topappbar.rememberTopAppBarFilterState
import kotlin.time.Clock
import kotlin.time.Instant

internal abstract class DetailsPresenter<DetailsModel : MusicBrainzDetailsModel>(
    private val screen: DetailsScreen,
    private val navigator: Navigator,
    override val incrementLookupHistory: IncrementLookupHistory,
    private val allEntitiesListPresenter: AllEntitiesListPresenter,
    private val imageMetadataRepository: ImageMetadataRepository,
    private val logger: Logger,
    private val musicBrainzLoginPresenter: MusicBrainzLoginPresenter,
    private val getMusicBrainzUrl: GetMusicBrainzUrl,
    private val wikimediaRepository: WikimediaRepository,
    private val collectionRepository: CollectionRepository,
    private val listenBrainzAuthStore: ListenBrainzAuthStore,
    private val appPreferences: AppPreferences,
) : Presenter<DetailsUiState<DetailsModel>>, RecordVisit {

    abstract fun getTabs(): ImmutableList<Tab>

    open fun getSubtitle(detailsModel: DetailsModel): String {
        return ""
    }

    open fun getSearchHint(detailsModel: DetailsModel?): String? {
        return ""
    }

    abstract suspend fun lookupDetailsModel(
        id: String,
        forceRefresh: Boolean,
    ): DetailsModel

    @Suppress("CyclomaticComplexMethod")
    @Composable
    final override fun present(): DetailsUiState<DetailsModel> {
        val browseMethod = BrowseMethod.ByEntity(
            entityId = screen.id,
            entityType = screen.entityType,
        )
        var subtitle by rememberSaveable { mutableStateOf("") }
        var isLoading by rememberSaveable { mutableStateOf(true) }
        var handledException: HandledException? by rememberSaveable { mutableStateOf(null) }
        var numberOfImages: Int? by rememberSaveable { mutableStateOf(null) }
        var imageMetadata: ImageMetadata? by rememberRetained { mutableStateOf(null) }
        var wikipediaExtract: WikipediaExtract by rememberRetained { mutableStateOf(WikipediaExtract()) }
        var detailsModel: DetailsModel? by rememberRetained { mutableStateOf(null) }
        var selectedTab by rememberSaveable { mutableStateOf(Tab.DETAILS) }
        val topAppBarFilterState = rememberTopAppBarFilterState()
        val query = topAppBarFilterState.filterText
        var forceRefreshDetails by remember { mutableStateOf(false) }
        val detailsLazyListState = rememberLazyListState()
        var snackbarMessage: String? by rememberSaveable { mutableStateOf(null) }
        var isReleaseEventsCollapsed by rememberSaveable { mutableStateOf(false) }
        var isExternalLinksCollapsed by rememberSaveable { mutableStateOf(false) }
        var isAliasesCollapsed by rememberSaveable { mutableStateOf(false) }
        val collected by collectionRepository.observeEntityIsInACollection(
            entityId = screen.id,
        ).collectAsRetainedState(false)
        val loggedInUsername by listenBrainzAuthStore.username.collectAsRetainedState("")
        val scrollToHideTopAppBar by appPreferences.scrollToHideTopAppBar.collectAsRetainedState(false)

        val entitiesListUiState = allEntitiesListPresenter.present()
        val entitiesListEventSink = entitiesListUiState.eventSink

        val selectionState = rememberSelectionState(
            totalCount = entitiesListUiState.getTotalLocalCount(tab = selectedTab),
        )

        val loginUiState = musicBrainzLoginPresenter.present()

        var detailsRequestKey by remember { mutableStateOf(0) }
        var imageMetadataRequestKey by remember { mutableStateOf(0) }
        var wikipediaRequestKey by remember { mutableStateOf(0) }

        LaunchedEffect(detailsRequestKey) {
            try {
                isLoading = true
                val newDetailsModel = lookupDetailsModel(
                    id = screen.id,
                    forceRefresh = forceRefreshDetails,
                )
                subtitle = getSubtitle(newDetailsModel)
                detailsModel = newDetailsModel

                handledException = null
            } catch (ex: HandledException) {
                logger.e(ex)
                handledException = ex
            }
            isLoading = false
            forceRefreshDetails = false
        }

        RecordVisit(
            oldId = screen.id,
            mbid = detailsModel?.id,
            title = detailsModel.getAnnotatedName().text,
            entity = screen.entityType,
            searchHint = getSearchHint(detailsModel),
        )

        LaunchedEffect(detailsModel?.id, imageMetadataRequestKey) {
            val showImage = setOf(
                MusicBrainzEntityType.ARTIST,
                MusicBrainzEntityType.EVENT,
                MusicBrainzEntityType.RELEASE,
                MusicBrainzEntityType.RELEASE_GROUP,
            ).contains(screen.entityType)
            if (!showImage) return@LaunchedEffect

            val imageMetadataWithCount = imageMetadataRepository.getAndSaveImageMetadata(
                detailsModel = detailsModel ?: return@LaunchedEffect,
                entity = screen.entityType,
                forceRefresh = forceRefreshDetails,
            )
            numberOfImages = imageMetadataWithCount?.count ?: 0
            imageMetadata = imageMetadataWithCount?.imageMetadata ?: return@LaunchedEffect
        }

        LaunchedEffect(detailsModel?.id, wikipediaRequestKey) {
            wikimediaRepository.getWikipediaExtract(
                mbid = detailsModel?.id ?: return@LaunchedEffect,
                urls = detailsModel?.urls ?: return@LaunchedEffect,
                languageTag = Locale.current.language,
                forceRefresh = forceRefreshDetails,
            ).onSuccess { newWikipediaExtract ->
                wikipediaExtract = newWikipediaExtract
            }.onFailure {
                snackbarMessage = it.message
            }
        }

        LaunchedEffect(
            key1 = query,
            key2 = selectedTab,
            key3 = detailsModel,
        ) {
            entitiesListEventSink(
                AllEntitiesListUiEvent.Get(
                    tab = selectedTab,
                    browseMethod = browseMethod,
                    query = query,
                    isRemote = true,
                    mostListenedTrackCount = (detailsModel as? ReleaseDetailsModel)?.mostListenedTrackCount ?: 0,
                ),
            )
        }

        fun eventSink(event: DetailsUiEvent) {
            when (event) {
                DetailsUiEvent.NavigateUp -> {
                    navigator.pop()
                }

                is DetailsUiEvent.UpdateTab -> {
                    selectedTab = event.tab
                    // TODO: this will clear when returning from backstack
                    //  whereas returning to a collection does not clear
                    //  When switching tabs, it is necessary to clear,
                    //  otherwise we may add entities to the wrong collection type
                    selectionState.clearSelection()
                }

                DetailsUiEvent.RefreshLocalDetails -> {
                    detailsRequestKey++
                }

                DetailsUiEvent.ForceRefreshDetails -> {
                    forceRefreshDetails = true
                    detailsRequestKey++
                    imageMetadataRequestKey++
                    wikipediaRequestKey++
                }

                is DetailsUiEvent.ToggleSelectItem -> {
                    selectionState.toggleSelection(event.collectableId, event.loadedCount)
                }

                is DetailsUiEvent.ToggleSelectAllItems -> {
                    selectionState.toggleSelectAll(event.collectableIds)
                }

                is DetailsUiEvent.ClickItem -> {
                    navigator.onNavEvent(
                        NavEvent.GoTo(
                            DetailsScreen(
                                entityType = event.entityType,
                                id = event.id,
                            ),
                        ),
                    )
                }

                is DetailsUiEvent.GoToScreen -> {
                    navigator.onNavEvent(
                        NavEvent.GoTo(
                            event.screen,
                        ),
                    )
                }

                DetailsUiEvent.ClickImage -> {
                    navigator.onNavEvent(
                        NavEvent.GoTo(
                            CoverArtsScreen(
                                entity = MusicBrainzEntity(
                                    id = screen.id,
                                    type = screen.entityType,
                                ),
                            ),
                        ),
                    )
                }

                DetailsUiEvent.NavigateToCollaboratorsGraph -> {
                    navigator.onNavEvent(
                        NavEvent.GoTo(
                            ArtistCollaborationScreen(
                                id = screen.id,
                                name = detailsModel?.getNameWithDisambiguation().orEmpty(),
                            ),
                        ),
                    )
                }

                DetailsUiEvent.ToggleCollapseExpandReleaseEvents -> {
                    isReleaseEventsCollapsed = !isReleaseEventsCollapsed
                }

                DetailsUiEvent.ToggleCollapseExpandExternalLinks -> {
                    isExternalLinksCollapsed = !isExternalLinksCollapsed
                }

                DetailsUiEvent.ToggleCollapseExpandAliases -> {
                    isAliasesCollapsed = !isAliasesCollapsed
                }
            }
        }

        return DetailsUiState(
            browseMethod = browseMethod,
            subtitle = subtitle,
            tabs = getTabs(),
            selectedTab = selectedTab,
            url = getMusicBrainzUrl(entity = MusicBrainzEntity(type = screen.entityType, id = screen.id)),
            detailsModel = detailsModel,
            collected = collected,
            snackbarMessage = snackbarMessage,
            topAppBarFilterState = topAppBarFilterState,
            selectionState = selectionState,
            showListenSubmission = loggedInUsername.isNotEmpty(),
            detailsTabUiState = DetailsTabUiState(
                isLoading = isLoading,
                handledException = handledException,
                imageMetadata = imageMetadata,
                numberOfImages = numberOfImages,
                wikipediaExtract = wikipediaExtract,
                lazyListState = detailsLazyListState,
                isReleaseEventsCollapsed = isReleaseEventsCollapsed,
                isExternalLinksCollapsed = isExternalLinksCollapsed,
                isAliasesCollapsed = isAliasesCollapsed,
            ),
            allEntitiesListUiState = entitiesListUiState,
            musicBrainzLoginUiState = loginUiState,
            scrollToHideTopAppBar = scrollToHideTopAppBar,
            eventSink = ::eventSink,
        )
    }
}

internal data class DetailsUiState<DetailsModel : MusicBrainzDetailsModel>(
    val browseMethod: BrowseMethod.ByEntity,
    val subtitle: String = "",
    val tabs: ImmutableList<Tab>,
    val selectedTab: Tab,
    val url: String = "",
    val detailsModel: DetailsModel?,
    val collected: Boolean = false,
    val snackbarMessage: String? = null,
    val topAppBarFilterState: TopAppBarFilterState = TopAppBarFilterState(),
    val selectionState: SelectionState = SelectionState(),
    val showListenSubmission: Boolean = false,
    val detailsTabUiState: DetailsTabUiState = DetailsTabUiState(),
    val allEntitiesListUiState: AllEntitiesListUiState = AllEntitiesListUiState(),
    val musicBrainzLoginUiState: MusicBrainzLoginUiState = MusicBrainzLoginUiState(),
    val scrollToHideTopAppBar: Boolean,
    val eventSink: (DetailsUiEvent) -> Unit = {},
) : CircuitUiState

internal data class DetailsTabUiState(
    val isLoading: Boolean = false,
    val handledException: HandledException? = null,
    val imageMetadata: ImageMetadata? = null,
    val numberOfImages: Int? = null,
    val wikipediaExtract: WikipediaExtract = WikipediaExtract(),
    val lazyListState: LazyListState = LazyListState(),
    val isReleaseEventsCollapsed: Boolean = false,
    val isExternalLinksCollapsed: Boolean = false,
    val isAliasesCollapsed: Boolean = false,
    val now: Instant = Clock.System.now(),
)

internal sealed interface DetailsUiEvent : CircuitUiEvent {
    data object NavigateUp : DetailsUiEvent

    data object RefreshLocalDetails : DetailsUiEvent

    data object ForceRefreshDetails : DetailsUiEvent

    data class UpdateTab(val tab: Tab) : DetailsUiEvent

    data class ToggleSelectItem(
        val collectableId: SelectableId,
        val loadedCount: Int,
    ) : DetailsUiEvent

    data class ToggleSelectAllItems(
        val collectableIds: List<SelectableId>,
    ) : DetailsUiEvent

    data class ClickItem(
        val entityType: MusicBrainzEntityType,
        val id: String,
    ) : DetailsUiEvent

    data class GoToScreen(
        val screen: Screen,
    ) : DetailsUiEvent

    data object ClickImage : DetailsUiEvent

    data object NavigateToCollaboratorsGraph : DetailsUiEvent

    data object ToggleCollapseExpandExternalLinks : DetailsUiEvent

    data object ToggleCollapseExpandReleaseEvents : DetailsUiEvent

    data object ToggleCollapseExpandAliases : DetailsUiEvent
}
