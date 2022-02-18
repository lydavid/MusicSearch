package ly.david.musicbrainzjetpackcompose.ui.artist

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
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ly.david.musicbrainzjetpackcompose.common.getYear
import ly.david.musicbrainzjetpackcompose.common.toDate
import ly.david.musicbrainzjetpackcompose.data.Artist
import ly.david.musicbrainzjetpackcompose.data.ReleaseGroup
import ly.david.musicbrainzjetpackcompose.ui.common.StickyHeader
import ly.david.musicbrainzjetpackcompose.ui.theme.MusicBrainzJetpackComposeTheme

// TODO:
@Composable
fun ArtistScreenScaffold(
    artist: Artist,
    onReleaseGroupClick: (String) -> Unit = {},
) {

    // TODO: setting to show more options. By default, we can keep it as just title/year,
    //  but we should be able to see all the relevant info from this screen too like we do from web
    //  each row will probably just be label: data

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = artist.name) },
            )
        },
//        bottomBar = {
        // TODO: meant for main navigation in app, so this nested screen shouldn't use it
        //  instead, it should use tabs, which don't belong in topbar
        //  however, it won't fit too many
        // https://developer.android.com/reference/kotlin/androidx/compose/material/package-summary#Tab(kotlin.Boolean,kotlin.Function0,androidx.compose.ui.Modifier,kotlin.Boolean,kotlin.Function0,kotlin.Function0,androidx.compose.foundation.interaction.MutableInteractionSource,androidx.compose.ui.graphics.Color,androidx.compose.ui.graphics.Color)
        // https://developer.android.com/reference/kotlin/androidx/compose/material/package-summary#ScrollableTabRow(kotlin.Int,androidx.compose.ui.Modifier,androidx.compose.ui.graphics.Color,androidx.compose.ui.graphics.Color,androidx.compose.ui.unit.Dp,kotlin.Function1,kotlin.Function0,kotlin.Function0)
        // TODO: apparently, we can get scrolling, so maybe we can fit everything
        // See Reddit is Fun app: check out the behaviour to hide top bar and tabs on scroll
        // https://material.io/components/tabs#usage
//            BottomNavigation(
//                backgroundColor = Color.White
//            ) {
        // TODO:  should have tabs for Overview (release groups),  releases, recordings, ...
//            }
//        }
    ) { innerPadding ->
        ArtistReleaseGroupsScreen(
            modifier = Modifier.padding(innerPadding),
            artistId = artist.id,
            onReleaseGroupClick = onReleaseGroupClick
        )
    }
}

// TODO: rename? will we need something like this for every api return type? Can generalize
private data class ArtistUiState(
    val response: List<ReleaseGroup>? = null,
    val isLoading: Boolean = false,
    val isError: Boolean = false
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ArtistReleaseGroupsScreen(
    modifier: Modifier,
    artistId: String,
    onReleaseGroupClick: (String) -> Unit = {},
    // This only works if our ViewModel has no parameters. Otherwise we will need Hilt. Or by viewModels() from Activity.
    viewModel: ArtistViewModel = viewModel()
) {
    val uiState by produceState(initialValue = ArtistUiState(isLoading = true)) {
        value = ArtistUiState(response = viewModel.getReleaseGroupsByArtist(artistId = artistId))
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

                    // TODO: primary type can be null, if so, turn to empty string
                    //  flatten list, concat types with +
                    val grouped = response.groupBy {
                        Pair(it.primaryType, it.secondaryTypes) ?: "(no type)"
                    }
                    grouped.forEach { (type, releaseGroupsForType) ->
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
    onClick: (ReleaseGroup) -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        onClick = { onClick(releaseGroup) },
        border = BorderStroke(1.dp, Color.LightGray)
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
                text = releaseGroup.firstReleaseDate?.getYear().orEmpty(),
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
