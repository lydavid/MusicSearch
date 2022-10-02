package ly.david.mbjc.ui.releasegroup

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
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
import ly.david.mbjc.ui.navigation.Destination
import ly.david.mbjc.ui.releasegroup.relations.ReleaseGroupRelationsScreen
import ly.david.mbjc.ui.releasegroup.stats.ReleaseGroupStatsScreen

private enum class ReleaseGroupTab(@StringRes val titleRes: Int) {
    RELEASES(R.string.releases),
    RELATIONSHIPS(R.string.relationships),
    STATS(R.string.stats),
}

/**
 * Equivalent to a screen like: https://musicbrainz.org/release-group/81d75493-78b6-4a37-b5ae-2a3918ee3756
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ReleaseGroupScaffold(
    releaseGroupId: String,
    onReleaseClick: (String) -> Unit = {},
    onItemClick: (destination: Destination, id: String, title: String?) -> Unit = { _, _, _ -> },
    onBack: () -> Unit,
    viewModel: ReleaseGroupViewModel = hiltViewModel()
) {
    val resource = MusicBrainzResource.RELEASE_GROUP

    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    var title by rememberSaveable { mutableStateOf("") }
    var subtitle by rememberSaveable { mutableStateOf("") }
    var selectedTab by rememberSaveable { mutableStateOf(ReleaseGroupTab.RELEASES) }
    var filterText by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(key1 = releaseGroupId) {
        viewModel.updateReleaseGroupId(releaseGroupId)
        try {
            val releaseGroup = viewModel.lookupReleaseGroup(releaseGroupId)
            title = releaseGroup.getNameWithDisambiguation()
            subtitle = "Release Group by ${releaseGroup.artistCredits}"

        } catch (e: Exception) {
            title = "[Release group lookup failed]"
            subtitle = "[error]"
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBarWithSearch(
                resource = resource,
                title = title,
                subtitle = subtitle,
                onBack = onBack,
                dropdownMenuItems = {
                    DropdownMenuItem(
                        text = { Text(stringResource(id = R.string.open_in_browser)) },
                        onClick = {
                            context.lookupInBrowser(resource, releaseGroupId)
                            closeMenu()
                        })
                },
                tabsTitles = ReleaseGroupTab.values().map { stringResource(id = it.titleRes) },
                selectedTabIndex = selectedTab.ordinal,
                onSelectTabIndex = { selectedTab = ReleaseGroupTab.values()[it] },
                showSearchIcon = selectedTab == ReleaseGroupTab.RELEASES,
                searchText = filterText,
                onSearchTextChange = {
                    filterText = it
                    viewModel.updateQuery(query = filterText)
                },
            )
        },
    ) { innerPadding ->

        val releasesLazyListState = rememberLazyListState()
        val releasesLazyPagingItems: LazyPagingItems<ReleaseUiModel> =
            rememberFlowWithLifecycleStarted(viewModel.pagedReleases)
                .collectAsLazyPagingItems()

        val relationsLazyListState = rememberLazyListState()
        var pagedRelations: Flow<PagingData<UiModel>> by remember { mutableStateOf(emptyFlow()) }
        val relationsLazyPagingItems: LazyPagingItems<UiModel> =
            rememberFlowWithLifecycleStarted(pagedRelations)
                .collectAsLazyPagingItems()

        when (selectedTab) {
            ReleaseGroupTab.RELEASES -> {
                ReleasesListScreen(
                    modifier = Modifier.padding(innerPadding),
                    snackbarHostState = snackbarHostState,
                    lazyListState = releasesLazyListState,
                    lazyPagingItems = releasesLazyPagingItems,
                    onReleaseClick = onReleaseClick
                )
            }
            ReleaseGroupTab.RELATIONSHIPS -> {
                ReleaseGroupRelationsScreen(
                    releaseGroupId = releaseGroupId,
                    onItemClick = onItemClick,
                    lazyListState = relationsLazyListState,
                    lazyPagingItems = relationsLazyPagingItems,
                    onPagedRelationsChange = {
                        pagedRelations = it
                    }
                )
            }
            ReleaseGroupTab.STATS -> {
                ReleaseGroupStatsScreen(releaseGroupId = releaseGroupId)
            }
        }
    }
}
