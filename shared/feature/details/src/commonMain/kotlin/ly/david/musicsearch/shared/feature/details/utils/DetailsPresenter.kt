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
import ly.david.musicsearch.shared.feature.details.releasegroup.ReleaseGroupUiEvent
import ly.david.musicsearch.ui.common.musicbrainz.LoginPresenter
import ly.david.musicsearch.ui.common.musicbrainz.LoginUiState
import ly.david.musicsearch.ui.common.relation.RelationsPresenter
import ly.david.musicsearch.ui.common.relation.RelationsUiEvent
import ly.david.musicsearch.ui.common.relation.RelationsUiState
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
            forceRefreshDetails = false
        }

        RecordVisit(
            mbid = screen.id,
            title = title,
            entity = screen.entity,
            searchHint = "",
        )

        LaunchedEffect(forceRefreshDetails, detailsModel) {
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

        return DetailsUiState(
            title = title,
            subtitle = subtitle,
            tabs = getTabs(),
            selectedTab = selectedTab,
            topAppBarFilterState = topAppBarFilterState,
            url = getMusicBrainzUrl(screen.entity, screen.id),
            detailsModel = detailsModel?.withUrls(
                urls = detailsModel?.urls.filterUrlRelations(query = query),
            ) as DetailsModel?,
            snackbarMessage = snackbarMessage,
            detailsTabUiState = DetailsTabUiState(
                handledException = handledException,
                lazyListState = detailsLazyListState,
                numberOfImages = numberOfImages,
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
    val subtitle: String,
    val tabs: ImmutableList<Tab>,
    val selectedTab: Tab,
    val detailsModel: DetailsModel?,
    val detailsTabUiState: DetailsTabUiState = DetailsTabUiState(),
    val url: String,
    val topAppBarFilterState: TopAppBarFilterState,
    val snackbarMessage: String?,
    val entitiesListUiState: EntitiesListUiState,
    val relationsUiState: RelationsUiState,
    val loginUiState: LoginUiState,
    val eventSink: (ReleaseGroupUiEvent) -> Unit,
) : CircuitUiState

internal data class DetailsTabUiState(
    val handledException: HandledException? = null,
    val numberOfImages: Int? = null,
    val lazyListState: LazyListState = LazyListState(),
    val isExternalLinksCollapsed: Boolean = false,
)
