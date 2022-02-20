package ly.david.musicbrainzjetpackcompose.ui.release

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconToggleButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ly.david.musicbrainzjetpackcompose.common.toTimeString
import ly.david.musicbrainzjetpackcompose.common.transformThisIfNotNullOrEmpty
import ly.david.musicbrainzjetpackcompose.data.Recording
import ly.david.musicbrainzjetpackcompose.data.Release
import ly.david.musicbrainzjetpackcompose.data.Track
import ly.david.musicbrainzjetpackcompose.data.Work
import ly.david.musicbrainzjetpackcompose.ui.common.StickyHeader
import ly.david.musicbrainzjetpackcompose.ui.theme.MusicBrainzJetpackComposeTheme

// TODO: rename? will we need something like this for every api return type? Can generalize
private data class ReleaseUiState(
    val response: Release? = null,
    val isLoading: Boolean = false,
    val isError: Boolean = false // TODO: deal with errors
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TracksInReleaseScreen(
    modifier: Modifier,
    releaseId: String,
    onTitleUpdate: (String) -> Unit = {},
    onRecordingClick: (Recording) -> Unit = {},
    viewModel: ReleaseViewModel = viewModel()
) {
    val uiState by produceState(initialValue = ReleaseUiState(isLoading = true)) {
        value = ReleaseUiState(response = viewModel.lookupRelease(releaseId))
    }

    when {
        uiState.response != null -> {
            uiState.response?.let { response ->

                onTitleUpdate("Release: ${response.title}")

                LazyColumn(
                    modifier = modifier
                ) {
                    response.media?.forEach { medium ->
                        if (medium.tracks == null) return@forEach

                        stickyHeader {
                            StickyHeader(
                                text = "${medium.format.orEmpty()} ${medium.position}" +
                                    medium.title.transformThisIfNotNullOrEmpty { " (${it})" }
                            )
                        }

                        items(medium.tracks) { track ->
                            TrackCard(track = track, onRecordingClick = onRecordingClick)
                        }
                    }

                }
            }

        }
        uiState.isLoading -> {
            Text(text = "Loading...")
        }
        else -> {
            Text(text = "error...")
        }
    }
}

// TODO: Should have similar data to each table row in: https://musicbrainz.org/release/85363599-44b3-4eb2-b976-382a23d7f1ba

// TODO: Track includes Recording in it
@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun TrackCard(
    track: Track,
    onRecordingClick: (Recording) -> Unit = {},
    onWorkClick: (Work) -> Unit = {},
    // no onTrackClick needed since Tracks exists in the context of a Release
) {

    var expanded by rememberSaveable { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        onClick = {
            onRecordingClick(track.recording)
        },
        border = BorderStroke(1.dp, Color.LightGray)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = track.number,
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.padding(4.dp))

                Text(
                    text = track.title,
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier.weight(10f)
                )

                Spacer(modifier = Modifier.weight(1f))

                IconToggleButton(
                    checked = expanded,
                    onCheckedChange = {
                        expanded = it
                    }
                ) {
                    Icon(
                        if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = if (expanded) "Click to collapse" else "click to expand",
                    )
                }
            }

            Row {
                Text(
                    text = track.length.toTimeString(),
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier.weight(1f)
                )
            }

        }

        // TODO: if recording has artists not part of the release's artist, list them underneath with joinphrase

        // TODO: more content
    }
}

private val testTrack = Track(
    id = "1",
    title = "Track title",
    recording = Recording(
        id = "2",
        title = "Recording title",
        length = 256000,
        video = false
    ),
    position = 1,
    number = "A1",
    length = 253000
)

@Preview(showBackground = true)
@Composable
internal fun ReleaseCardPreview() {
    MusicBrainzJetpackComposeTheme {
        TrackCard(testTrack)
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
internal fun ReleaseCardDarkPreview() {
    MusicBrainzJetpackComposeTheme {
        TrackCard(testTrack)
    }
}
