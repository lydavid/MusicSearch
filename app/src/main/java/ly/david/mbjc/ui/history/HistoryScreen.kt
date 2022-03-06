package ly.david.mbjc.ui.history

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import ly.david.mbjc.data.LookupHistory
import ly.david.mbjc.ui.Destination
import ly.david.mbjc.ui.common.ClickableListItem
import ly.david.mbjc.ui.common.FullScreenLoadingIndicator
import ly.david.mbjc.ui.common.ScrollableTopAppBar
import ly.david.mbjc.ui.common.UiState
import ly.david.mbjc.ui.theme.MusicBrainzJetpackComposeTheme

@Composable
internal fun HistoryScreenScaffold(
    onItemClick: (destination: Destination, id: String) -> Unit = { _, _ -> },
    openDrawer: () -> Unit = {},
) {

    Scaffold(
        topBar = { ScrollableTopAppBar(title = "Recent History", openDrawer = openDrawer) },
    ) {
        HistoryScreen(onItemClick)
    }
}

val testData = listOf(
    LookupHistory(
        summary = "欠けた心象、世のよすが\nRelease Group by 月詠み",
        destination = Destination.LOOKUP_RELEASE_GROUP,
        mbid = "81d75493-78b6-4a37-b5ae-2a3918ee3756",
        numberOfVisits = 9999
    ),
    LookupHistory(
        summary = "欠けた心象、世のよすが\n" +
            "Release by 月詠み",
        destination = Destination.LOOKUP_RELEASE,
        mbid = "165f6643-2edb-4795-9abe-26bd0533e59d"
    ),
    LookupHistory(
        summary = "月詠み",
        destination = Destination.LOOKUP_ARTIST,
        mbid = "6825ace2-3563-4ac5-8d85-c7bf1334bd2c"
    )
)

@Composable
fun HistoryScreen(
    onItemClick: (destination: Destination, id: String) -> Unit = { _, _ -> },
    viewModel: HistoryViewModel = hiltViewModel()
) {

    val uiState by produceState(initialValue = UiState(isLoading = true)) {
        value = UiState(response = viewModel.getAllLookupHistory())
    }

    when {
        uiState.response != null -> {
            uiState.response?.let { response ->
                LazyColumn {
                    items(response) {
                        HistoryEntry(
                            lookupHistory = it,
                            onItemClick = onItemClick
                        )
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

            Text(
                text = lookupHistory.summary,
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
