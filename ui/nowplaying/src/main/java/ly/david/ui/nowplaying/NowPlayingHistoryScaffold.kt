package ly.david.ui.nowplaying

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import ly.david.data.domain.listitem.NowPlayingHistoryListItemModel
import ly.david.data.network.MusicBrainzEntity
import ly.david.ui.common.R
import ly.david.ui.common.paging.PagingLoadingAndErrorHandler
import ly.david.ui.common.rememberFlowWithLifecycleStarted
import ly.david.ui.common.topappbar.TopAppBarWithFilter

@Composable
fun NowPlayingHistoryScaffold(
    modifier: Modifier = Modifier,
    searchMusicBrainz: (query: String, entity: MusicBrainzEntity) -> Unit = { _, _ -> },
) {
    var filterText by rememberSaveable { mutableStateOf("") }

    NowPlayingHistoryScaffold(
        modifier = modifier,
        searchMusicBrainz = searchMusicBrainz,
        filterText = filterText,
        onFilterTextChange = { filterText = it }
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
internal fun NowPlayingHistoryScaffold(
    modifier: Modifier = Modifier,
    searchMusicBrainz: (query: String, entity: MusicBrainzEntity) -> Unit = { _, _ -> },
    filterText: String = "",
    onFilterTextChange: (String) -> Unit = { _ -> },
    viewModel: NowPlayingViewModel = hiltViewModel(),
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val lazyPagingItems = rememberFlowWithLifecycleStarted(viewModel.nowPlayingHistory)
        .collectAsLazyPagingItems()

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBarWithFilter(
                showBackButton = false,
                title = stringResource(id = R.string.now_playing_history),
                scrollBehavior = scrollBehavior,
                filterText = filterText,
                onFilterTextChange = {
                    onFilterTextChange(it)
                    viewModel.updateQuery(query = it)
                },
            )
        },
    ) { innerPadding ->
        PagingLoadingAndErrorHandler(
            lazyPagingItems = lazyPagingItems,
            modifier = Modifier.padding(innerPadding),
        ) { nowPlayingHistory: NowPlayingHistoryListItemModel? ->
            when (nowPlayingHistory) {
                is NowPlayingHistoryListItemModel -> {
                    NowPlayingCard(
                        nowPlayingHistory = nowPlayingHistory,
                        modifier = Modifier.animateItemPlacement(),
                        onClick = {
                            searchMusicBrainz(
                                "$title artist:\"$artist\"",
                                MusicBrainzEntity.RECORDING,
                            )
                        },
                    )
                }

                else -> {
                    // Do nothing.
                }
            }
        }
    }
}
