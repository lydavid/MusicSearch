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
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.collections.immutable.ImmutableList
import ly.david.musicsearch.core.logging.Logger
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.details.MusicBrainzDetailsModel
import ly.david.musicsearch.shared.domain.error.HandledException
import ly.david.musicsearch.shared.domain.getNameWithDisambiguation
import ly.david.musicsearch.shared.domain.history.usecase.IncrementLookupHistory
import ly.david.musicsearch.shared.domain.image.ImageMetadataRepository
import ly.david.musicsearch.shared.domain.musicbrainz.usecase.GetMusicBrainzUrl
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.wikimedia.WikimediaRepository
import ly.david.musicsearch.ui.common.musicbrainz.LoginPresenter
import ly.david.musicsearch.ui.common.musicbrainz.LoginUiState
import ly.david.musicsearch.ui.common.relation.RelationsPresenter
import ly.david.musicsearch.ui.common.relation.RelationsUiEvent
import ly.david.musicsearch.ui.common.relation.RelationsUiState
import ly.david.musicsearch.ui.common.screen.ArtistCollaborationScreen
import ly.david.musicsearch.ui.common.screen.CoverArtsScreen
import ly.david.musicsearch.ui.common.screen.DetailsScreen
import ly.david.musicsearch.ui.common.screen.EntitiesListPresenter
import ly.david.musicsearch.ui.common.screen.EntitiesListUiEvent
import ly.david.musicsearch.ui.common.screen.EntitiesListUiState
import ly.david.musicsearch.ui.common.screen.RecordVisit
import ly.david.musicsearch.ui.common.topappbar.Tab
import ly.david.musicsearch.ui.common.topappbar.TopAppBarFilterState
import ly.david.musicsearch.ui.common.topappbar.rememberTopAppBarFilterState
import ly.david.musicsearch.ui.common.topappbar.toMusicBrainzEntity

