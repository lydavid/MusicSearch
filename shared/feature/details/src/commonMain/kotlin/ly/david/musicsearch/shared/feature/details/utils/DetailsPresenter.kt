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
import com.slack.circuit.foundation.NavEvent
import com.slack.circuit.foundation.onNavEvent
import com.slack.circuit.retained.collectAsRetainedState
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.collections.immutable.ImmutableList
import ly.david.musicsearch.core.logging.Logger
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.collection.CollectionRepository
import ly.david.musicsearch.shared.domain.details.MusicBrainzDetailsModel
import ly.david.musicsearch.shared.domain.error.HandledException
import ly.david.musicsearch.shared.domain.getNameWithDisambiguation
import ly.david.musicsearch.shared.domain.history.usecase.IncrementLookupHistory
import ly.david.musicsearch.shared.domain.image.ImageMetadataRepository
import ly.david.musicsearch.shared.domain.musicbrainz.usecase.GetMusicBrainzUrl
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.domain.wikimedia.WikimediaRepository
import ly.david.musicsearch.shared.feature.details.alias.filterAliases
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
import ly.david.musicsearch.ui.common.topappbar.toMusicBrainzEntity
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
        var subtitle by rememberSaveable { mutableStateOf("") }
        var isLoading by rememberSaveable { mutableStateOf(true) }
        var handledException: HandledException? by rememberSaveable { mutableStateOf(null) }
        var numberOfImages: Int? by rememberSaveable { mutableStateOf(null) }
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

        val entitiesListUiState = allEntitiesListPresenter.present()
        val entitiesListEventSink = entitiesListUiState.eventSink

        val selectionState = rememberSelectionState(
            totalCount = entitiesListUiState.getTotalLocalCount(selectedTab.toMusicBrainzEntity()),
        )

        val loginUiState = musicBrainzLoginPresenter.present()

        LaunchedEffect(forceRefreshDetails) {
            try {
                isLoading = true
                val newDetailsModel = lookupDetailsModel(
                    screen.id,
                    forceRefreshDetails,
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
            mbid = screen.id,
            title = detailsModel.getAnnotatedName().text,
            entity = screen.entity,
            searchHint = getSearchHint(detailsModel),
        )

        LaunchedEffect(forceRefreshDetails, detailsModel) {
            val showImage = setOf(
                MusicBrainzEntityType.ARTIST,
                MusicBrainzEntityType.EVENT,
                MusicBrainzEntityType.RELEASE,
                MusicBrainzEntityType.RELEASE_GROUP,
            ).contains(screen.entity)
            if (!showImage) return@LaunchedEffect

            val imageMetadataWithCount = imageMetadataRepository.getAndSaveImageMetadata(
                detailsModel = detailsModel ?: return@LaunchedEffect,
                entity = screen.entity,
                forceRefresh = forceRefreshDetails,
            )
            detailsModel = detailsModel?.withImageMetadata(
                imageMetadata = imageMetadataWithCount.imageMetadata,
            ) as DetailsModel?
            numberOfImages = imageMetadataWithCount.count
        }

        LaunchedEffect(forceRefreshDetails, detailsModel) {
            wikimediaRepository.getWikipediaExtract(
                mbid = detailsModel?.id ?: return@LaunchedEffect,
                urls = detailsModel?.urls ?: return@LaunchedEffect,
                forceRefresh = forceRefreshDetails,
            ).onSuccess { wikipediaExtract ->
                detailsModel = detailsModel?.withWikipediaExtract(
                    wikipediaExtract = wikipediaExtract,
                ) as DetailsModel?
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
            entitiesListEventSink(
                AllEntitiesListUiEvent.Get(
                    tab = selectedTab,
                    browseMethod = browseMethod,
                    query = query,
                    isRemote = true,
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

                DetailsUiEvent.ForceRefreshDetails -> {
                    forceRefreshDetails = true
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
                                entity = event.entity,
                                id = event.id,
                            ),
                        ),
                    )
                }

                DetailsUiEvent.ClickImage -> {
                    navigator.onNavEvent(
                        NavEvent.GoTo(
                            CoverArtsScreen(
                                id = screen.id,
                                entity = screen.entity,
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
            subtitle = subtitle,
            tabs = getTabs(),
            selectedTab = selectedTab,
            url = getMusicBrainzUrl(screen.entity, screen.id),
            detailsModel = detailsModel
                ?.withUrls(
                    urls = detailsModel?.urls.filterUrlRelations(query = query),
                )
                ?.withAliases(
                    aliases = detailsModel?.aliases.filterAliases(query = query),
                ) as DetailsModel?,
            collected = collected,
            snackbarMessage = snackbarMessage,
            topAppBarFilterState = topAppBarFilterState,
            selectionState = selectionState,
            detailsTabUiState = DetailsTabUiState(
                isLoading = isLoading,
                handledException = handledException,
                numberOfImages = numberOfImages,
                lazyListState = detailsLazyListState,
                isReleaseEventsCollapsed = isReleaseEventsCollapsed,
                isExternalLinksCollapsed = isExternalLinksCollapsed,
                isAliasesCollapsed = isAliasesCollapsed,
                totalUrls = detailsModel?.urls?.size ?: 0,
                totalAliases = detailsModel?.aliases?.size ?: 0,
            ),
            allEntitiesListUiState = entitiesListUiState,
            musicBrainzLoginUiState = loginUiState,
            eventSink = ::eventSink,
        )
    }
}

internal data class DetailsUiState<DetailsModel : MusicBrainzDetailsModel>(
    val subtitle: String = "",
    val tabs: ImmutableList<Tab>,
    val selectedTab: Tab,
    val url: String = "",
    val detailsModel: DetailsModel?,
    val collected: Boolean = false,
    val snackbarMessage: String? = null,
    val topAppBarFilterState: TopAppBarFilterState = TopAppBarFilterState(),
    val selectionState: SelectionState = SelectionState(),
    val detailsTabUiState: DetailsTabUiState = DetailsTabUiState(),
    val allEntitiesListUiState: AllEntitiesListUiState = AllEntitiesListUiState(),
    val musicBrainzLoginUiState: MusicBrainzLoginUiState = MusicBrainzLoginUiState(),
    val eventSink: (DetailsUiEvent) -> Unit = {},
) : CircuitUiState

internal data class DetailsTabUiState(
    val isLoading: Boolean = false,
    val handledException: HandledException? = null,
    val numberOfImages: Int? = null,
    val lazyListState: LazyListState = LazyListState(),
    val isReleaseEventsCollapsed: Boolean = false,
    val isExternalLinksCollapsed: Boolean = false,
    val isAliasesCollapsed: Boolean = false,
    val now: Instant = Clock.System.now(),
    val totalUrls: Int = 0,
    val totalAliases: Int = 0,
)

internal sealed interface DetailsUiEvent : CircuitUiEvent {
    data object NavigateUp : DetailsUiEvent

    data object ForceRefreshDetails : DetailsUiEvent

    data class UpdateTab(val tab: Tab) : DetailsUiEvent

    data class ToggleSelectItem(
        val collectableId: String,
        val loadedCount: Int,
    ) : DetailsUiEvent

    data class ToggleSelectAllItems(
        val collectableIds: List<String>,
    ) : DetailsUiEvent

    data class ClickItem(
        val entity: MusicBrainzEntityType,
        val id: String,
    ) : DetailsUiEvent

    data object ClickImage : DetailsUiEvent

    data object NavigateToCollaboratorsGraph : DetailsUiEvent

    data object ToggleCollapseExpandExternalLinks : DetailsUiEvent

    data object ToggleCollapseExpandReleaseEvents : DetailsUiEvent

    data object ToggleCollapseExpandAliases : DetailsUiEvent
}
