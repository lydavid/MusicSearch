package ly.david.mbjc.ui.release.tracks

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.imageLoader
import coil.request.ImageRequest
import coil.size.Scale
import coil.size.Size
import ly.david.data.common.useHttps
import ly.david.data.domain.Header
import ly.david.data.domain.ListSeparator
import ly.david.data.domain.TrackListItemModel
import ly.david.data.domain.ListItemModel
import ly.david.mbjc.ui.common.ListSeparatorHeader
import ly.david.mbjc.ui.common.paging.PagingLoadingAndErrorHandler

/**
 * Main screen for Release lookup. Shows all tracks in all media in this release.
 *
 * Tracks are recordings that are part of a release. It includes reference to recording,
 * but some of its details might be different for a given release.
 */
@Composable
internal fun TracksInReleaseScreen(
    modifier: Modifier = Modifier,
    coverArtUrl: String = "",
    snackbarHostState: SnackbarHostState = SnackbarHostState(),
    lazyListState: LazyListState = rememberLazyListState(),
    lazyPagingItems: LazyPagingItems<ListItemModel>,
    onRecordingClick: (String, String) -> Unit = { _, _ -> },
) {

    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(coverArtUrl.useHttps())
            .size(Size.ORIGINAL)
            .scale(Scale.FIT)
            .crossfade(true)
            .build(),
        imageLoader = LocalContext.current.imageLoader
    )

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    PagingLoadingAndErrorHandler(
        modifier = modifier,
        lazyListState = lazyListState,
        lazyPagingItems = lazyPagingItems,
        snackbarHostState = snackbarHostState
    ) { listItemModel: ListItemModel? ->
        when (listItemModel) {
            is Header -> {
                if (coverArtUrl.isNotEmpty()) {

                    // TODO: seems like this no longer takes up space before the image is loaded
                    //  I checkout'd the commit that supposedly fixed this but it also now no longer works
                    //  It's possible it was never fully fixed
                    //  Some of the examples I used didn't have enough tracks to actually hide the cover art
                    //  When viewing a release again from history, loading from cache seems to show the loading
                    //  indicator and have it take up space, if only briefly.
                    when (painter.state) {
                        is AsyncImagePainter.State.Loading, AsyncImagePainter.State.Empty -> {
                            Box(
                                modifier = Modifier
                                    .height(screenWidth)
                                    .fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                        is AsyncImagePainter.State.Success -> {
                            Image(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .semantics { testTag = "coverArtImage" },
                                painter = painter,
                                contentDescription = null,
                                contentScale = ContentScale.FillWidth,
                            )
                        }
                        is AsyncImagePainter.State.Error -> {
                            // TODO: handle error
                        }
                    }
                }
            }
            is TrackListItemModel -> {
                TrackListItem(
                    track = listItemModel,
//                            showTrackArtists = shouldShowTrackArtists,
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
