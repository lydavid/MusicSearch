package ly.david.mbjc.ui.area

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
import ly.david.mbjc.data.Area
import ly.david.mbjc.data.domain.ReleaseUiModel
import ly.david.mbjc.data.domain.UiModel
import ly.david.mbjc.data.getNameWithDisambiguation
import ly.david.mbjc.data.network.MusicBrainzResource
import ly.david.mbjc.ui.area.relations.AreaRelationsScreen
import ly.david.mbjc.ui.common.lookupInBrowser
import ly.david.mbjc.ui.common.paging.ReleasesListScreen
import ly.david.mbjc.ui.common.rememberFlowWithLifecycleStarted
import ly.david.mbjc.ui.common.topappbar.TopAppBarWithSearch
import ly.david.mbjc.ui.navigation.Destination

// TODO: how to exclude a tab for certain types?
private enum class AreaTab(@StringRes val titleRes: Int) {
    RELATIONSHIPS(R.string.relationships),
    RELEASES(R.string.releases),
    STATS(R.string.stats)
}

/**
 * A screen that starts on relationships tab.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AreaScaffold(
    areaId: String,
    titleWithDisambiguation: String? = null,
    onBack: () -> Unit,
    onItemClick: (destination: Destination, id: String, title: String?) -> Unit = { _, _, _ -> },
    viewModel: AreaViewModel = hiltViewModel(),
) {

    val resource = MusicBrainzResource.AREA

    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    // TODO: api doesn't seem to include area containment
    //  but we could get its parent area via relations "part of" "backward"
    var title by rememberSaveable { mutableStateOf("") }
    var selectedTab by rememberSaveable { mutableStateOf(AreaTab.RELATIONSHIPS) }
    var searchText by rememberSaveable { mutableStateOf("") }
    var recordedLookup by rememberSaveable { mutableStateOf(false) }
    var areaUiModel: Area? by remember { mutableStateOf(null) }

    val relationsLazyListState = rememberLazyListState()
    var pagedRelations: Flow<PagingData<UiModel>> by remember { mutableStateOf(emptyFlow()) }
    val relationsLazyPagingItems: LazyPagingItems<UiModel> = rememberFlowWithLifecycleStarted(pagedRelations)
        .collectAsLazyPagingItems()

    val releasesLazyListState = rememberLazyListState()
    val releasesLazyPagingItems: LazyPagingItems<ReleaseUiModel> =
        rememberFlowWithLifecycleStarted(viewModel.pagedReleases)
            .collectAsLazyPagingItems()

    if (!titleWithDisambiguation.isNullOrEmpty()) {
        title = titleWithDisambiguation
    }

    LaunchedEffect(key1 = title) {
        if (title.isEmpty()) return@LaunchedEffect

        if (!recordedLookup) {
            viewModel.recordLookupHistory(
                resourceId = areaId,
                resource = resource,
                summary = title
            )
            recordedLookup = true
        }
    }

    Scaffold(
        topBar = {
            TopAppBarWithSearch(
                resource = resource,
                title = title,
                onBack = onBack,
                overflowDropdownMenuItems = {
                    DropdownMenuItem(
                        text = { Text(stringResource(id = R.string.open_in_browser)) },
                        onClick = {
                            context.lookupInBrowser(resource, areaId)
                            closeMenu()
                        }
                    )
                },
                tabsTitles = AreaTab.values()
//                    .filter { it != AreaTab.RELEASES || areaUiModel?.type == "Country" }
                    .map { stringResource(id = it.titleRes) },
                selectedTabIndex = selectedTab.ordinal,
                // TODO: selecting by index does not allow filtering tabs
                //  using title res may not be the best idea
                //  how about passing in a list of tabs?
                onSelectTabIndex = { selectedTab = AreaTab.values()[it] },
                showSearchIcon = selectedTab == AreaTab.RELEASES,
                searchText = searchText,
                onSearchTextChange = {
                    searchText = it
                    viewModel.query.value = searchText
                },
            )
        },
    ) { innerPadding ->

        when (selectedTab) {
            AreaTab.RELATIONSHIPS -> {
                AreaRelationsScreen(
                    modifier = Modifier.padding(innerPadding),
                    areaId = areaId,
                    onItemClick = onItemClick,
                    lazyListState = relationsLazyListState,
                    lazyPagingItems = relationsLazyPagingItems,
                    onPagedRelationsChange = {
                        pagedRelations = it
                    },
                    onAreaLookup = { area ->
                        // TODO: this is only called after network call
                        //  returning to this screen will not trigger this
                        areaUiModel = area
                        if (titleWithDisambiguation.isNullOrEmpty()) {
                            title = area.getNameWithDisambiguation()
                        }
                    }
                )
            }
            AreaTab.RELEASES -> {
                viewModel.updateAreaId(areaId)

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
            AreaTab.STATS -> {

            }
        }
    }
}
