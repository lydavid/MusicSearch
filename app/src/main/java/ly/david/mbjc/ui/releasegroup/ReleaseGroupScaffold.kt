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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.launch
import ly.david.mbjc.R
import ly.david.mbjc.data.domain.ReleaseUiModel
import ly.david.mbjc.data.network.MusicBrainzResource
import ly.david.mbjc.ui.common.lookupInBrowser
import ly.david.mbjc.ui.common.rememberFlowWithLifecycleStarted
import ly.david.mbjc.ui.common.topappbar.TopAppBarWithSearch
import ly.david.mbjc.ui.navigation.Destination
import ly.david.mbjc.ui.releasegroup.relations.ReleaseGroupRelationsScreen
import ly.david.mbjc.ui.releasegroup.releases.ReleasesByReleaseGroupScreen
import ly.david.mbjc.ui.releasegroup.releases.ReleasesByReleaseGroupViewModel
import ly.david.mbjc.ui.releasegroup.stats.ReleaseGroupStatsScreen

private enum class ReleaseGroupTab(@StringRes val titleRes: Int) {
    RELEASES(R.string.releases),
    RELATIONSHIPS(R.string.relationships),
    STATS(R.string.stats),
}

/**
 * Equivalent of a screen like: https://musicbrainz.org/release-group/81d75493-78b6-4a37-b5ae-2a3918ee3756
 *
 * Displays a list of releases under this release group.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ReleaseGroupScaffold(
    releaseGroupId: String,
    onReleaseClick: (String) -> Unit = {},
    onItemClick: (destination: Destination, id: String) -> Unit = { _, _ -> },
    onBack: () -> Unit,
    viewModel: ReleasesByReleaseGroupViewModel = hiltViewModel()
) {

    val snackbarHostState = remember { SnackbarHostState() }
    var titleState by rememberSaveable { mutableStateOf("") }
    var subtitleState by rememberSaveable { mutableStateOf("") }
    var selectedTab by rememberSaveable { mutableStateOf(ReleaseGroupTab.RELEASES) }
    var searchText by rememberSaveable { mutableStateOf("") }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val releasesLazyListState = rememberLazyListState()
    val releasesLazyPagingItems: LazyPagingItems<ReleaseUiModel> =
        rememberFlowWithLifecycleStarted(viewModel.pagedReleases)
            .collectAsLazyPagingItems()

    // TODO: this is not enough to remember state of Relationships tab. Need to hoist lazypagingitems out too
    val relationshipsLazyListState = rememberLazyListState()

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBarWithSearch(
                resource = MusicBrainzResource.RELEASE_GROUP,
                title = titleState,
                subtitle = subtitleState,
                onBack = onBack,
                dropdownMenuItems = {
                    DropdownMenuItem(
                        text = { Text(stringResource(id = R.string.open_in_browser)) },
                        onClick = {
                            context.lookupInBrowser(MusicBrainzResource.RELEASE_GROUP, releaseGroupId)
                            closeMenu()
                        })

                    if (selectedTab == ReleaseGroupTab.RELEASES) {
                        DropdownMenuItem(
                            text = {
                                Text(stringResource(id = R.string.refresh))
                            },
                            onClick = {
                                closeMenu()
                                coroutineScope.launch {
                                    releasesLazyListState.scrollToItem(0)
                                    releasesLazyPagingItems.refresh()
                                }
                            })
                    }
                },
                tabsTitles = ReleaseGroupTab.values().map { stringResource(id = it.titleRes) },
                selectedTabIndex = selectedTab.ordinal,
                onSelectTabIndex = { selectedTab = ReleaseGroupTab.values()[it] },
                showSearchIcon = selectedTab == ReleaseGroupTab.RELEASES,
                searchText = searchText,
                onSearchTextChange = {
                    searchText = it
                },
            )
        },
    ) { innerPadding ->

        when (selectedTab) {
            ReleaseGroupTab.RELEASES -> {
                ReleasesByReleaseGroupScreen(
                    modifier = Modifier.padding(innerPadding),
                    releaseGroupId = releaseGroupId,
                    onTitleUpdate = { title, subtitle ->
                        titleState = title
                        subtitleState = subtitle
                    },
                    snackbarHostState = snackbarHostState,
                    onReleaseClick = onReleaseClick,
                    searchText = searchText,
                    viewModel = viewModel,
                    lazyListState = releasesLazyListState,
                    lazyPagingItems = releasesLazyPagingItems
                )
            }
            ReleaseGroupTab.RELATIONSHIPS -> {
                ReleaseGroupRelationsScreen(
                    releaseGroupId = releaseGroupId,
                    onItemClick = onItemClick,
                    lazyListState = relationshipsLazyListState
                )
            }
            ReleaseGroupTab.STATS -> {
                ReleaseGroupStatsScreen(releaseGroupId = releaseGroupId)
            }
        }
    }
}
