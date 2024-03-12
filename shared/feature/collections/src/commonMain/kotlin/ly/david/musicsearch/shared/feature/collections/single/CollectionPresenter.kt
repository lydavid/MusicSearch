package ly.david.musicsearch.shared.feature.collections.single

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.paging.insertSeparators
import app.cash.paging.PagingData
import app.cash.paging.compose.collectAsLazyPagingItems
import com.slack.circuit.foundation.NavEvent
import com.slack.circuit.foundation.onNavEvent
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ly.david.musicsearch.core.models.ActionableResult
import ly.david.musicsearch.core.models.ListFilters
import ly.david.musicsearch.core.models.history.LookupHistory
import ly.david.musicsearch.core.models.listitem.CollectionListItemModel
import ly.david.musicsearch.core.models.listitem.ListItemModel
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.core.preferences.AppPreferences
import ly.david.musicsearch.domain.area.usecase.GetAreasByEntity
import ly.david.musicsearch.domain.artist.usecase.GetArtistsByEntity
import ly.david.musicsearch.domain.collection.usecase.DeleteFromCollection
import ly.david.musicsearch.domain.collection.usecase.GetCollection
import ly.david.musicsearch.domain.event.usecase.GetEventsByEntity
import ly.david.musicsearch.domain.history.usecase.IncrementLookupHistory
import ly.david.musicsearch.domain.instrument.usecase.GetInstrumentsByEntity
import ly.david.musicsearch.domain.label.usecase.GetLabelsByEntity
import ly.david.musicsearch.domain.place.usecase.GetPlacesByEntity
import ly.david.musicsearch.domain.recording.usecase.GetRecordingsByEntity
import ly.david.musicsearch.domain.release.ReleaseImageRepository
import ly.david.musicsearch.domain.releasegroup.ReleaseGroupImageRepository
import ly.david.musicsearch.domain.releasegroup.usecase.GetReleaseGroupsByEntity
import ly.david.musicsearch.domain.series.usecase.GetSeriesByEntity
import ly.david.musicsearch.domain.work.usecase.GetWorksByEntity
import ly.david.ui.common.screen.CollectionScreen
import ly.david.ui.common.screen.DetailsScreen
import ly.david.ui.common.release.ReleasesByEntityPresenter
import ly.david.ui.common.release.ReleasesByEntityUiEvent

