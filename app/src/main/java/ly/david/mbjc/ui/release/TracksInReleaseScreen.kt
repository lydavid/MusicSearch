package ly.david.mbjc.ui.release

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import ly.david.mbjc.data.Release
import ly.david.mbjc.data.Work
import ly.david.mbjc.data.domain.ListSeparator
import ly.david.mbjc.data.domain.TrackUiModel
import ly.david.mbjc.data.domain.UiModel
import ly.david.mbjc.data.getNameWithDisambiguation
import ly.david.mbjc.ui.common.ClickableListItem
import ly.david.mbjc.ui.common.ListSeparatorHeader
import ly.david.mbjc.ui.common.paging.PagingLoadingAndErrorHandler
import ly.david.mbjc.ui.common.toDisplayTime
import ly.david.mbjc.ui.theme.PreviewTheme
import ly.david.mbjc.ui.theme.TextStyles

/**
 * Main screen for Release lookup. Shows all tracks in all media in this release.
 *
 * Tracks are recordings that are part of a release. It includes reference to recording,
 * but some of its details might be different for a given release.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun TracksInReleaseScreen(
    modifier: Modifier = Modifier,
    releaseId: String,
    onTitleUpdate: (title: String, subtitle: String) -> Unit = { _, _ -> },
    onRecordingClick: (String) -> Unit = {},
    viewModel: TracksInReleaseViewModel = hiltViewModel()
) {

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
//            if (release is MusicBrainzRelease) {
//                viewModel.insertMediaAndTracks(release)
//            }

            // TODO: if not paging, we would just populate the lazy column here
            //  with all media, and all tracks

        } catch (e: Exception) {
            onTitleUpdate("[Release lookup failed]", "[error]")
        }
    }

    val lazyPagingItems: LazyPagingItems<UiModel> = viewModel.pagedTracks.collectAsLazyPagingItems()

    // TODO: never see error, cause error would be from the above try-catch
    //  this paging source is local only

    // TODO: keeps flashing between "No results found" and loading indicator when loading big data
    //  doesn't happen on my physical device. Emulator issue?
    PagingLoadingAndErrorHandler(
        modifier = modifier,
        lazyPagingItems = lazyPagingItems,
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

// TODO: Should have similar data to each table row in: https://musicbrainz.org/release/85363599-44b3-4eb2-b976-382a23d7f1ba

@Composable
private fun TrackCard(
    track: TrackUiModel,
    showTrackArtists: Boolean = false,
    onRecordingClick: (String) -> Unit = {},
    onWorkClick: (Work) -> Unit = {},
    // no onTrackClick needed since Tracks exists in the context of a Release
) {

    // TODO: constraint
    ClickableListItem(
        onClick = {
//            onRecordingClick(musicBrainzTrack.recording.id)
        },
    ) {
        Row(
            modifier = Modifier.padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = track.number,
                modifier = Modifier.weight(1f),
                style = TextStyles.getCardBodyTextStyle(),
            )

            Column(
                modifier = Modifier
                    .weight(10f)
                    .padding(start = 4.dp)
            ) {
                Text(
                    text = track.title,
                    style = TextStyles.getCardTitleTextStyle(),

                    )
                if (showTrackArtists) {
                    Text(
                        modifier = Modifier.padding(top = 4.dp),
                        style = TextStyles.getCardBodyTextStyle(),
                        text = "TODO"//uiTrack.artistCredits.getDisplayNames()
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // TODO: constraint layout to keep start/end text on 1 line, wrapping only middle

            Text(
                text = track.length.toDisplayTime(),
                style = TextStyles.getCardBodyTextStyle()
            )
        }

        // TODO: more content in new screen that expands, but maybe not cover the entire screen
    }
}

private val testTrack = TrackUiModel(
    id = "1",
    title = "Track title that is long and wraps",
//    recording = Recording(
//        id = "2",
//        name = "Recording title",
//        length = 256000,
//        video = false
//    ),
    position = 1,
    number = "123",
    length = 25300000,
//    artistCredits = listOf(
//        MusicBrainzArtistCredit(
//            MusicBrainzArtist(
//                "3",
//                name = "actual name",
//                sortName = "sort name"
//            ),
//            joinPhrase = "",
//            name = "name on track"
//        )
//    )
)

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
internal fun TrackCardPreview() {
    PreviewTheme {
        Surface {
            TrackCard(
                track = testTrack,
                showTrackArtists = true
            )
        }
    }
}
