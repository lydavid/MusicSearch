package ly.david.musicbrainzjetpackcompose.ui.history

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ly.david.musicbrainzjetpackcompose.ui.Destination
import ly.david.musicbrainzjetpackcompose.ui.common.ClickableListItem
import ly.david.musicbrainzjetpackcompose.ui.common.ScrollableTopAppBar
import ly.david.musicbrainzjetpackcompose.ui.theme.MusicBrainzJetpackComposeTheme

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

data class HistoricalRecord(
    val summary: String,
    val destination: Destination,
    val id: String,
    val numberOfVisits: Int = 0
)

val testData = listOf(
    HistoricalRecord(
        "Viewed 欠けた心象、世のよすが\nRelease Group by 月詠み",
        Destination.LOOKUP_RELEASE_GROUP,
        "81d75493-78b6-4a37-b5ae-2a3918ee3756"
    ),
    HistoricalRecord(
        "Viewed 欠けた心象、世のよすが\n" +
            "Release by 月詠み",
        Destination.LOOKUP_RELEASE,
        "165f6643-2edb-4795-9abe-26bd0533e59d"
    ),
    HistoricalRecord(
        "Viewed 月詠み",
        Destination.LOOKUP_ARTIST,
        "6825ace2-3563-4ac5-8d85-c7bf1334bd2c"
    )
)

@Composable
fun HistoryScreen(
    onItemClick: (destination: Destination, id: String) -> Unit = { _, _ -> },
    viewModel: HistoryViewModel = viewModel()
) {

    LazyColumn {
        items(testData) {
            HistoryEntry(
                historicalRecord = it,
                onItemClick = onItemClick
            )
        }
    }
}

@Composable
private fun HistoryEntry(
    historicalRecord: HistoricalRecord,
    onItemClick: (destination: Destination, id: String) -> Unit = { _, _ -> },
) {
    ClickableListItem(
        onClick = {
            onItemClick(historicalRecord.destination, historicalRecord.id)
        },
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
        ) {

            Text(
                text = historicalRecord.summary,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
internal fun ArtistCardPreview() {
    MusicBrainzJetpackComposeTheme {
        HistoryEntry(testData.first())
    }
}
