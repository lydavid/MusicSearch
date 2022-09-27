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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import ly.david.mbjc.R
import ly.david.mbjc.data.domain.ReleaseUiModel
import ly.david.mbjc.data.network.MusicBrainzResource
import ly.david.mbjc.ui.common.lookupInBrowser
import ly.david.mbjc.ui.common.rememberFlowWithLifecycleStarted
import ly.david.mbjc.ui.common.topappbar.TopAppBarWithSearch
import ly.david.mbjc.ui.label.relations.LabelRelationsScreen
import ly.david.mbjc.ui.label.releases.ReleasesByLabelScreen
import ly.david.mbjc.ui.label.releases.ReleasesByLabelViewModel
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
    onReleaseClick: (String) -> Unit = {},
    onItemClick: (destination: Destination, id: String) -> Unit = { _, _ -> },
    viewModel: ReleasesByLabelViewModel = hiltViewModel()
) {

    val snackbarHostState = remember { SnackbarHostState() }
    var titleState by rememberSaveable { mutableStateOf("") }
    var selectedTab by rememberSaveable { mutableStateOf(LabelTab.RELEASES) }
    val context = LocalContext.current
    var searchText by rememberSaveable { mutableStateOf("") }


    val releasesLazyListState = rememberLazyListState()
    val releasesLazyPagingItems: LazyPagingItems<ReleaseUiModel> =
        rememberFlowWithLifecycleStarted(viewModel.pagedReleases)
            .collectAsLazyPagingItems()

    Scaffold(
        topBar = {
            TopAppBarWithSearch(
                resource = MusicBrainzResource.LABEL,
                title = titleState,
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
                },
            )
        },
    ) { innerPadding ->

        when (selectedTab) {
            LabelTab.RELEASES -> {
                ReleasesByLabelScreen(
                    modifier = Modifier.padding(innerPadding),
                    labelId = labelId,
                    onTitleUpdate = { title ->
                        titleState = title
                    },
                    snackbarHostState = snackbarHostState,
                    onReleaseClick = onReleaseClick,
                    searchText = searchText,
                    viewModel = viewModel,
                    lazyListState = releasesLazyListState,
                    lazyPagingItems = releasesLazyPagingItems
                )
            }
            LabelTab.RELATIONSHIPS -> {
                LabelRelationsScreen(
                    modifier = Modifier.padding(innerPadding),
                    labelId = labelId,
                    onTitleUpdate = { title ->
                        titleState = title
                    },
                    onItemClick = onItemClick
                )
            }
            LabelTab.STATS -> {
                Text(text = stringResource(id = R.string.stats))
            }
        }
    }
}
