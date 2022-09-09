package ly.david.mbjc.ui.artist

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
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import ly.david.mbjc.R
import ly.david.mbjc.data.domain.UiModel
import ly.david.mbjc.data.network.MusicBrainzResource
import ly.david.mbjc.ui.artist.relations.ArtistRelationsScreen
import ly.david.mbjc.ui.artist.releasegroups.ReleaseGroupsByArtistScreen
import ly.david.mbjc.ui.artist.releasegroups.ReleaseGroupsByArtistViewModel
import ly.david.mbjc.ui.artist.stats.ArtistStatsScreen
import ly.david.mbjc.ui.common.lookupInBrowser
import ly.david.mbjc.ui.common.rememberFlowWithLifecycleStarted
import ly.david.mbjc.ui.common.topappbar.TopAppBarWithSearch
import ly.david.mbjc.ui.navigation.Destination

//        listOf("Overview", "Releases", "Recordings", "Works", "Events", "Recordings", "Aliases", "Tags", "Details")

// Would be nice if we could have an enum of Tabs, then pick a subset of them for each of these scaffolds.
// Right now, we just have to copy/paste these around.
private enum class ArtistTab(@StringRes val titleRes: Int) {
    RELEASE_GROUPS(R.string.release_groups),
    RELATIONSHIPS(R.string.relationships),
    STATS(R.string.stats)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ArtistScaffold(
    artistId: String,
    onReleaseGroupClick: (String) -> Unit = {},
    onItemClick: (destination: Destination, id: String) -> Unit = { _, _ -> },
    onBack: () -> Unit,
    viewModel: ReleaseGroupsByArtistViewModel = hiltViewModel()
) {

    var selectedTab by rememberSaveable { mutableStateOf(ArtistTab.RELEASE_GROUPS) }
    var artistName by rememberSaveable { mutableStateOf("") }
    var searchText by rememberSaveable { mutableStateOf("") }
    var isSorted by rememberSaveable { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // This is sufficient to preserve scroll position when switching tabs
    val releaseGroupsLazyListState = rememberLazyListState()
    val releaseGroupsLazyPagingItems = rememberFlowWithLifecycleStarted(viewModel.pagedReleaseGroups)
        .collectAsLazyPagingItems()

    val relationsLazyListState = rememberLazyListState()
    var pagedRelations: Flow<PagingData<UiModel>> by remember { mutableStateOf(emptyFlow()) }
    val relationsLazyPagingItems: LazyPagingItems<UiModel> =
        rememberFlowWithLifecycleStarted(pagedRelations)
            .collectAsLazyPagingItems()

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBarWithSearch(
                onBack = onBack,
                resource = MusicBrainzResource.ARTIST,
                title = artistName,
                showSearchIcon = selectedTab == ArtistTab.RELEASE_GROUPS,
                dropdownMenuItems = {
                    DropdownMenuItem(
                        text = { Text("Open in browser") },
                        onClick = {
                            context.lookupInBrowser(MusicBrainzResource.ARTIST, artistId)
                            closeMenu()
                        })

                    if (selectedTab == ArtistTab.RELEASE_GROUPS) {
                        DropdownMenuItem(
                            text = {
                                Text(if (isSorted) "Un-sort" else "Sort")
                            },
                            onClick = {
                                closeMenu()
                                // TODO: disclaimer when turning on sort if we have not gotten all release groups
                                isSorted = !isSorted
                            }
                        )
                    }

                    if (selectedTab == ArtistTab.RELEASE_GROUPS || selectedTab == ArtistTab.RELATIONSHIPS) {
                        DropdownMenuItem(
                            text = {
                                Text(stringResource(id = R.string.refresh))
                            },
                            onClick = {
                                closeMenu()
                                coroutineScope.launch {
                                    if (selectedTab == ArtistTab.RELEASE_GROUPS) {
                                        releaseGroupsLazyListState.scrollToItem(0)
                                        releaseGroupsLazyPagingItems.refresh()
                                    } else {
                                        relationsLazyListState.scrollToItem(0)
                                        relationsLazyPagingItems.refresh()
                                    }
                                }
                            }
                        )
                    }
                },
                searchText = searchText,
                onSearchTextChange = {
                    searchText = it
                },
                tabsTitles = ArtistTab.values().map { stringResource(id = it.titleRes) },
                selectedTabIndex = selectedTab.ordinal,
                onSelectTabIndex = { selectedTab = ArtistTab.values()[it] }
            )
        },
    ) { innerPadding ->

        when (selectedTab) {
            ArtistTab.RELEASE_GROUPS -> {
                ReleaseGroupsByArtistScreen(
                    modifier = Modifier.padding(innerPadding),
                    artistId = artistId,
                    searchText = searchText,
                    isSorted = isSorted,
                    snackbarHostState = snackbarHostState,
                    onReleaseGroupClick = onReleaseGroupClick,
                    onTitleUpdate = {
                        artistName = it
                    },
                    viewModel = viewModel,
                    lazyListState = releaseGroupsLazyListState,
                    lazyPagingItems = releaseGroupsLazyPagingItems
                )
            }
            ArtistTab.RELATIONSHIPS -> {
                ArtistRelationsScreen(
                    artistId = artistId,
                    onItemClick = onItemClick,
                    lazyListState = relationsLazyListState,
                    lazyPagingItems = relationsLazyPagingItems,
                    onPagedRelationsChange = {
                        pagedRelations = it
                    }
                )
            }
            ArtistTab.STATS -> {
                ArtistStatsScreen(artistId = artistId)
            }
        }
    }
}
