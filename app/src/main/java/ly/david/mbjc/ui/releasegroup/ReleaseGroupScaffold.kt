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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import ly.david.data.domain.ListItemModel
import ly.david.data.domain.ReleaseListItemModel
import ly.david.data.navigation.Destination
import ly.david.data.network.MusicBrainzResource
import ly.david.mbjc.R
import ly.david.mbjc.ui.common.ResourceIcon
import ly.david.mbjc.ui.common.fullscreen.FullScreenErrorWithRetry
import ly.david.mbjc.ui.common.fullscreen.FullScreenLoadingIndicator
import ly.david.mbjc.ui.common.paging.RelationsScreen
import ly.david.mbjc.ui.common.rememberFlowWithLifecycleStarted
import ly.david.mbjc.ui.common.topappbar.CopyToClipboardMenuItem
import ly.david.mbjc.ui.common.topappbar.OpenInBrowserMenuItem
import ly.david.mbjc.ui.common.topappbar.TopAppBarWithFilter
import ly.david.mbjc.ui.releasegroup.details.ReleaseGroupDetailsScreen
import ly.david.mbjc.ui.releasegroup.releases.ReleasesByReleaseGroupScreen
import ly.david.mbjc.ui.releasegroup.stats.ReleaseGroupStatsScreen

internal enum class ReleaseGroupTab(@StringRes val titleRes: Int) {
    DETAILS(R.string.details),
    RELEASES(R.string.releases),
    RELATIONSHIPS(R.string.relationships),
    STATS(R.string.stats),
}

/**
 * Equivalent to a screen like: https://musicbrainz.org/release-group/81d75493-78b6-4a37-b5ae-2a3918ee3756
 *
 * Starts on a screen that displays all of its releases.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ReleaseGroupScaffold(
    releaseGroupId: String,
    titleWithDisambiguation: String? = null,
    onBack: () -> Unit = {},
    onItemClick: (destination: Destination, id: String, title: String?) -> Unit = { _, _, _ -> },
    viewModel: ReleaseGroupViewModel = hiltViewModel()
) {
    val resource = MusicBrainzResource.RELEASE_GROUP
    val snackbarHostState = remember { SnackbarHostState() }
    var selectedTab by rememberSaveable { mutableStateOf(ReleaseGroupTab.DETAILS) }
    var filterText by rememberSaveable { mutableStateOf("") }
    var forceRefresh by rememberSaveable { mutableStateOf(false) }

    val title by viewModel.title.collectAsState()
    val subtitle by viewModel.subtitle.collectAsState()
    val releaseGroup by viewModel.releaseGroup.collectAsState()
    val showError by viewModel.isFullScreenError.collectAsState()
    val url by viewModel.url.collectAsState()

    LaunchedEffect(key1 = releaseGroupId) {
        viewModel.setTitle(titleWithDisambiguation)
    }

    LaunchedEffect(key1 = selectedTab, key2 = forceRefresh) {
        viewModel.onSelectedTabChange(
            releaseGroupId = releaseGroupId,
            selectedTab = selectedTab
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBarWithFilter(
                resource = resource,
                title = title,
                subtitle = subtitle,
                onBack = onBack,
                overflowDropdownMenuItems = {
                    OpenInBrowserMenuItem(resource = MusicBrainzResource.RELEASE_GROUP, resourceId = releaseGroupId)
                    CopyToClipboardMenuItem(releaseGroupId)
                },
                subtitleDropdownMenuItems = {
                    releaseGroup?.artistCredits?.forEach { artistCredit ->
                        DropdownMenuItem(
                            text = { Text(artistCredit.name) },
                            leadingIcon = { ResourceIcon(resource = MusicBrainzResource.ARTIST) },
                            onClick = {
                                closeMenu()
                                // Don't pass a title, because the name used here may not be the name used for the
                                // the artist's page.
                                onItemClick(Destination.LOOKUP_ARTIST, artistCredit.artistId, null)
                            })
                    }
                },
                tabsTitles = ReleaseGroupTab.values().map { stringResource(id = it.titleRes) },
                selectedTabIndex = selectedTab.ordinal,
                onSelectTabIndex = { selectedTab = ReleaseGroupTab.values()[it] },
                showFilterIcon = selectedTab == ReleaseGroupTab.RELEASES,
                filterText = filterText,
                onFilterTextChange = {
                    filterText = it
                },
            )
        },
    ) { innerPadding ->

        val detailsLazyListState = rememberLazyListState()

        val releasesLazyListState = rememberLazyListState()
        var pagedReleasesFlow: Flow<PagingData<ReleaseListItemModel>> by remember { mutableStateOf(emptyFlow()) }
        val releasesLazyPagingItems: LazyPagingItems<ReleaseListItemModel> =
            rememberFlowWithLifecycleStarted(pagedReleasesFlow)
                .collectAsLazyPagingItems()

        val relationsLazyListState = rememberLazyListState()
        val relationsLazyPagingItems: LazyPagingItems<ListItemModel> =
            rememberFlowWithLifecycleStarted(viewModel.pagedRelations)
                .collectAsLazyPagingItems()

        when (selectedTab) {
            ReleaseGroupTab.DETAILS -> {
                val releaseGroupListItemModel = releaseGroup
                when {
                    showError -> {
                        FullScreenErrorWithRetry(
                            onClick = { forceRefresh = true }
                        )
                    }
                    releaseGroupListItemModel == null -> {
                        FullScreenLoadingIndicator()
                    }
                    else -> {
                        ReleaseGroupDetailsScreen(
                            releaseGroup = releaseGroupListItemModel,
                            coverArtUrl = url,
                            lazyListState = detailsLazyListState,
                        )
                    }
                }
            }
            ReleaseGroupTab.RELEASES -> {
                ReleasesByReleaseGroupScreen(
                    releaseGroupId = releaseGroupId,
                    modifier = Modifier.padding(innerPadding),
                    snackbarHostState = snackbarHostState,
                    releasesLazyListState = releasesLazyListState,
                    releasesLazyPagingItems = releasesLazyPagingItems,
                    onPagedReleasesFlowChange = { pagedReleasesFlow = it },
                    onReleaseClick = onItemClick,
                    filterText = filterText
                )
            }
            ReleaseGroupTab.RELATIONSHIPS -> {
                viewModel.loadRelations(releaseGroupId)

                RelationsScreen(
                    onItemClick = onItemClick,
                    snackbarHostState = snackbarHostState,
                    lazyListState = relationsLazyListState,
                    lazyPagingItems = relationsLazyPagingItems,
                )
            }
            ReleaseGroupTab.STATS -> {
                ReleaseGroupStatsScreen(releaseGroupId = releaseGroupId)
            }
        }
    }
}
