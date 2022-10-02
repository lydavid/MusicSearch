package ly.david.mbjc.ui.label

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import ly.david.mbjc.R
import ly.david.mbjc.data.domain.ReleaseUiModel
import ly.david.mbjc.data.domain.UiModel
import ly.david.mbjc.data.getNameWithDisambiguation
import ly.david.mbjc.data.network.MusicBrainzResource
import ly.david.mbjc.ui.common.lookupInBrowser
import ly.david.mbjc.ui.common.paging.ReleasesListScreen
import ly.david.mbjc.ui.common.rememberFlowWithLifecycleStarted
import ly.david.mbjc.ui.common.topappbar.TopAppBarWithSearch
import ly.david.mbjc.ui.label.relations.LabelRelationsScreen
import ly.david.mbjc.ui.label.stats.LabelStatsScreen
import ly.david.mbjc.ui.navigation.Destination

private enum class LabelTab(@StringRes val titleRes: Int) {
    RELEASES(R.string.releases),
    RELATIONSHIPS(R.string.relationships),
    STATS(R.string.stats)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun LabelScaffold(
    labelId: String,
    onBack: () -> Unit,
    onItemClick: (destination: Destination, id: String, title: String?) -> Unit = { _, _, _ -> },
    viewModel: LabelViewModel = hiltViewModel()
) {

    val snackbarHostState = remember { SnackbarHostState() }
    var title by rememberSaveable { mutableStateOf("") }
    var selectedTab by rememberSaveable { mutableStateOf(LabelTab.RELEASES) }
    val context = LocalContext.current
    var searchText by rememberSaveable { mutableStateOf("") }

    val releasesLazyListState = rememberLazyListState()
    val releasesLazyPagingItems: LazyPagingItems<ReleaseUiModel> =
        rememberFlowWithLifecycleStarted(viewModel.pagedReleases)
            .collectAsLazyPagingItems()

    val relationsLazyListState = rememberLazyListState()
    var pagedRelations: Flow<PagingData<UiModel>> by remember { mutableStateOf(emptyFlow()) }
    val relationsLazyPagingItems: LazyPagingItems<UiModel> = rememberFlowWithLifecycleStarted(pagedRelations)
        .collectAsLazyPagingItems()

    LaunchedEffect(key1 = labelId) {
        viewModel.updateLabelId(labelId)
        title = try {
            // TODO: when returning to screen, we are doing lookup again, and recording any visit
            val label = viewModel.lookupLabel(labelId)
            label.getNameWithDisambiguation()
        } catch (e: Exception) {
            "[Label lookup failed]"
        }
    }

    Scaffold(
        topBar = {
            TopAppBarWithSearch(
                resource = MusicBrainzResource.LABEL,
                title = title,
                onBack = onBack,
                dropdownMenuItems = {
                    DropdownMenuItem(
                        text = { Text(stringResource(id = R.string.open_in_browser)) },
                        onClick = {
                            context.lookupInBrowser(MusicBrainzResource.LABEL, labelId)
                            closeMenu()
                        }
                    )
                },
                tabsTitles = LabelTab.values().map { stringResource(id = it.titleRes) },
                selectedTabIndex = selectedTab.ordinal,
                onSelectTabIndex = { selectedTab = LabelTab.values()[it] },
                showSearchIcon = selectedTab == LabelTab.RELEASES,
                searchText = searchText,
                onSearchTextChange = {
                    searchText = it
                    viewModel.updateQuery(query = searchText)
                },
            )
        },
    ) { innerPadding ->

        when (selectedTab) {
            LabelTab.RELEASES -> {
                ReleasesListScreen(
                    modifier = Modifier.padding(innerPadding),
                    snackbarHostState = snackbarHostState,
                    lazyListState = releasesLazyListState,
                    lazyPagingItems = releasesLazyPagingItems,
                    onReleaseClick = { id, title ->
                        onItemClick(Destination.LOOKUP_RELEASE, id, title)
                    }
                )
            }
            LabelTab.RELATIONSHIPS -> {
                LabelRelationsScreen(
                    labelId = labelId,
                    onItemClick = onItemClick,
                    lazyListState = relationsLazyListState,
                    lazyPagingItems = relationsLazyPagingItems,
                    onPagedRelationsChange = {
                        pagedRelations = it
                    }
                )
            }
            LabelTab.STATS -> {
                LabelStatsScreen(labelId = labelId)
            }
        }
    }
}
