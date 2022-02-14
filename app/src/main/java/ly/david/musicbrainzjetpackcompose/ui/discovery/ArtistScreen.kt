package ly.david.musicbrainzjetpackcompose.ui.discovery

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ly.david.musicbrainzjetpackcompose.musicbrainz.ReleaseGroup
import ly.david.musicbrainzjetpackcompose.musicbrainz.ReleaseGroups
import ly.david.musicbrainzjetpackcompose.ui.theme.MusicBrainzJetpackComposeTheme

// TODO: should have tabs for Overview (release groups),  releases, recordings, ...
@Composable
fun ArtistScreen(
    artistId: String,
//    onReleaseGroupClick: (String) -> Unit = {},
//    viewModel: ArtistViewModel = viewModel()
) {

    ArtistReleaseGroupsScreen(artistId = artistId)

}

// TODO: rename? will we need something like this for every api return type? Can generalize
private data class ArtistUiState(
    val releaseGroups: ReleaseGroups? = null,
    val isLoading: Boolean = false,
    val isError: Boolean = false
)

@Composable
fun ArtistReleaseGroupsScreen(
    artistId: String,
    onReleaseGroupClick: (String) -> Unit = {},
    viewModel: ArtistViewModel = viewModel()
) {
    val uiState by produceState(initialValue = ArtistUiState(isLoading = true)) {
        value = ArtistUiState(viewModel.getReleaseGroupsByArtist(artistId))
    }

    when {
        uiState.releaseGroups != null -> {
            uiState.releaseGroups?.let { releaseGroups ->
                LazyColumn {
                    item {
                        val results = releaseGroups.releaseGroupCount
                        if (results == 0) {
                            Text("No release groups found for this artist.")
                        } else {
                            Text("Found $results release groups for this artist.")
                        }
                    }

                    items(releaseGroups.releaseGroups) { releaseGroup ->
                        ReleaseGroupCard(releaseGroup = releaseGroup)
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun ReleaseGroupCard(
    releaseGroup: ReleaseGroup,
    onClick: (id: String) -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        onClick = { onClick(releaseGroup.id) },
        border = BorderStroke(1.dp, Color.LightGray)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
        ) {

            Text(
                text = releaseGroup.title,
                modifier = Modifier.fillMaxWidth()
            )

//            if (artist.disambiguation != null) {
//                Spacer(modifier = Modifier.padding(top = 4.dp))
//                Text(
//                    text = "(${artist.disambiguation})",
//                    color = Color.Gray,
//                    modifier = Modifier.fillMaxWidth()
//                )
//            }
        }
    }
}

//    {
//        "primary-type-id": "6d0c5bf6-7a33-3420-a519-44fc63eedebf",
//        "primary-type": "EP",
//        "disambiguation": "",
//        "id": "81d75493-78b6-4a37-b5ae-2a3918ee3756",
//        "secondary-types": [],
//        "secondary-type-ids": [],
//        "title": "欠けた心象、世のよすが",
//        "first-release-date": "2021-09-08"
//    }
private val testReleaseGroup = ReleaseGroup(
    id = "6825ace2-3563-4ac5-8d85-c7bf1334bd2c",
    title = "欠けた心象、世のよすが",
    primaryType = "EP",
    primaryTypeId = "6d0c5bf6-7a33-3420-a519-44fc63eedebf",
    firstReleaseDate = "2021-09-08"
)

@Preview(showBackground = true)
@Composable
internal fun ReleaseGroupCardPreview() {
    MusicBrainzJetpackComposeTheme {
        ReleaseGroupCard(testReleaseGroup)
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
internal fun ReleaseGroupCardDarkPreview() {
    MusicBrainzJetpackComposeTheme {
        ReleaseGroupCard(testReleaseGroup)
    }
}
