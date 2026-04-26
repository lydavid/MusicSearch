package ly.david.musicsearch.ui.common.list

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.paging.PagingData
import com.slack.circuit.retained.collectAsRetainedState
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.collections.immutable.toPersistentSet
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.image.MusicBrainzImageMetadataRepository
import ly.david.musicsearch.shared.domain.list.GetEntities
import ly.david.musicsearch.shared.domain.list.ListFilters
import ly.david.musicsearch.shared.domain.list.ObserveLocalCount
import ly.david.musicsearch.shared.domain.listitem.ListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.domain.preferences.AppPreferences
import ly.david.musicsearch.shared.domain.release.ReleaseStatus
import ly.david.musicsearch.shared.domain.sort.AreaSortOption
import ly.david.musicsearch.shared.domain.sort.ArtistSortOption
import ly.david.musicsearch.shared.domain.sort.EventSortOption
import ly.david.musicsearch.shared.domain.sort.LabelSortOption
import ly.david.musicsearch.shared.domain.sort.PlaceSortOption
import ly.david.musicsearch.shared.domain.sort.RecordingSortOption
import ly.david.musicsearch.shared.domain.sort.ReleaseGroupSortOption
import ly.david.musicsearch.shared.domain.sort.ReleaseSortOption
import ly.david.musicsearch.shared.domain.sort.SortableOption
import ly.david.musicsearch.shared.domain.sort.WorkSortOption
import ly.david.musicsearch.ui.common.topappbar.BrowseMethodSaver

