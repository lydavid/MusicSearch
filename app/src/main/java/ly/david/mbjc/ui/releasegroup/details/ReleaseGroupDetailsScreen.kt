package ly.david.mbjc.ui.releasegroup.details

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.imageLoader
import coil.request.ImageRequest
import coil.size.Scale
import coil.size.Size
import ly.david.data.common.useHttps
import ly.david.data.domain.ReleaseGroupListItemModel
import ly.david.data.getDisplayTypes
import ly.david.mbjc.R
import ly.david.mbjc.ui.common.TextWithHeadingRes
import ly.david.mbjc.ui.common.listitem.InformationListSeparatorHeader

@Composable
internal fun ReleaseGroupDetailsScreen(
    releaseGroup: ReleaseGroupListItemModel,
    coverArtUrl: String = "",
    lazyListState: LazyListState = rememberLazyListState(),
) {

    // TODO: can we put this inside lazycolumn, then generalize with release details
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

    LazyColumn(state = lazyListState) {

        item {
            if (coverArtUrl.isNotEmpty()) {
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
                        // TODO: handle error with retry
                        //  this case means there is an image but we failed to get it
                    }
                }
            }
        }

        item {
            releaseGroup.run {
                InformationListSeparatorHeader(R.string.release_group)
                TextWithHeadingRes(headingRes = R.string.type, text = getDisplayTypes())
            }
        }
    }
}
