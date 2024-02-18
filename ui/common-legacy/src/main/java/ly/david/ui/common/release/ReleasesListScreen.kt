package ly.david.ui.common.release

import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.cash.paging.PagingData
import app.cash.paging.compose.LazyPagingItems
import app.cash.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.flow.MutableStateFlow
import ly.david.musicsearch.core.models.getNameWithDisambiguation
import ly.david.musicsearch.core.models.listitem.ReleaseListItemModel
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.ui.common.listitem.SwipeToDeleteListItem
import ly.david.ui.core.preview.DefaultPreviews
import ly.david.ui.core.theme.PreviewTheme
import org.koin.androidx.compose.koinViewModel
import timber.log.Timber

@Composable
fun ReleasesListScreen(
    lazyPagingItems: LazyPagingItems<ReleaseListItemModel>,
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState = SnackbarHostState(),
    lazyListState: LazyListState = rememberLazyListState(),
    showMoreInfo: Boolean = true,
    onReleaseClick: (entity: MusicBrainzEntity, String, String) -> Unit = { _, _, _ -> },
    onDeleteFromCollection: ((entityId: String, name: String) -> Unit)? = null,
    viewModel: ReleasesListViewModel = koinViewModel(),
) {
    ReleasesListScreenInternal(
        lazyPagingItems = lazyPagingItems,
        modifier = modifier,
        snackbarHostState = snackbarHostState,
        lazyListState = lazyListState,
        showMoreInfo = showMoreInfo,
        onReleaseClick = onReleaseClick,
        onDeleteFromCollection = onDeleteFromCollection,
        requestForMissingCoverArtUrl = {
            try {
                viewModel.getReleaseCoverArtUrlFromNetwork(
                    releaseId = it,
                )
            } catch (ex: Exception) {
                Timber.e(ex)
            }
        },
    )
}

@VisibleForTesting
@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun ReleasesListScreenInternal(
    lazyPagingItems: LazyPagingItems<ReleaseListItemModel>,
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState = SnackbarHostState(),
    lazyListState: LazyListState = rememberLazyListState(),
    showMoreInfo: Boolean = true,
    onReleaseClick: (entity: MusicBrainzEntity, String, String) -> Unit = { _, _, _ -> },
    onDeleteFromCollection: ((entityId: String, name: String) -> Unit)? = null,
    requestForMissingCoverArtUrl: suspend (id: String) -> Unit = {},
) {
    ly.david.ui.common.paging.ScreenWithPagingLoadingAndError(
        modifier = modifier,
        lazyListState = lazyListState,
        lazyPagingItems = lazyPagingItems,
        snackbarHostState = snackbarHostState,
    ) { releaseListItemModel: ReleaseListItemModel? ->
        when (releaseListItemModel) {
            is ReleaseListItemModel -> {
                SwipeToDeleteListItem(
                    content = {
                        ReleaseListItem(
                            release = releaseListItemModel,
                            modifier = Modifier.animateItemPlacement(),
                            showMoreInfo = showMoreInfo,
                            requestForMissingCoverArtUrl = {
                                requestForMissingCoverArtUrl(releaseListItemModel.id)
                            },
                        ) {
                            onReleaseClick(
                                MusicBrainzEntity.RELEASE,
                                id,
                                getNameWithDisambiguation(),
                            )
                        }
                    },
                    disable = onDeleteFromCollection == null,
                    onDelete = {
                        onDeleteFromCollection?.invoke(
                            releaseListItemModel.id,
                            releaseListItemModel.name
                        )
                    },
                )
            }

            else -> {
                // Do nothing.
            }
        }
    }
}

@DefaultPreviews
@Composable
internal fun PreviewReleasesListScreen() {
    PreviewTheme {
        Surface {
            val items = MutableStateFlow(
                PagingData.from(
                    ReleasePreviewParameterProvider().values.toList(),
                ),
            )

            ReleasesListScreenInternal(lazyPagingItems = items.collectAsLazyPagingItems())
        }
    }
}