abstract class BaseListPresenter(
    private val getEntities: GetEntities,
    private val observeLocalCount: ObserveLocalCount,
    private val appPreferences: AppPreferences,
    private val musicBrainzImageMetadataRepository: MusicBrainzImageMetadataRepository,
) : Presenter<EntitiesListUiState> {

    abstract fun getEntityType(): MusicBrainzEntityType

    @Composable
    override fun present(): EntitiesListUiState {
        var query by rememberSaveable { mutableStateOf("") }
        var browseMethod: BrowseMethod? by rememberSaveable(saver = BrowseMethodSaver) { mutableStateOf(null) }
        var isRemote: Boolean by rememberSaveable { mutableStateOf(false) }

        val areaSortOption by appPreferences.areaSortOption.collectAsRetainedState(AreaSortOption.InsertedAscending)

        val artistSortOption
            by appPreferences.artistSortOption.collectAsRetainedState(ArtistSortOption.InsertedAscending)

        val eventSortOption
            by appPreferences.eventSortOption.collectAsRetainedState(EventSortOption.InsertedAscending)

        val labelSortOption
            by appPreferences.labelSortOption.collectAsRetainedState(LabelSortOption.InsertedAscending)

        val placeSortOption
            by appPreferences.placeSortOption.collectAsRetainedState(PlaceSortOption.InsertedAscending)

        val recordingSortOption
            by appPreferences.recordingSortOption.collectAsRetainedState(RecordingSortOption.InsertedAscending)

        val releaseSortOption
            by appPreferences.releaseSortOption.collectAsRetainedState(ReleaseSortOption.InsertedAscending)
        val showMoreInfoInReleaseListItem
            by appPreferences.showMoreInfoInReleaseListItem.collectAsRetainedState(true)
        val showReleaseStatuses by appPreferences.showReleaseStatuses.collectAsRetainedState(
            ReleaseStatus.entries,
        )

        val releaseGroupSortOption
            by appPreferences.releaseGroupSortOption.collectAsRetainedState(ReleaseGroupSortOption.InsertedAscending)

        val workSortOption by appPreferences.workSortOption.collectAsRetainedState(WorkSortOption.InsertedAscending)

        val listFilters = when (getEntityType()) {
            MusicBrainzEntityType.AREA -> ListFilters.Areas(
                query = query,
                isRemote = isRemote,
                sortOption = areaSortOption,
            )

            MusicBrainzEntityType.ARTIST -> ListFilters.Artists(
                query = query,
                isRemote = isRemote,
                sortOption = artistSortOption,
            )

            MusicBrainzEntityType.EVENT -> ListFilters.Events(
                query = query,
                isRemote = isRemote,
                sortOption = eventSortOption,
            )

            MusicBrainzEntityType.LABEL -> ListFilters.Labels(
                query = query,
                isRemote = isRemote,
                sortOption = labelSortOption,
            )

            MusicBrainzEntityType.PLACE -> ListFilters.Places(
                query = query,
                isRemote = isRemote,
                sortOption = placeSortOption,
            )

            MusicBrainzEntityType.RECORDING -> ListFilters.Recordings(
                query = query,
                isRemote = isRemote,
                sortOption = recordingSortOption,
            )

            MusicBrainzEntityType.RELEASE -> ListFilters.Releases(
                query = query,
                isRemote = isRemote,
                sortOption = releaseSortOption,
                showMoreInfo = showMoreInfoInReleaseListItem,
                showStatuses = showReleaseStatuses.toPersistentSet(),
            )

            MusicBrainzEntityType.RELEASE_GROUP -> ListFilters.ReleaseGroups(
                query = query,
                isRemote = isRemote,
                sortOption = releaseGroupSortOption,
            )

            MusicBrainzEntityType.WORK -> ListFilters.Works(
                query = query,
                isRemote = isRemote,
                sortOption = workSortOption,
            )

            else -> ListFilters.Base(
                query = query,
                isRemote = isRemote,
            )
        }
        val pagingDataFlow: Flow<PagingData<ListItemModel>> by rememberRetained(
            query,
            browseMethod,
            listFilters,
        ) {
            mutableStateOf(
                getEntities(
                    entity = getEntityType(),
                    browseMethod = browseMethod,
                    listFilters = listFilters,
                ),
            )
        }

        val totalCount by observeLocalCount(
            browseEntity = getEntityType(),
            browseMethod = browseMethod,
            query = "",
            showReleaseStatuses = ReleaseStatus.entries.toSet(),
        ).collectAsRetainedState(0)
        val filteredCount by observeLocalCount(
            browseEntity = getEntityType(),
            browseMethod = browseMethod,
            query = query,
            showReleaseStatuses = showReleaseStatuses.toSet(),
        ).collectAsRetainedState(0)

        val lazyListState = rememberLazyListState()
        val scope = rememberCoroutineScope()

        var requestedImageMetadataForIds: Set<String> by remember { mutableStateOf(setOf()) }

        fun eventSink(event: EntitiesListUiEvent) {
            when (event) {
                is EntitiesListUiEvent.Get -> {
                    browseMethod = event.browseMethod
                    isRemote = event.isRemote
                }

                is EntitiesListUiEvent.UpdateQuery -> {
                    query = event.query
                }

                is EntitiesListUiEvent.RequestForMissingCoverArtUrl -> {
                    val entityId = event.entityId
                    if (!requestedImageMetadataForIds.contains(entityId)) {
                        requestedImageMetadataForIds = requestedImageMetadataForIds + setOf(entityId)
                        scope.launch {
                            musicBrainzImageMetadataRepository.saveImageMetadata(
                                mbid = entityId,
                                entity = getEntityType(),
                                // pass filteredCount?
                                itemsCount = totalCount,
                            )
                        }
                    }
                }

                is EntitiesListUiEvent.UpdateSortOption -> {
                    when (val sortOption = event.sortOption) {
                        is AreaSortOption -> appPreferences.setAreaSortOption(sortOption)
                        is ArtistSortOption -> appPreferences.setArtistSortOption(sortOption)
                        is EventSortOption -> appPreferences.setEventSortOption(sortOption)
                        is LabelSortOption -> appPreferences.setLabelSortOption(sortOption)
                        is PlaceSortOption -> appPreferences.setPlaceSortOption(sortOption)
                        is RecordingSortOption -> appPreferences.setRecordingSortOption(sortOption)
                        is ReleaseGroupSortOption -> appPreferences.setReleaseGroupSortOption(sortOption)
                        is ReleaseSortOption -> appPreferences.setReleaseSortOption(sortOption)
                        is WorkSortOption -> appPreferences.setWorkSortOption(sortOption)
                    }
                }

                is EntitiesListUiEvent.UpdateShowMoreInfoInReleaseListItem -> {
                    appPreferences.setShowMoreInfoInReleaseListItem(event.showMore)
                }

                is EntitiesListUiEvent.UpdateShowReleaseStatus -> {
                    appPreferences.setShowReleaseStatus(event.status)
                }
            }
        }

        return EntitiesListUiState(
            pagingDataFlow = pagingDataFlow,
            totalCount = totalCount,
            filteredCount = filteredCount,
            lazyListState = lazyListState,
            listFilters = listFilters,
            eventSink = { event ->
                eventSink(event)
            },
        )
    }
}

sealed interface EntitiesListUiEvent : CircuitUiEvent {
    data class Get(
        val browseMethod: BrowseMethod,
        val isRemote: Boolean = true,
    ) : EntitiesListUiEvent

    data class UpdateQuery(
        val query: String,
    ) : EntitiesListUiEvent

    data class RequestForMissingCoverArtUrl(
        val entityId: String,
    ) : EntitiesListUiEvent

    data class UpdateSortOption(
        val sortOption: SortableOption,
    ) : EntitiesListUiEvent

    data class UpdateShowMoreInfoInReleaseListItem(
        val showMore: Boolean,
    ) : EntitiesListUiEvent

    data class UpdateShowReleaseStatus(
        val status: ReleaseStatus?,
    ) : EntitiesListUiEvent
}
