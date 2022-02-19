package ly.david.musicbrainzjetpackcompose.ui.release

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ly.david.musicbrainzjetpackcompose.common.getYear
import ly.david.musicbrainzjetpackcompose.common.ifNotNullOrEmpty
import ly.david.musicbrainzjetpackcompose.data.Recording
import ly.david.musicbrainzjetpackcompose.data.Release

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

//                LazyColumn(
//                    modifier = modifier
//                ) {
//                    val grouped = response.groupBy { it.status ?: "(No status)" }
//                    grouped.forEach { (status, releasesForStatus) ->
//                        stickyHeader {
//                            StickyHeader(text = status)
//                        }
//                        items(releasesForStatus) { release ->
//                            // TODO: sort by date ascending
//                            ReleaseCard(release = release) {
//                                onReleaseClick(it)
//                            }
//                        }
//                    }
//
//                }
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
    release: Release,
    onClick: (Release) -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        onClick = { onClick(release) },
        border = BorderStroke(1.dp, Color.LightGray)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = release.title,
                style = MaterialTheme.typography.h6,
                modifier = Modifier.weight(7f)
            )
            release.date?.getYear().ifNotNullOrEmpty {
                Spacer(modifier = Modifier.padding(4.dp))
                Text(
                    text = it,
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.End
                )
            }
        }
    }
}

//private val testRelease = Release(
//    id = "f171e0ae-bea8-41e6-bb41-4c7af7977f50",
//    title = "欠けた心象、世のよすが",
//    disambiguation = "初回限定盤",
//    date = "2021-09-08",
//    country = "JP"
//)
//
//@Preview(showBackground = true)
//@Composable
//internal fun ReleaseCardPreview() {
//    MusicBrainzJetpackComposeTheme {
//        ReleaseCard(testRelease)
//    }
//}
//
//@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
//@Composable
//internal fun ReleaseCardDarkPreview() {
//    MusicBrainzJetpackComposeTheme {
//        ReleaseCard(testRelease)
//    }
//}
//
//private val testRelease2 = Release(
//    id = "f171e0ae-bea8-41e6-bb41-4c7af7977f50",
//    title = "欠けた心象、世のよすが",
//    disambiguation = "初回限定盤",
//    country = "JP"
//)
//
//@Preview(showBackground = true)
//@Composable
//internal fun ReleaseCardWithoutYearPreview() {
//    MusicBrainzJetpackComposeTheme {
//        ReleaseCard(testRelease2)
//    }
//}
