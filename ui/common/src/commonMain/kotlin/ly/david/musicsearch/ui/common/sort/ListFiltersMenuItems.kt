package ly.david.musicsearch.ui.common.sort

import androidx.compose.runtime.Composable
import ly.david.musicsearch.shared.domain.list.ListFilters
import ly.david.musicsearch.shared.domain.sort.AreaSortOption
import ly.david.musicsearch.shared.domain.sort.ArtistSortOption
import ly.david.musicsearch.shared.domain.sort.EventSortOption
import ly.david.musicsearch.shared.domain.sort.RecordingSortOption
import ly.david.musicsearch.shared.domain.sort.ReleaseGroupSortOption
import ly.david.musicsearch.shared.domain.sort.ReleaseSortOption
import ly.david.musicsearch.shared.domain.sort.WorkSortOption
import ly.david.musicsearch.ui.common.list.EntitiesListUiEvent
import ly.david.musicsearch.ui.common.release.ShowStatusesMenuItem
import ly.david.musicsearch.ui.common.topappbar.MoreInfoToggleMenuItem
import ly.david.musicsearch.ui.common.topappbar.OverflowMenuScope

@Composable
fun OverflowMenuScope.ListFiltersMenuItems(
    listFilters: ListFilters,
    eventSink: (EntitiesListUiEvent) -> Unit,
) {
    when (listFilters) {
        is ListFilters.Base -> {
            // nothing
        }

        is ListFilters.Areas -> {
            SortMenuItem(
                sortOptions = AreaSortOption.entries,
                selectedSortOption = listFilters.sortOption,
                onSortOptionClick = {
                    eventSink(
                        EntitiesListUiEvent.UpdateSortOption(it),
                    )
                },
            )
        }

        is ListFilters.Artists -> {
            SortMenuItem(
                sortOptions = ArtistSortOption.entries,
                selectedSortOption = listFilters.sortOption,
                onSortOptionClick = {
                    // TODO: consider moving to AllEntitiesListUiEvent
                    eventSink(
                        EntitiesListUiEvent.UpdateSortOption(it),
                    )
                },
            )
        }

        is ListFilters.Events -> {
            SortMenuItem(
                sortOptions = EventSortOption.entries,
                selectedSortOption = listFilters.sortOption,
                onSortOptionClick = {
                    eventSink(
                        EntitiesListUiEvent.UpdateSortOption(it),
                    )
                },
            )
        }

        is ListFilters.Recordings -> {
            SortMenuItem(
                sortOptions = RecordingSortOption.entries,
                selectedSortOption = listFilters.sortOption,
                onSortOptionClick = {
                    eventSink(
                        EntitiesListUiEvent.UpdateSortOption(it),
                    )
                },
            )
        }

        is ListFilters.Releases -> {
            ShowStatusesMenuItem(
                selectedStatuses = listFilters.showStatuses,
                onClick = {
                    eventSink(
                        EntitiesListUiEvent.UpdateShowReleaseStatus(it),
                    )
                },
            )
            SortMenuItem(
                sortOptions = ReleaseSortOption.entries,
                selectedSortOption = listFilters.sortOption,
                onSortOptionClick = {
                    eventSink(
                        EntitiesListUiEvent.UpdateSortOption(it),
                    )
                },
            )
            MoreInfoToggleMenuItem(
                showMoreInfo = listFilters.showMoreInfo,
                onToggle = {
                    eventSink(
                        EntitiesListUiEvent.UpdateShowMoreInfoInReleaseListItem(it),
                    )
                },
            )
        }

        is ListFilters.ReleaseGroups -> {
            SortMenuItem(
                sortOptions = ReleaseGroupSortOption.entries,
                selectedSortOption = listFilters.sortOption,
                onSortOptionClick = {
                    eventSink(
                        EntitiesListUiEvent.UpdateSortOption(it),
                    )
                },
            )
        }

        is ListFilters.Works -> {
            SortMenuItem(
                sortOptions = WorkSortOption.entries,
                selectedSortOption = listFilters.sortOption,
                onSortOptionClick = {
                    eventSink(
                        EntitiesListUiEvent.UpdateSortOption(it),
                    )
                },
            )
        }
    }
}
