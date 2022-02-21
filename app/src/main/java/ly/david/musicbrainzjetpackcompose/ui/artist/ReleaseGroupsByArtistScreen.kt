package ly.david.musicbrainzjetpackcompose.ui.artist

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ly.david.musicbrainzjetpackcompose.common.getYear
import ly.david.musicbrainzjetpackcompose.common.toDate
import ly.david.musicbrainzjetpackcompose.data.ReleaseGroup
import ly.david.musicbrainzjetpackcompose.data.sortAndGroupByTypes
import ly.david.musicbrainzjetpackcompose.ui.common.ClickableCard
import ly.david.musicbrainzjetpackcompose.ui.common.FullScreenLoadingIndicator
import ly.david.musicbrainzjetpackcompose.ui.common.StickyHeader
import ly.david.musicbrainzjetpackcompose.ui.theme.MusicBrainzJetpackComposeTheme

// TODO: rename? will we need something like this for every api return type? Can generalize
private data class ReleaseGroupsByArtistUiState(
    val response: List<ReleaseGroup>? = null,
    val isLoading: Boolean = false,
    val isError: Boolean = false
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ReleaseGroupsByArtistScreen(
    modifier: Modifier,
    artistId: String,
    onReleaseGroupClick: (String) -> Unit = {},
    viewModel: ArtistViewModel = viewModel()
) {
    val uiState by produceState(initialValue = ReleaseGroupsByArtistUiState(isLoading = true)) {
        value = ReleaseGroupsByArtistUiState(response = viewModel.getReleaseGroupsByArtist(artistId = artistId))
    }

    when {
        uiState.response != null -> {
            uiState.response?.let { response ->
                LazyColumn(
                    modifier = modifier
                    // rememberLazyListState() currently not working for possibly one of many reasons:
                    //  - not working for lists that have headers/footers
                    //  - not working for lazy lists in NavHost
                    //  - not working for lazy lists at all
                    // https://issuetracker.google.com/issues/177245496
                ) {
                    item {
                        val results = response.size
                        if (results == 0) {
                            Text("No release groups found for this artist.")
                        } else {
                            Text("Found $results release groups for this artist.")
                        }
                    }

                    response.sortAndGroupByTypes().forEach { (type, releaseGroupsForType) ->

                        // TODO: clicking on header should collapse the group
                        stickyHeader {
                            StickyHeader(text = "$type (${releaseGroupsForType.size})")
                        }
                        items(releaseGroupsForType.sortedBy {
                            it.firstReleaseDate.toDate()
                        }) { releaseGroup ->
                            ReleaseGroupCard(releaseGroup = releaseGroup) {
                                onReleaseGroupClick(it.id)
                            }
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

@Composable
private fun ReleaseGroupCard(
    releaseGroup: ReleaseGroup,
    onClick: (ReleaseGroup) -> Unit = {}
) {
    ClickableCard(
        onClick = { onClick(releaseGroup) },
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
        ) {
            Text(
                text = releaseGroup.title,
                modifier = Modifier.weight(3f)
            )
            Spacer(modifier = Modifier.padding(4.dp))
            Text(
                text = releaseGroup.firstReleaseDate.getYear(),
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.End
            )
        }
    }
}

private val testReleaseGroup = ReleaseGroup(
    id = "6825ace2-3563-4ac5-8d85-c7bf1334bd2c",
    title = "欠けた心象、世のよすが",
    primaryType = "EP",
    primaryTypeId = "6d0c5bf6-7a33-3420-a519-44fc63eedebf",
    firstReleaseDate = "2021-09-08"
)

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
internal fun ReleaseGroupCardPreview() {
    MusicBrainzJetpackComposeTheme {
        ReleaseGroupCard(testReleaseGroup)
    }
}
