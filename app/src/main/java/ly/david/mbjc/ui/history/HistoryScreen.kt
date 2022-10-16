package ly.david.mbjc.ui.history

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import ly.david.mbjc.data.network.MusicBrainzResource
import ly.david.mbjc.data.persistence.history.LookupHistory
import ly.david.mbjc.ui.common.ClickableListItem
import ly.david.mbjc.ui.common.ResourceIcon
import ly.david.mbjc.ui.common.paging.PagingLoadingAndErrorHandler
import ly.david.mbjc.ui.common.preview.DefaultPreviews
import ly.david.mbjc.ui.common.rememberFlowWithLifecycleStarted
import ly.david.mbjc.ui.common.topappbar.TopAppBarWithSearch
import ly.david.mbjc.ui.navigation.Destination
import ly.david.mbjc.ui.navigation.toDestination
import ly.david.mbjc.ui.theme.PreviewTheme
import ly.david.mbjc.ui.theme.TextStyles

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HistoryScreenScaffold(
    onItemClick: (destination: Destination, id: String, title: String?) -> Unit = { _, _, _ -> },
    openDrawer: () -> Unit = {},
    viewModel: HistoryViewModel = hiltViewModel()
) {

    var searchText by rememberSaveable { mutableStateOf("") }
    val lazyPagingItems = rememberFlowWithLifecycleStarted(viewModel.lookUpHistory)
        .collectAsLazyPagingItems()

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
internal fun HistoryScreen(
    lazyPagingItems: LazyPagingItems<LookupHistory>,
    onItemClick: (destination: Destination, id: String, title: String?) -> Unit = { _, _, _ -> },
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
    onItemClick: (destination: Destination, id: String, title: String?) -> Unit = { _, _, _ -> },
) {
    ClickableListItem(
        onClick = {
            onItemClick(lookupHistory.resource.toDestination(), lookupHistory.mbid, lookupHistory.title)
        },
    ) {
        Column(
            modifier = Modifier.padding(vertical = 16.dp)
        ) {

            Row(verticalAlignment = Alignment.CenterVertically) {
                ResourceIcon(
                    resource = lookupHistory.resource,
                    modifier = Modifier.padding(end = 8.dp)
                )

                val resourceDescription = stringResource(id = lookupHistory.resource.displayTextRes)
                Text(
                    text = "$resourceDescription: ${lookupHistory.title}",
                    style = TextStyles.getCardTitleTextStyle(),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Row {
                Text(
                    text = "Last visited: ${lookupHistory.lastAccessed.toDisplayDate()}",
                    style = TextStyles.getCardBodyTextStyle(),
                )

                Text(
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .fillMaxWidth(),
                    text = lookupHistory.numberOfVisits.toString(),
                    style = TextStyles.getCardBodyTextStyle(),
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

internal class LookupHistoryPreviewParameterProvider : PreviewParameterProvider<LookupHistory> {
    override val values: Sequence<LookupHistory> = sequenceOf(
        LookupHistory(
            title = "欠けた心象、世のよすがみ",
            resource = MusicBrainzResource.RELEASE_GROUP,
            mbid = "81d75493-78b6-4a37-b5ae-2a3918ee3756",
            numberOfVisits = 9999
        ),
        LookupHistory(
            title = "欠けた心象、世のよすが",
            resource = MusicBrainzResource.RELEASE,
            mbid = "165f6643-2edb-4795-9abe-26bd0533e59d"
        ),
        LookupHistory(
            title = "月詠み",
            resource = MusicBrainzResource.ARTIST,
            mbid = "6825ace2-3563-4ac5-8d85-c7bf1334bd2c"
        )
    )
}

@DefaultPreviews
@Composable
private fun Preview(
    @PreviewParameter(LookupHistoryPreviewParameterProvider::class) history: LookupHistory
) {
    PreviewTheme {
        Surface {
            HistoryEntry(history)
        }
    }
}
