package ly.david.mbjc.ui.release.tracks

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
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import coil.compose.SubcomposeAsyncImage
import ly.david.mbjc.data.domain.Header
import ly.david.mbjc.data.domain.ListSeparator
import ly.david.mbjc.data.domain.TrackUiModel
import ly.david.mbjc.data.domain.UiModel
import ly.david.mbjc.ui.common.ListSeparatorHeader
import ly.david.mbjc.ui.common.paging.PagingLoadingAndErrorHandler
import ly.david.mbjc.ui.common.useHttps

/**
 * Main screen for Release lookup. Shows all tracks in all media in this release.
 *
 * Tracks are recordings that are part of a release. It includes reference to recording,
 * but some of its details might be different for a given release.
 */
@Composable
internal fun TracksInReleaseScreen(
    modifier: Modifier = Modifier,
    url: String? = null,
    snackbarHostState: SnackbarHostState = SnackbarHostState(),
    lazyListState: LazyListState = rememberLazyListState(),
    lazyPagingItems: LazyPagingItems<UiModel>,
    onRecordingClick: (String, String) -> Unit = { _, _ -> },
) {
    PagingLoadingAndErrorHandler(
        modifier = modifier,
        lazyListState = lazyListState,
        lazyPagingItems = lazyPagingItems,
        snackbarHostState = snackbarHostState
    ) { uiModel: UiModel? ->
        when (uiModel) {
            is Header -> {
                if (!url.isNullOrEmpty()) {

                    // TODO: seems like the reason why it's so slow loading the loading indicator is because of our
                    //  structure. It's instant in our Experimental screen
                    //  One solution is to load it outside of here, and pass either the loading indicator or
                    //  painter here.
                    SubcomposeAsyncImage(
                        modifier = Modifier.fillMaxWidth(),
                        contentScale = ContentScale.FillWidth,
                        model = url.useHttps(),
                        contentDescription = "Cover art for [TODO]",
                        loading = {
                            Box(
                                modifier = Modifier
                                    .height(250.dp)
                                    .fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    )

//                    val painter: AsyncImagePainter = rememberAsyncImagePainter(
//                        model = ImageRequest.Builder(LocalContext.current)
//                            .data(url.useHttps())
//                            .size(Size.ORIGINAL)
//                            .crossfade(true)
//                            .build()
//                    )
//                    if (painter.state is AsyncImagePainter.State.Loading) {
//                        CircularProgressIndicator()
//                    } else {
//                        Image(
////                        modifier = Modifier.size(250.dp),
//                            painter = painter,
//                            contentDescription = null,
////                        contentScale = ContentScale.FillWidth,
//                        )
//
//                    }

                }
            }
            is TrackUiModel -> {
                TrackCard(
                    track = uiModel,
//                            showTrackArtists = shouldShowTrackArtists,
                    onRecordingClick = onRecordingClick
                )
            }
            is ListSeparator -> {
                ListSeparatorHeader(text = uiModel.text)
            }
            else -> {
                // Do nothing.
            }
        }
    }
}
