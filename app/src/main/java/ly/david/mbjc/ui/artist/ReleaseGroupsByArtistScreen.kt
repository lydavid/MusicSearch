package ly.david.mbjc.ui.artist

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ly.david.mbjc.data.UiReleaseGroup
import ly.david.mbjc.data.getTitleWithDisambiguation
import ly.david.mbjc.data.sortAndGroupByTypes
import ly.david.mbjc.ui.common.ClickableListItem
import ly.david.mbjc.ui.common.FullScreenLoadingIndicator
import ly.david.mbjc.ui.common.StickyHeader
import ly.david.mbjc.ui.common.UiState
import ly.david.mbjc.ui.common.getYear
import ly.david.mbjc.ui.common.toDate
import ly.david.mbjc.ui.theme.MusicBrainzJetpackComposeTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ReleaseGroupsByArtistScreen(
    modifier: Modifier,
    artistId: String,
    state: LazyListState,
    onReleaseGroupClick: (String) -> Unit = {},
    viewModel: ReleaseGroupsByArtistViewModel = hiltViewModel()
) {

    // TODO: these seem to happen on ui thread? It can't load in background when user switches tabs
    // TODO: something like this should be hoisted? don't have to do it now since we're changing to flow
    //  but maybe hoist that flow
    val uiState by produceState(initialValue = UiState(isLoading = true)) {
        value = UiState(response = viewModel.getReleaseGroupsByArtist(artistId = artistId))
    }

    when {
        uiState.response != null -> {
            uiState.response?.let { response: List<UiReleaseGroup> ->

                LazyColumn(
                    state = state,
                    modifier = modifier
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

// TODO: show artist credits if there are any that are different from artist?
//  similar to tracks's shouldShowTrackArtists
//  However, if we have to go through 500+ release groups only to find one at the end, then it might take too much effort to refresh
//  so either always show artist credits or don't
@Composable
private fun ReleaseGroupCard(
    releaseGroup: UiReleaseGroup,
    onClick: (UiReleaseGroup) -> Unit = {}
) {
    ClickableListItem(
        onClick = { onClick(releaseGroup) },
    ) {
        Column(
            modifier = Modifier.padding(vertical = 16.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = releaseGroup.getTitleWithDisambiguation(),
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier.weight(3f)
                )
                Spacer(modifier = Modifier.padding(4.dp))
                Text(
                    text = releaseGroup.firstReleaseDate.getYear(),
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.body1,
                    textAlign = TextAlign.End
                )
            }

            if (releaseGroup.artistCredits.isNotEmpty()) {
                Text(
                    text = releaseGroup.artistCredits,
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.body1
                )
            }
        }
    }
}

private val testReleaseGroup = UiReleaseGroup(
    id = "6825ace2-3563-4ac5-8d85-c7bf1334bd2c",
    title = "欠けた心象、世のよすが",
    primaryType = "EP",
    firstReleaseDate = "2021-09-08",
    artistCredits = "Some artist feat. some other artist"
)

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
internal fun ReleaseGroupCardPreview() {
    MusicBrainzJetpackComposeTheme {
        Surface {
            ReleaseGroupCard(testReleaseGroup)
        }
    }
}
