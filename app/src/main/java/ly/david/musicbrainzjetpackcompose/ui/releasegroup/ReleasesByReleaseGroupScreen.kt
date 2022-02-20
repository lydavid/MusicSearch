package ly.david.musicbrainzjetpackcompose.ui.releasegroup

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ly.david.musicbrainzjetpackcompose.common.getYear
import ly.david.musicbrainzjetpackcompose.common.ifNotNullOrEmpty
import ly.david.musicbrainzjetpackcompose.common.transformThisIfNotNullOrEmpty
import ly.david.musicbrainzjetpackcompose.data.Release
import ly.david.musicbrainzjetpackcompose.ui.common.StickyHeader
import ly.david.musicbrainzjetpackcompose.ui.theme.MusicBrainzJetpackComposeTheme

// TODO: rename? will we need something like this for every api return type? Can generalize
private data class ReleasesByReleaseGroupUiState(
    val response: List<Release>? = null,
    val isLoading: Boolean = false,
    val isError: Boolean = false // TODO: deal with errors
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ReleasesByReleaseGroupScreen(
    modifier: Modifier,
    releaseGroupId: String,
    onTitleUpdate: (String) -> Unit = {},
    onReleaseClick: (String) -> Unit = {},
    viewModel: ReleaseGroupViewModel = viewModel()
) {
    val uiState by produceState(initialValue = ReleasesByReleaseGroupUiState(isLoading = true)) {
        value = ReleasesByReleaseGroupUiState(response = viewModel.getReleasesByReleaseGroup(releaseGroupId))
    }

    when {
        uiState.response != null -> {
            uiState.response?.let { response ->

                onTitleUpdate("Release Group: ${response.first().title}")

                LazyColumn(
                    modifier = modifier
                ) {
                    val grouped = response.groupBy { it.status ?: "(No status)" }
                    grouped.forEach { (status, releasesWithStatus) ->
                        stickyHeader {
                            StickyHeader(text = status)
                        }
                        items(releasesWithStatus) { release ->
                            // TODO: sort by date ascending
                            ReleaseCard(release = release) {
                                onReleaseClick(it.id)
                            }
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

// TODO: card only needs: title/disamb/format/tracks/country/date
//  rest of details in go inside the release itself
//  inside release, we will have a list of tracks as default screen
//  but we should also have a tab for rest of details for that release such as barcode
@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun ReleaseCard(
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
                text = release.title +
                    release.disambiguation.transformThisIfNotNullOrEmpty {
                        "\n($it)"
                    },
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

//    {
//        "barcode": "4988002911981",
//        "status": "Official",
//        "cover-art-archive": {
//        "darkened": false,
//        "artwork": false,
//        "back": false,
//        "front": false,
//        "count": 0
//    },
//        "date": "2021-09-08",
//        "country": "JP",
//        "release-events": [
//        {
//            "date": "2021-09-08",
//            "area": {
//            "id": "2db42837-c832-3c27-b4a3-08198f75693c",
//            "iso-3166-1-codes": [
//            "JP"
//            ],
//            "type": null,
//            "type-id": null,
//            "sort-name": "Japan",
//            "name": "Japan",
//            "disambiguation": ""
//        }
//        }
//        ],
//        "status-id": "4e304316-386d-3409-af2e-78857eec5cfe",
//        "packaging": null,
//        "asin": "B098VJNDLC",
//        "text-representation": {
//        "script": "Jpan",
//        "language": "jpn"
//    },
//        "quality": "normal",
//        "packaging-id": null
//    },

private val testRelease = Release(
    id = "f171e0ae-bea8-41e6-bb41-4c7af7977f50",
    title = "欠けた心象、世のよすが",
    disambiguation = "初回限定盤",
    date = "2021-09-08",
    country = "JP"
)

@Preview(showBackground = true)
@Composable
internal fun ReleaseCardPreview() {
    MusicBrainzJetpackComposeTheme {
        ReleaseCard(testRelease)
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
internal fun ReleaseCardDarkPreview() {
    MusicBrainzJetpackComposeTheme {
        ReleaseCard(testRelease)
    }
}

private val testRelease2 = Release(
    id = "f171e0ae-bea8-41e6-bb41-4c7af7977f50",
    title = "欠けた心象、世のよすが",
    disambiguation = "初回限定盤",
    country = "JP"
)

@Preview(showBackground = true)
@Composable
internal fun ReleaseCardWithoutYearPreview() {
    MusicBrainzJetpackComposeTheme {
        ReleaseCard(testRelease2)
    }
}