internal abstract class DetailsPresenter<DetailsModel : MusicBrainzDetailsModel>(
    private val screen: DetailsScreen,
    private val navigator: Navigator,
    override val incrementLookupHistory: IncrementLookupHistory,
    private val entitiesListPresenter: EntitiesListPresenter,
    private val relationsPresenter: RelationsPresenter,
    private val imageMetadataRepository: ImageMetadataRepository,
    private val logger: Logger,
    private val loginPresenter: LoginPresenter,
    private val getMusicBrainzUrl: GetMusicBrainzUrl,
    private val wikimediaRepository: WikimediaRepository,
) : Presenter<DetailsUiState<DetailsModel>>, RecordVisit {

    abstract fun getTabs(): ImmutableList<Tab>

    abstract fun getSubtitle(detailsModel: DetailsModel): String

    abstract suspend fun lookupDetailsModel(
        id: String,
        forceRefresh: Boolean,
    ): DetailsModel

    @Suppress("CyclomaticComplexMethod")
    @Composable
    final override fun present(): DetailsUiState<DetailsModel> {
        var title by rememberSaveable { mutableStateOf(screen.title.orEmpty()) }
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
        var isExternalLinksCollapsed by rememberSaveable { mutableStateOf(false) }

        val entitiesListUiState = entitiesListPresenter.present()
        val entitiesListEventSink = entitiesListUiState.eventSink

        val relationsUiState = relationsPresenter.present()
        val relationsEventSink = relationsUiState.eventSink

        val loginUiState = loginPresenter.present()

        LaunchedEffect(forceRefreshDetails) {
            try {
                isLoading = true
                val releaseGroupDetailsModel = lookupDetailsModel(
                    screen.id,
                    forceRefreshDetails,
                )
                title = releaseGroupDetailsModel.getNameWithDisambiguation()
                subtitle = getSubtitle(releaseGroupDetailsModel)
                detailsModel = releaseGroupDetailsModel

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
            title = title,
            entity = screen.entity,
            searchHint = "",
        )

        LaunchedEffect(forceRefreshDetails, detailsModel) {
            val showImage = setOf(
                MusicBrainzEntity.ARTIST,
                MusicBrainzEntity.EVENT,
                MusicBrainzEntity.RELEASE,
                MusicBrainzEntity.RELEASE_GROUP,
            ).contains(screen.entity)
            if (!showImage) return@LaunchedEffect

            val imageMetadataWithCount = imageMetadataRepository.getAndSaveImageMetadata(
                mbid = detailsModel?.id ?: return@LaunchedEffect,
                entity = MusicBrainzEntity.RELEASE_GROUP,
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
            when (selectedTab) {
                Tab.DETAILS, Tab.STATS -> {
                    // no-op
                }

                // TODO: handle release tracks as well?

                Tab.RELATIONSHIPS -> {
                    relationsEventSink(
                        RelationsUiEvent.GetRelations(
                            byEntityId = screen.id,
                            byEntity = screen.entity,
                        ),
                    )
                    relationsEventSink(RelationsUiEvent.UpdateQuery(query))
                }

                else -> {
                    entitiesListEventSink(
                        EntitiesListUiEvent.Get(
                            entityTab = selectedTab.toMusicBrainzEntity(),
                            browseMethod = browseMethod,
                            query = query,
                            isRemote = true,
                        ),
                    )
                }
            }
        }

        fun eventSink(event: DetailsUiEvent) {
            when (event) {
                DetailsUiEvent.NavigateUp -> {
                    navigator.pop()
                }

                is DetailsUiEvent.UpdateTab -> {
                    selectedTab = event.tab
                }

                DetailsUiEvent.ForceRefreshDetails -> {
                    forceRefreshDetails = true
                }

                is DetailsUiEvent.ClickItem -> {
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
                                name = title,
                            ),
                        ),
                    )
                }

                DetailsUiEvent.ToggleCollapseExpandExternalLinks -> {
                    isExternalLinksCollapsed = !isExternalLinksCollapsed
                }
            }
        }

        return DetailsUiState(
            title = title,
            subtitle = subtitle,
            tabs = getTabs(),
            selectedTab = selectedTab,
            url = getMusicBrainzUrl(screen.entity, screen.id),
            detailsModel = detailsModel?.withUrls(
                urls = detailsModel?.urls.filterUrlRelations(query = query),
            ) as DetailsModel?,
            snackbarMessage = snackbarMessage,
            topAppBarFilterState = topAppBarFilterState,
            detailsTabUiState = DetailsTabUiState(
                isLoading = isLoading,
                handledException = handledException,
                numberOfImages = numberOfImages,
                lazyListState = detailsLazyListState,
                isExternalLinksCollapsed = isExternalLinksCollapsed,
            ),
            entitiesListUiState = entitiesListUiState,
            relationsUiState = relationsUiState,
            loginUiState = loginUiState,
            eventSink = ::eventSink,
        )
    }
}

internal class DetailsUiState<DetailsModel : MusicBrainzDetailsModel>(
    val title: String,
    val subtitle: String = "",
    val tabs: ImmutableList<Tab>,
    val selectedTab: Tab,
    val url: String = "",
    val detailsModel: DetailsModel?,
    val snackbarMessage: String? = null,
    val topAppBarFilterState: TopAppBarFilterState = TopAppBarFilterState(),
    val detailsTabUiState: DetailsTabUiState = DetailsTabUiState(),
    val entitiesListUiState: EntitiesListUiState = EntitiesListUiState(),
    val relationsUiState: RelationsUiState = RelationsUiState(),
    val loginUiState: LoginUiState = LoginUiState(),
    val eventSink: (DetailsUiEvent) -> Unit = {},
) : CircuitUiState

internal data class DetailsTabUiState(
    val isLoading: Boolean = false,
    val handledException: HandledException? = null,
    val numberOfImages: Int? = null,
    val lazyListState: LazyListState = LazyListState(),
    val isExternalLinksCollapsed: Boolean = false,
)

internal sealed interface DetailsUiEvent : CircuitUiEvent {
    data object NavigateUp : DetailsUiEvent

    data object ForceRefreshDetails : DetailsUiEvent

    data class UpdateTab(val tab: Tab) : DetailsUiEvent

    data class ClickItem(
        val entity: MusicBrainzEntity,
        val id: String,
        val title: String?,
    ) : DetailsUiEvent

    data object ClickImage : DetailsUiEvent

    data object NavigateToCollaboratorsGraph : DetailsUiEvent

    data object ToggleCollapseExpandExternalLinks : DetailsUiEvent
}
