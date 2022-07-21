package ly.david.mbjc.ui.release

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import ly.david.mbjc.data.Release
import ly.david.mbjc.data.domain.ListSeparator
import ly.david.mbjc.data.domain.TrackUiModel
import ly.david.mbjc.data.domain.UiModel
import ly.david.mbjc.data.getNameWithDisambiguation
import ly.david.mbjc.ui.common.ListSeparatorHeader
import ly.david.mbjc.ui.common.paging.PagingLoadingAndErrorHandler
import ly.david.mbjc.ui.common.rememberFlowWithLifecycleStarted

/**
 * Main screen for Release lookup. Shows all tracks in all media in this release.
 *
 * Tracks are recordings that are part of a release. It includes reference to recording,
 * but some of its details might be different for a given release.
 */
@Composable
internal fun TracksInReleaseScreen(
    modifier: Modifier = Modifier,
    releaseId: String,
    onTitleUpdate: (title: String, subtitle: String) -> Unit = { _, _ -> },
    onRecordingClick: (String) -> Unit = {},
    viewModel: TracksInReleaseViewModel = hiltViewModel()
) {

    var lookupInProgress by rememberSaveable { mutableStateOf(true) }

    LaunchedEffect(key1 = releaseId) {
        try {
            // TODO: if the lookup is really long, then we won't display title until it's done
            //  technically, we already know title if we can from release group screen.
            //  we could fetch title from db if it exists, or fetch it from network without rest of data
            //  worst case means we need to make 2 api calls.

            // TODO: check if release is in db, if so, display title from it
            //  if not, query for just title

            val release: Release = viewModel.lookupRelease(releaseId)
            onTitleUpdate(
                release.getNameWithDisambiguation(),
                "Release by [TODO]" // TODO: artistCredits
            )
        } catch (ex: Exception) {
            onTitleUpdate("[Release lookup failed]", "[error]")
        }
        lookupInProgress = false
    }

    val lazyPagingItems: LazyPagingItems<UiModel> = rememberFlowWithLifecycleStarted(viewModel.pagedTracks)
        .collectAsLazyPagingItems()

    // TODO: this will never show error, cause error would be from the above try-catch
    //  this paging source is local only
    PagingLoadingAndErrorHandler(
        modifier = modifier,
        lazyPagingItems = lazyPagingItems,
        somethingElseLoading = lookupInProgress
    ) { uiModel: UiModel? ->
        when (uiModel) {
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

    // TODO: don't do this, it causes loading spinner to stall right before data is loaded
//    val uiState by produceState(initialValue = UiState(isLoading = true)) {
//        value = UiState(response = viewModel.lookupRelease(releaseId))
//    }
//
//    var shouldShowTrackArtists by rememberSaveable { mutableStateOf(false) }
//
//    when {
//        uiState.response != null -> {
//            uiState.response?.let { release ->
//
//                onTitleUpdate(
//                    release.getNameWithDisambiguation(),
//                    "Release by ${release.artistCredits.getDisplayNames()}"
//                )
//
//                LazyColumn(
//                    modifier = modifier
//                ) {
//                    release.media?.forEach { medium ->
//                        if (medium.tracks == null) return@forEach
//
//                        stickyHeader {
//                            ListSeparatorHeader(
//                                text = "${medium.format.orEmpty()} ${medium.position}" +
//                                    medium.title.transformThisIfNotNullOrEmpty { " ($it)" }
//                            )
//                        }
//
//                        items(medium.tracks) { track ->
//                            // Only show tracks' artists if there are any tracks in this release
//                            // with artists different from the release's artists
//                            if (track.artistCredits != release.artistCredits) {
//                                shouldShowTrackArtists = true
//                            }
//                            TrackCard(
//                                track = track,
//                                showTrackArtists = shouldShowTrackArtists,
//                                onRecordingClick = onRecordingClick
//                            )
//                        }
//                    }
//
//                }
//            }
//
//        }
}
