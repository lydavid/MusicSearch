package ly.david.mbjc.ui.history

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import ly.david.mbjc.data.persistence.LookupHistory
import ly.david.mbjc.ui.Destination
import ly.david.mbjc.ui.common.ClickableListItem
import ly.david.mbjc.ui.common.paging.PagingLoadingAndErrorHandler
import ly.david.mbjc.ui.common.topappbar.TopAppBarWithSearch
import ly.david.mbjc.ui.common.transformThisIfNotNullOrEmpty
import ly.david.mbjc.ui.theme.MusicBrainzJetpackComposeTheme

@Composable
internal fun HistoryScreenScaffold(
    onItemClick: (destination: Destination, id: String) -> Unit = { _, _ -> },
    openDrawer: () -> Unit = {},
    viewModel: HistoryViewModel = hiltViewModel()
) {

    var searchText by rememberSaveable { mutableStateOf("") }
    val lazyPagingItems = viewModel.lookUpHistory.collectAsLazyPagingItems()

    Scaffold(
        topBar = {
            TopAppBarWithSearch(
                openDrawer = openDrawer,
                title = "Recent History",
                searchText = searchText,
                onSearchTextChange = {
                    searchText = it
                    viewModel.updateQuery(query = searchText)
                },
            )
        },
    ) {
        HistoryScreen(
            lazyPagingItems = lazyPagingItems,
            onItemClick = onItemClick
        )
    }
}

@Composable
fun HistoryScreen(
    lazyPagingItems: LazyPagingItems<LookupHistory>,
    onItemClick: (destination: Destination, id: String) -> Unit = { _, _ -> },
) {

    PagingLoadingAndErrorHandler(
        lazyPagingItems = lazyPagingItems,
    ) { lookupHistory: LookupHistory? ->
        when (lookupHistory) {
            is LookupHistory -> {
                HistoryEntry(
                    lookupHistory = lookupHistory,
                    onItemClick = onItemClick
                )
            }
            else -> {
                // Do nothing.
            }
        }
    }
}

@Composable
private fun HistoryEntry(
    lookupHistory: LookupHistory,
    onItemClick: (destination: Destination, id: String) -> Unit = { _, _ -> },
) {
    ClickableListItem(
        onClick = {
            onItemClick(lookupHistory.destination, lookupHistory.mbid)
        },
    ) {
        Column(
            modifier = Modifier.padding(vertical = 16.dp)

        ) {

            val resourceDescription =
                lookupHistory.destination.musicBrainzResource?.displayText?.transformThisIfNotNullOrEmpty { "$it: " }
            Text(
                text = "$resourceDescription${lookupHistory.summary}",
                style = MaterialTheme.typography.h6,
                modifier = Modifier.fillMaxWidth()
            )

            Row {
                Text(
                    text = "Last visited: ${lookupHistory.lastAccessed.toDisplayDate()}",
                    style = MaterialTheme.typography.body1,
                )

                Text(
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .fillMaxWidth(),
                    text = lookupHistory.numberOfVisits.toString(),
                    style = MaterialTheme.typography.body1,
                    textAlign = TextAlign.End
                )
            }
        }
    }
}

private fun Date.toDisplayDate(): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    return dateFormat.format(this)
}

val testData = listOf(
    LookupHistory(
        summary = "欠けた心象、世のよすがみ",
        destination = Destination.LOOKUP_RELEASE_GROUP,
        mbid = "81d75493-78b6-4a37-b5ae-2a3918ee3756",
        numberOfVisits = 9999
    ),
    LookupHistory(
        summary = "欠けた心象、世のよすが",
        destination = Destination.LOOKUP_RELEASE,
        mbid = "165f6643-2edb-4795-9abe-26bd0533e59d"
    ),
    LookupHistory(
        summary = "月詠み",
        destination = Destination.LOOKUP_ARTIST,
        mbid = "6825ace2-3563-4ac5-8d85-c7bf1334bd2c"
    )
)

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
internal fun ArtistCardPreview() {
    MusicBrainzJetpackComposeTheme {
        Surface {
            HistoryEntry(testData.first())
        }
    }
}
