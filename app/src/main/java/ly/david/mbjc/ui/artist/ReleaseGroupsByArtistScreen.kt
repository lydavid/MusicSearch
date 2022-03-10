package ly.david.mbjc.ui.artist

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarResult
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import ly.david.mbjc.data.UiReleaseGroup
import ly.david.mbjc.data.getNameWithDisambiguation
import ly.david.mbjc.ui.common.ClickableListItem
import ly.david.mbjc.ui.common.FullScreenLoadingIndicator
import ly.david.mbjc.ui.common.getYear
import ly.david.mbjc.ui.theme.MusicBrainzJetpackComposeTheme

/**
 * Handles loading and errors for paging screens.
 */
@Composable
fun <T : Any> PagingLoadingAndErrorHandler(
    lazyPagingItems: LazyPagingItems<T>,
    scaffoldState: ScaffoldState,
    content: @Composable () -> Unit
) {

    // This doesn't affect "loads" from db/source.
    if (lazyPagingItems.loadState.refresh is LoadState.Loading) {
        FullScreenLoadingIndicator()
        return
    }

    // TODO: going to another tab, and coming back will show same error message (doesn't make another call)
    if (lazyPagingItems.loadState.refresh is LoadState.Error) {
        LaunchedEffect(Unit) {
            val errorMessage = (lazyPagingItems.loadState.refresh as LoadState.Error).error.message
            val displayMessage = "Failed to fetch data: ${errorMessage ?: "unknown"}"
            scaffoldState.snackbarHostState.showSnackbar(displayMessage)
        }

        // TODO: sometimes some amount of data is loaded (despite no internet), so this will show underneath those loaded data
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.body1,
                text = "Couldn't fetch data from Music Brainz.\nCome back later or click below to try again."
            )
            Button(
                modifier = Modifier.padding(top = 16.dp),
                onClick = {
                    lazyPagingItems.retry()
                }
            ) {
                Icon(Icons.Default.Refresh, "")
                Text(
                    modifier = Modifier.padding(start = 8.dp),
                    text = "Retry"
                )
            }
        }
    }


    if (lazyPagingItems.loadState.append is LoadState.Error) {
        LaunchedEffect(Unit) {
            val actionPerformed: SnackbarResult = scaffoldState.snackbarHostState.showSnackbar(
                message = "Failed to fetch more data.",
                actionLabel = "Retry"
            )
            if (actionPerformed == SnackbarResult.ActionPerformed) {
                lazyPagingItems.retry()
            }
        }
    }


    // When not in full-screen loading, or full screen error, display content.
    content()
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ReleaseGroupsByArtistScreen(
    modifier: Modifier,
    artistId: String,
    searchText: String,
    state: LazyListState,
    scaffoldState: ScaffoldState,
    onReleaseGroupClick: (String) -> Unit = {},
    viewModel: ReleaseGroupsByArtistViewModel = hiltViewModel()
) {

    viewModel.updateArtist(artistId = artistId)
    viewModel.updateQuery(query = searchText)

    val lazyPagingItems: LazyPagingItems<UiReleaseGroup> = viewModel.pagedReleaseGroups.collectAsLazyPagingItems()



    PagingLoadingAndErrorHandler(lazyPagingItems = lazyPagingItems, scaffoldState = scaffoldState) {
        LazyColumn(
            state = state,
            modifier = modifier
        ) {
            items(lazyPagingItems) { releaseGroup: UiReleaseGroup? ->
                if (releaseGroup == null) return@items
                ReleaseGroupCard(releaseGroup = releaseGroup) {
                    onReleaseGroupClick(it.id)
                }
            }
        }
    }


    // TODO: allow determinate loading, progress based on remaining number of api calls
//    val uiState: UiState<List<UiReleaseGroup>> by viewModel.uiReleaseGroups.collectAsState()
//    var queryText by rememberSaveable { mutableStateOf("") }
//
//    when {
//        // TODO: could we do something about having to null check twice?
//        uiState.response != null -> {
//            uiState.response?.let { uiReleaseGroups ->
//                LazyColumn(
//                    state = state,
//                    modifier = modifier
//                ) {
//
//                    item {
//
//                        // TODO: add clear
//                        // TODO: in landscape mode, keyboard might block out field
//                        // For now, let's just have this be in the screen itself, so that we don't clog up the top app bar.
//                        OutlinedTextField(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(16.dp),
//                            singleLine = true,
//                            maxLines = 1,
//                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
//                            keyboardActions = KeyboardActions(
//                                onDone = {
//                                    focusManager.clearFocus()
//                                }
//                            ),
//                            placeholder = {
//                                Text(text = "Find in page")
//                            },
//                            value = queryText,
//                            onValueChange = {
//                                queryText = it
//                                viewModel.updateQuery(it)
//                            }
//                        )
//
//                        // TODO: lookup artist (should be cached at this point), and figure out number of release groups
//                        //  on filter: Showing 10 out of 197 release groups for this artist
//                        val results = uiReleaseGroups.size
//                        if (results == 0) {
//                            Text("No release groups found for this artist.")
//                        } else {
//                            Text("Found $results release groups for this artist.")
//                        }
//                    }
//
//                    uiReleaseGroups.sortAndGroupByTypes().forEach { (type, releaseGroupsForType) ->
//
//                        // TODO: clicking on header should collapse the group
//                        stickyHeader {
//                            StickyHeader(text = "$type (${releaseGroupsForType.size})")
//                        }
//                        items(releaseGroupsForType.sortedBy {
//                            it.firstReleaseDate.toDate()
//                        }) { releaseGroup ->
//                            ReleaseGroupCard(releaseGroup = releaseGroup) {
//                                onReleaseGroupClick(it.id)
//                            }
//                        }
//                    }
//
//                }
//            }
//        }
//        uiState.isLoading -> {
//            // TODO: loading bar freezes right before we get all of our data at once as a list
//            //  can we make it more smooth?
//            FullScreenLoadingIndicator()
//        }
//        else -> {
//            Text(text = "error...")
//        }
//    }
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
                    text = releaseGroup.getNameWithDisambiguation(),
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
    name = "欠けた心象、世のよすが",
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
