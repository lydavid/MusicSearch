package ly.david.mbjc.ui.series

import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import ly.david.data.domain.ListItemModel
import ly.david.data.domain.SeriesListItemModel
import ly.david.data.getNameWithDisambiguation
import ly.david.data.navigation.Destination
import ly.david.data.network.MusicBrainzResource
import ly.david.mbjc.R
import ly.david.mbjc.ui.common.fullscreen.FullScreenLoadingIndicator
import ly.david.mbjc.ui.common.paging.RelationsScreen
import ly.david.mbjc.ui.common.rememberFlowWithLifecycleStarted
import ly.david.mbjc.ui.common.topappbar.CopyToClipboardMenuItem
import ly.david.mbjc.ui.common.topappbar.OpenInBrowserMenuItem
import ly.david.mbjc.ui.common.topappbar.ScrollableTopAppBar
import ly.david.mbjc.ui.series.details.SeriesDetailsScreen

private enum class SeriesTab(@StringRes val titleRes: Int) {
    DETAILS(R.string.details),
    RELATIONSHIPS(R.string.relationships),
    STATS(R.string.stats)
}

/**
 * The top-level screen for an series.
 *
 * All of its content are relationships, there's no browsing supported in the API.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SeriesScaffold(
    seriesId: String,
    titleWithDisambiguation: String? = null,
    onBack: () -> Unit,
    onItemClick: (destination: Destination, id: String, title: String?) -> Unit = { _, _, _ -> },
    viewModel: SeriesViewModel = hiltViewModel()
) {
    val resource = MusicBrainzResource.SERIES
    var series: SeriesListItemModel? by remember { mutableStateOf(null) }

    val snackbarHostState = remember { SnackbarHostState() }

    var titleState by rememberSaveable { mutableStateOf("") }
    var selectedTab by rememberSaveable { mutableStateOf(SeriesTab.DETAILS) }
    var recordedLookup by rememberSaveable { mutableStateOf(false) }

    if (!titleWithDisambiguation.isNullOrEmpty()) {
        titleState = titleWithDisambiguation
    }

    LaunchedEffect(key1 = seriesId) {
        try {
            val seriesListItemModel = viewModel.lookupSeries(seriesId)
            if (titleWithDisambiguation.isNullOrEmpty()) {
                titleState = seriesListItemModel.getNameWithDisambiguation()
            }
            series = seriesListItemModel
        } catch (ex: Exception) {
            Log.d("Remove This", "SeriesScaffold: $ex")
        }

        if (!recordedLookup) {
            viewModel.recordLookupHistory(
                resourceId = seriesId,
                resource = resource,
                summary = titleState
            )
            recordedLookup = true
        }
    }

    Scaffold(
        topBar = {
            ScrollableTopAppBar(
                resource = resource,
                title = titleState,
                onBack = onBack,
                overflowDropdownMenuItems = {
                    OpenInBrowserMenuItem(resource, seriesId)
                    CopyToClipboardMenuItem(seriesId)
                },
                tabsTitles = SeriesTab.values().map { stringResource(id = it.titleRes) },
                selectedTabIndex = selectedTab.ordinal,
                onSelectTabIndex = { selectedTab = SeriesTab.values()[it] },
            )
        },
    ) { innerPadding ->

        val relationsLazyListState = rememberLazyListState()
        val relationsLazyPagingItems: LazyPagingItems<ListItemModel> =
            rememberFlowWithLifecycleStarted(viewModel.pagedRelations)
                .collectAsLazyPagingItems()

        when (selectedTab) {
            SeriesTab.DETAILS -> {
                val seriesListItemModel = series
                if (seriesListItemModel == null) {
                    FullScreenLoadingIndicator()
                } else {
                    SeriesDetailsScreen(
                        modifier = Modifier.padding(innerPadding),
                        series = seriesListItemModel
                    )
                }
            }
            SeriesTab.RELATIONSHIPS -> {
                viewModel.loadRelations(seriesId)

                RelationsScreen(
                    modifier = Modifier.padding(innerPadding),
                    snackbarHostState = snackbarHostState,
                    onItemClick = onItemClick,
                    lazyListState = relationsLazyListState,
                    lazyPagingItems = relationsLazyPagingItems,
                )
            }
            SeriesTab.STATS -> {

            }
        }
    }
}
