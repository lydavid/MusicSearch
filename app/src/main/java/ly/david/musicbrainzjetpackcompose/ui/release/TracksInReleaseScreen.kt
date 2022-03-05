package ly.david.musicbrainzjetpackcompose.ui.release

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ly.david.musicbrainzjetpackcompose.ui.common.toDisplayTime
import ly.david.musicbrainzjetpackcompose.ui.common.transformThisIfNotNullOrEmpty
import ly.david.musicbrainzjetpackcompose.data.Artist
import ly.david.musicbrainzjetpackcompose.data.ArtistCredit
import ly.david.musicbrainzjetpackcompose.data.Recording
import ly.david.musicbrainzjetpackcompose.data.Track
import ly.david.musicbrainzjetpackcompose.data.Work
import ly.david.musicbrainzjetpackcompose.data.getDisplayNames
import ly.david.musicbrainzjetpackcompose.ui.common.ClickableListItem
import ly.david.musicbrainzjetpackcompose.ui.common.FullScreenLoadingIndicator
import ly.david.musicbrainzjetpackcompose.ui.common.StickyHeader
import ly.david.musicbrainzjetpackcompose.ui.common.UiState
import ly.david.musicbrainzjetpackcompose.ui.theme.MusicBrainzJetpackComposeTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TracksInReleaseScreen(
    modifier: Modifier = Modifier,
    releaseId: String,
    onTitleUpdate: (title: String, subtitle: String) -> Unit = { _, _ -> },
    onRecordingClick: (String) -> Unit = {},
    viewModel: ReleaseViewModel = viewModel() // TODO: can we make this previewable by accepting an interface, and creating a mock viewmodel that just returns fake data with lookupRelease?
) {
    val uiState by produceState(initialValue = UiState(isLoading = true)) {
        value = UiState(response = viewModel.lookupRelease(releaseId))
    }

    var shouldShowTrackArtists by rememberSaveable { mutableStateOf(false) }

    when {
        uiState.response != null -> {
            uiState.response?.let { release ->

                onTitleUpdate(
                    release.title + release.disambiguation.transformThisIfNotNullOrEmpty { " ($it)" },
                    "Release by ${release.artistCredits.getDisplayNames()}"
                )

                LazyColumn(
                    modifier = modifier
                ) {
                    release.media?.forEach { medium ->
                        if (medium.tracks == null) return@forEach

                        stickyHeader {
                            StickyHeader(
                                text = "${medium.format.orEmpty()} ${medium.position}" +
                                    medium.title.transformThisIfNotNullOrEmpty { " ($it)" }
                            )
                        }

                        items(medium.tracks) { track ->
                            // Only show tracks' artists if there are any tracks in this release
                            // with artists different from the release's artists
                            if (track.artistCredits != release.artistCredits) {
                                shouldShowTrackArtists = true
                            }
                            TrackCard(
                                track = track,
                                showTrackArtists = shouldShowTrackArtists,
                                onRecordingClick = onRecordingClick
                            )
                        }
                    }

                }
            }

        }
        uiState.isLoading -> {
            FullScreenLoadingIndicator()
        }
        else -> {
            Text(text = "error...")
        }
    }
}

// TODO: Should have similar data to each table row in: https://musicbrainz.org/release/85363599-44b3-4eb2-b976-382a23d7f1ba

@Composable
private fun TrackCard(
    track: Track,
    showTrackArtists: Boolean = false,
    onRecordingClick: (String) -> Unit = {},
    onWorkClick: (Work) -> Unit = {},
    // no onTrackClick needed since Tracks exists in the context of a Release
) {

    ClickableListItem(
        onClick = { onRecordingClick(track.recording.id) },
    ) {
        Row(
            modifier = Modifier.padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = track.number,
                style = MaterialTheme.typography.body1,
            )
            Spacer(modifier = Modifier.padding(4.dp))

            Column(
                modifier = Modifier.weight(10f)
            ) {
                Text(
                    text = track.title,
                    style = MaterialTheme.typography.h6,

                    )
                if (showTrackArtists) {
                    Text(
                        modifier = Modifier.padding(top = 4.dp),
                        style = MaterialTheme.typography.body1,
                        text = track.artistCredits.getDisplayNames()
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = track.length.toDisplayTime(),
                style = MaterialTheme.typography.body2
            )
        }

        // TODO: more content in new screen that expands, but maybe not cover the entire screen
    }
}

private val testTrack = Track(
    id = "1",
    title = "Track title that is long and wraps",
    recording = Recording(
        id = "2",
        title = "Recording title",
        length = 256000,
        video = false
    ),
    position = 1,
    number = "123",
    length = 253000,
    artistCredits = listOf(
        ArtistCredit(
            Artist(
                "3",
                name = "actual name",
                sortName = "sort name"
            ),
            joinPhrase = "",
            name = "name on track"
        )
    )
)

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
internal fun ReleaseCardPreview() {
    MusicBrainzJetpackComposeTheme {
        Surface {
            TrackCard(
                track = testTrack,
                showTrackArtists = true
            )
        }
    }
}
