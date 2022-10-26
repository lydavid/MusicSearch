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
import androidx.compose.runtime.LaunchedEffect
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
import ly.david.data.domain.UiModel
import ly.david.data.getNameWithDisambiguation
import ly.david.data.navigation.Destination
import ly.david.data.network.MusicBrainzResource
import ly.david.mbjc.R
import ly.david.mbjc.ui.artist.relations.ArtistRelationsScreen
import ly.david.mbjc.ui.artist.releasegroups.ReleaseGroupsByArtistScreen
import ly.david.mbjc.ui.artist.stats.ArtistStatsScreen
import ly.david.mbjc.ui.common.rememberFlowWithLifecycleStarted
import ly.david.mbjc.ui.common.topappbar.OpenInBrowserMenuItem
import ly.david.mbjc.ui.common.topappbar.TopAppBarWithSearch

//        listOf("Overview", "Releases", "Recordings", "Works", "Events", "Recordings", "Aliases", "Tags", "Details")

// Would be nice if we could have an enum of Tabs, then pick a subset of them for each of these scaffolds.
// Right now, we just have to copy/paste these around.
private enum class ArtistTab(@StringRes val titleRes: Int) {
    RELEASE_GROUPS(R.string.release_groups),
    RELATIONSHIPS(R.string.relationships),
    STATS(R.string.stats)
}

// TODO: either align ReleaseGroupScaffold to be like this, or align this
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ArtistScaffold(
    artistId: String,
    titleWithDisambiguation: String? = null,
    onReleaseGroupClick: (String) -> Unit = {},
    onItemClick: (destination: Destination, id: String, title: String?) -> Unit = { _, _, _ -> },
    onBack: () -> Unit,

    // This can be hoisted up which would normally let us preview this,
    // but because it relies on compose paging, we can't preview.
    viewModel: ArtistViewModel = hiltViewModel()
) {

    val resource = MusicBrainzResource.ARTIST

    var selectedTab by rememberSaveable { mutableStateOf(ArtistTab.RELEASE_GROUPS) }
    var title by rememberSaveable { mutableStateOf("") }
    var searchText by rememberSaveable { mutableStateOf("") }
    var isSorted by rememberSaveable { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    var recordedLookup by rememberSaveable { mutableStateOf(false) }

    if (!titleWithDisambiguation.isNullOrEmpty()) {
        title = titleWithDisambiguation
    }

    LaunchedEffect(key1 = artistId) {
        val artist = viewModel.getArtist(artistId)
        if (titleWithDisambiguation.isNullOrEmpty()) {
            title = artist.getNameWithDisambiguation()
        }

        // Following experience of Firefox's visit count, which doesn't record back as visits.
        if (!recordedLookup) {
            viewModel.recordLookupHistory(
                resourceId = artistId,
                resource = resource,
                summary = title
            )
            recordedLookup = true
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBarWithSearch(
                onBack = onBack,
                resource = resource,
                title = title,
                showSearchIcon = selectedTab == ArtistTab.RELEASE_GROUPS,
                overflowDropdownMenuItems = {
                    OpenInBrowserMenuItem(resource = MusicBrainzResource.ARTIST, resourceId = artistId)

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

        // This is sufficient to preserve scroll position when switching tabs
        val releaseGroupsLazyListState = rememberLazyListState()
        var pagedReleaseGroups: Flow<PagingData<UiModel>> by remember { mutableStateOf(emptyFlow()) }
        val releaseGroupsLazyPagingItems = rememberFlowWithLifecycleStarted(pagedReleaseGroups)
            .collectAsLazyPagingItems()

        val relationsLazyListState = rememberLazyListState()
        var pagedRelations: Flow<PagingData<UiModel>> by remember { mutableStateOf(emptyFlow()) }
        val relationsLazyPagingItems: LazyPagingItems<UiModel> =
            rememberFlowWithLifecycleStarted(pagedRelations)
                .collectAsLazyPagingItems()

        val statsLazyListState = rememberLazyListState()

        when (selectedTab) {
            ArtistTab.RELEASE_GROUPS -> {
                ReleaseGroupsByArtistScreen(
                    modifier = Modifier.padding(innerPadding),
                    artistId = artistId,
                    searchText = searchText,
                    isSorted = isSorted,
                    snackbarHostState = snackbarHostState,
                    onReleaseGroupClick = onReleaseGroupClick,
                    lazyListState = releaseGroupsLazyListState,
                    lazyPagingItems = releaseGroupsLazyPagingItems,
                    onPagedReleaseGroupsChange = {
                        pagedReleaseGroups = it
                    }
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
                ArtistStatsScreen(
                    artistId = artistId,
                    lazyListState = statsLazyListState
                )
            }
        }
    }
}