internal class CollectionPresenter(
    private val screen: CollectionScreen,
    private val navigator: Navigator,
    private val getCollectionUseCase: GetCollection,
    private val incrementLookupHistory: IncrementLookupHistory,
    private val appPreferences: AppPreferences,
    private val getAreasByEntity: GetAreasByEntity,
    private val getArtistsByEntity: GetArtistsByEntity,
    private val getEventsByEntity: GetEventsByEntity,
    private val getInstrumentsByEntity: GetInstrumentsByEntity,
    private val getLabelsByEntity: GetLabelsByEntity,
    private val getPlacesByEntity: GetPlacesByEntity,
    private val getRecordingsByEntity: GetRecordingsByEntity,
    private val releasesByEntityPresenter: ReleasesByEntityPresenter,
    private val getReleaseGroupsByEntity: GetReleaseGroupsByEntity,
    private val getSeriesByEntity: GetSeriesByEntity,
    private val getWorksByEntity: GetWorksByEntity,
    private val deleteFromCollection: DeleteFromCollection,
    private val releaseGroupImageRepository: ReleaseGroupImageRepository,
    private val releaseImageRepository: ReleaseImageRepository,
) : Presenter<CollectionUiState> {
    @Composable
    override fun present(): CollectionUiState {
        val collectionId = screen.id

        val scope = rememberCoroutineScope()
        var collection: CollectionListItemModel? by remember { mutableStateOf(null) }
        var actionableResult: ActionableResult? by remember { mutableStateOf(null) }
        var query by rememberSaveable { mutableStateOf("") }
        var recordedHistory by rememberSaveable { mutableStateOf(false) }
        var isRemote: Boolean by rememberSaveable { mutableStateOf(false) }
        val sortReleaseGroupListItems by appPreferences.sortReleaseGroupListItems.collectAsState(true)
        var collectableItems: Flow<PagingData<ListItemModel>> by remember { mutableStateOf(emptyFlow()) }
        val releasesByEntityUiState = releasesByEntityPresenter.present()
        val releasesByEntityEventSink = releasesByEntityUiState.eventSink

        LaunchedEffect(Unit) {
            val nonNullCollection = getCollectionUseCase(collectionId)
            collection = nonNullCollection
            isRemote = nonNullCollection.isRemote

            if (!recordedHistory) {
                incrementLookupHistory(
                    LookupHistory(
                        mbid = collectionId,
                        title = nonNullCollection.name,
                        entity = MusicBrainzEntity.COLLECTION,
                    ),
                )
                recordedHistory = true
            }
        }

        LaunchedEffect(
            key1 = query,
        ) {
            when (collection?.entity) {
                MusicBrainzEntity.AREA -> {
                    collectableItems = getAreasByEntity(
                        entityId = collectionId,
                        entity = MusicBrainzEntity.COLLECTION,
                        listFilters = ListFilters(
                            query = query,
                            isRemote = isRemote,
                        ),
                    ).map { pagingData ->
                        pagingData.insertSeparators { _, _ ->
                            null
                        }
                    }
                }

                MusicBrainzEntity.ARTIST -> {
                    collectableItems = getArtistsByEntity(
                        entityId = collectionId,
                        entity = MusicBrainzEntity.COLLECTION,
                        listFilters = ListFilters(
                            query = query,
                            isRemote = isRemote,
                        ),
                    ).map { pagingData ->
                        pagingData.insertSeparators { _, _ ->
                            null
                        }
                    }
                }

                MusicBrainzEntity.EVENT -> {
                    collectableItems = getEventsByEntity(
                        entityId = collectionId,
                        entity = MusicBrainzEntity.COLLECTION,
                        listFilters = ListFilters(
                            query = query,
                            isRemote = isRemote,
                        ),
                    ).map { pagingData ->
                        pagingData.insertSeparators { _, _ ->
                            null
                        }
                    }
                }

                MusicBrainzEntity.INSTRUMENT -> {
                    collectableItems = getInstrumentsByEntity(
                        entityId = collectionId,
                        entity = MusicBrainzEntity.COLLECTION,
                        listFilters = ListFilters(
                            query = query,
                            isRemote = isRemote,
                        ),
                    ).map { pagingData ->
                        pagingData.insertSeparators { _, _ ->
                            null
                        }
                    }
                }

                MusicBrainzEntity.LABEL -> {
                    collectableItems = getLabelsByEntity(
                        entityId = collectionId,
                        entity = MusicBrainzEntity.COLLECTION,
                        listFilters = ListFilters(
                            query = query,
                            isRemote = isRemote,
                        ),
                    ).map { pagingData ->
                        pagingData.insertSeparators { _, _ ->
                            null
                        }
                    }
                }

                MusicBrainzEntity.PLACE -> {
                    collectableItems = getPlacesByEntity(
                        entityId = collectionId,
                        entity = MusicBrainzEntity.COLLECTION,
                        listFilters = ListFilters(
                            query = query,
                            isRemote = isRemote,
                        ),
                    ).map { pagingData ->
                        pagingData.insertSeparators { _, _ ->
                            null
                        }
                    }
                }

                MusicBrainzEntity.RECORDING -> {
                    collectableItems = getRecordingsByEntity(
                        entityId = collectionId,
                        entity = MusicBrainzEntity.COLLECTION,
                        listFilters = ListFilters(
                            query = query,
                            isRemote = isRemote,
                        ),
                    ).map { pagingData ->
                        pagingData.insertSeparators { _, _ ->
                            null
                        }
                    }
                }

                MusicBrainzEntity.RELEASE -> {
                    releasesByEntityEventSink(
                        ReleasesByEntityUiEvent.GetReleases(
                            byEntityId = collectionId,
                            byEntity = MusicBrainzEntity.COLLECTION,
                        ),
                    )
                    releasesByEntityEventSink(ReleasesByEntityUiEvent.UpdateQuery(query))
                }

                MusicBrainzEntity.RELEASE_GROUP -> {
                    collectableItems = getReleaseGroupsByEntity(
                        entityId = collectionId,
                        entity = MusicBrainzEntity.COLLECTION,
                        listFilters = ListFilters(
                            query = query,
                            isRemote = isRemote,
                            sorted = sortReleaseGroupListItems,
                        ),
                    ).map { pagingData ->
                        pagingData.insertSeparators { _, _ ->
                            null
                        }
                    }
                }

                MusicBrainzEntity.SERIES -> {
                    collectableItems = getSeriesByEntity(
                        entityId = collectionId,
                        entity = MusicBrainzEntity.COLLECTION,
                        listFilters = ListFilters(
                            query = query,
                            isRemote = isRemote,
                        ),
                    ).map { pagingData ->
                        pagingData.insertSeparators { _, _ ->
                            null
                        }
                    }
                }

                MusicBrainzEntity.WORK -> {
                    collectableItems = getWorksByEntity(
                        entityId = collectionId,
                        entity = MusicBrainzEntity.COLLECTION,
                        listFilters = ListFilters(
                            query = query,
                            isRemote = isRemote,
                        ),
                    ).map { pagingData ->
                        pagingData.insertSeparators { _, _ ->
                            null
                        }
                    }
                }

                MusicBrainzEntity.COLLECTION,
                MusicBrainzEntity.GENRE,
                MusicBrainzEntity.URL,
                -> error("${collection?.entity} by collection not supported")

                null -> {
                    // no-op
                }
            }
        }

        fun eventSink(event: CollectionUiEvent) {
            when (event) {
                is CollectionUiEvent.NavigateUp -> {
                    navigator.pop()
                }

                is CollectionUiEvent.ClickItem -> {
                    navigator.onNavEvent(
                        NavEvent.GoTo(
                            DetailsScreen(
                                event.entity,
                                event.id,
                                event.title,
                            ),
                        ),
                    )
                }

                is CollectionUiEvent.MarkItemForDeletion -> {
                    scope.launch {
                        actionableResult = deleteFromCollection(
                            collectionId = collection?.id ?: return@launch,
                            entityId = event.collectableId,
                            entityName = event.name,
                        )
                    }
                }

                is CollectionUiEvent.UnMarkItemForDeletion -> {
                }

                is CollectionUiEvent.DeleteItem -> {
                }

                is CollectionUiEvent.UpdateQuery -> {
                    query = event.query
                }

                is CollectionUiEvent.UpdateSortReleaseGroupListItems -> {
                    appPreferences.setSortReleaseGroupListItems(event.sort)
                }

                // TODO: remove once we have ReleaseGroupsByEntityPresenter
                is CollectionUiEvent.RequestForMissingCoverArtUrl -> {
                    scope.launch {
                        when (event.entity) {
                            MusicBrainzEntity.RELEASE -> {
                                releaseImageRepository.getReleaseCoverArtUrlFromNetwork(
                                    releaseId = event.entityId,
                                    thumbnail = true,
                                )
                            }

                            MusicBrainzEntity.RELEASE_GROUP -> {
                                releaseGroupImageRepository.getReleaseGroupCoverArtUrlFromNetwork(
                                    releaseGroupId = event.entityId,
                                    thumbnail = true,
                                )
                            }

                            else -> {
                                // no-op
                            }
                        }
                    }
                }
            }
        }

        return CollectionUiState(
            collection = collection,
            actionableResult = actionableResult,
            query = query,
            sortReleaseGroupListItems = sortReleaseGroupListItems,
            lazyPagingItems = collectableItems.collectAsLazyPagingItems(),
            releasesByEntityUiState = releasesByEntityUiState,
            eventSink = ::eventSink,
        )
    }
}
