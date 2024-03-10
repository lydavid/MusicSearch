package ly.david.ui.commonlegacy.release

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.cash.paging.compose.LazyPagingItems
import ly.david.musicsearch.core.models.listitem.ReleaseListItemModel
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.ui.common.release.ReleasesListScreen
import org.koin.androidx.compose.koinViewModel
import timber.log.Timber

@Composable
fun ReleasesListScreenLegacy(
    lazyPagingItems: LazyPagingItems<ReleaseListItemModel>,
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState = SnackbarHostState(),
    lazyListState: LazyListState = rememberLazyListState(),
    showMoreInfo: Boolean = true,
    onReleaseClick: (entity: MusicBrainzEntity, String, String) -> Unit = { _, _, _ -> },
    onDeleteFromCollection: ((entityId: String, name: String) -> Unit)? = null,
    viewModel: ReleasesListViewModel = koinViewModel(),
) {
    ReleasesListScreen(
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

// @DefaultPreviews
// @Composable
// internal fun PreviewReleasesListScreen() {
//    PreviewTheme {
//        Surface {
//            val items = MutableStateFlow(
//                PagingData.from(
//                    ReleasePreviewParameterProvider().values.toList(),
//                ),
//            )
//
//            ReleasesListScreenInternal(lazyPagingItems = items.collectAsLazyPagingItems())
//        }
//    }
// }
