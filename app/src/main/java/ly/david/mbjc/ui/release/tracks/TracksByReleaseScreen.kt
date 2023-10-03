package ly.david.mbjc.ui.release.tracks

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import ly.david.musicsearch.domain.listitem.ListItemModel
import ly.david.musicsearch.domain.listitem.ListSeparator
import ly.david.musicsearch.domain.listitem.TrackListItemModel
import ly.david.ui.common.listitem.ListSeparatorHeader
import ly.david.ui.common.paging.PagingLoadingAndErrorHandler
import ly.david.ui.common.release.TrackListItem
import ly.david.ui.core.preview.DefaultPreviews
import ly.david.ui.core.theme.PreviewTheme
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun TracksByReleaseScreen(
    releaseId: String,
    filterText: String,
    lazyPagingItems: LazyPagingItems<ListItemModel>,
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState = SnackbarHostState(),
    lazyListState: LazyListState = rememberLazyListState(),
    onRecordingClick: (String, String) -> Unit = { _, _ -> },
    onPagedTracksFlowChange: (Flow<PagingData<ListItemModel>>) -> Unit = {},
    viewModel: TracksByReleaseViewModel = koinViewModel(),
) {
    LaunchedEffect(key1 = Unit) {
        viewModel.loadTracks(releaseId)
        onPagedTracksFlowChange(viewModel.pagedTracks)
    }
    viewModel.updateQuery(filterText)

    TracksByReleaseScreen(
        lazyPagingItems = lazyPagingItems,
        modifier = modifier,
        snackbarHostState = snackbarHostState,
        lazyListState = lazyListState,
        onRecordingClick = onRecordingClick,
    )
}

/**
 * Main screen for Release lookup. Shows all tracks in all media in this release.
 *
 * Tracks are recordings that are part of a release. It includes reference to recording,
 * but some of its details might be different for a given release.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun TracksByReleaseScreen(
    lazyPagingItems: LazyPagingItems<ListItemModel>,
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState = SnackbarHostState(),
    lazyListState: LazyListState = rememberLazyListState(),
    onRecordingClick: (String, String) -> Unit = { _, _ -> },
) {
    PagingLoadingAndErrorHandler(
        modifier = modifier,
        lazyListState = lazyListState,
        lazyPagingItems = lazyPagingItems,
        snackbarHostState = snackbarHostState
    ) { listItemModel: ListItemModel? ->
        when (listItemModel) {
            is TrackListItemModel -> {
                TrackListItem(
                    track = listItemModel,
                    modifier = Modifier.animateItemPlacement(),
                    onRecordingClick = onRecordingClick
                )
            }

            is ListSeparator -> {
                ListSeparatorHeader(text = listItemModel.text)
            }

            else -> {
                // Do nothing.
            }
        }
    }
}

// region Previews
@DefaultPreviews
@Composable
internal fun PreviewTracksInReleaseScreen() {
    PreviewTheme {
        Surface {
            val items = MutableStateFlow(
                PagingData.from(
                    listOf(
                        ListSeparator(
                            id = "separator1",
                            text = "7\" Vinyl 1",
                        ),
                        TrackListItemModel(
                            id = "1",
                            position = 1,
                            number = "A1",
                            title = "Track name",
                            length = 295000,
                        ),
                        ListSeparator(
                            id = "separator2",
                            text = "7\" Vinyl 2",
                        ),
                        TrackListItemModel(
                            id = "2",
                            position = 1,
                            number = "B1",
                            title = "Another track name",
                            length = 199000,
                        ),
                    )
                )
            )

            TracksByReleaseScreen(
                lazyPagingItems = items.collectAsLazyPagingItems()
            )
        }
    }
}
// endregion
