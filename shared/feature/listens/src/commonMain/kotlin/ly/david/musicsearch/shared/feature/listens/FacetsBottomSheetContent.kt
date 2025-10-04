package ly.david.musicsearch.shared.feature.listens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import app.cash.paging.compose.collectAsLazyPagingItems
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.launch
import ly.david.musicsearch.shared.domain.musicbrainz.MusicBrainzEntity
import ly.david.musicsearch.ui.common.icons.Check
import ly.david.musicsearch.ui.common.icons.CustomIcons
import ly.david.musicsearch.ui.common.locale.getAnnotatedName
import ly.david.musicsearch.ui.common.theme.LocalStrings
import ly.david.musicsearch.ui.common.theme.TextStyles
import ly.david.musicsearch.ui.common.topappbar.Tab
import ly.david.musicsearch.ui.common.topappbar.TabsBar
import ly.david.musicsearch.ui.common.topappbar.TopAppBarWithFilter
import ly.david.musicsearch.ui.common.topappbar.getTitle
import ly.david.musicsearch.ui.common.topappbar.toMusicBrainzEntity

internal val tabs = persistentListOf(
    Tab.RECORDINGS,
    Tab.RELEASES,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun FacetsBottomSheetContent(
    state: FacetsUiState,
    onUpdateTab: (tab: Tab) -> Unit = {},
    onFacetClick: (entity: MusicBrainzEntity) -> Unit = {},
    onDismiss: () -> Unit = {},
) {
    // These are not collected until this UI is shown.
    val facets = state.facetsPagingDataFlow.collectAsLazyPagingItems()
    val strings = LocalStrings.current
    val scope = rememberCoroutineScope()
    val selectedTabIndex = tabs.indexOf(state.selectedTab)
    val pagerState = rememberPagerState(
        initialPage = selectedTabIndex,
        pageCount = { tabs.size },
    )

    val latestUpdateTab by rememberUpdatedState(onUpdateTab)
    LaunchedEffect(key1 = pagerState.currentPage) {
        latestUpdateTab(tabs[pagerState.currentPage])
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBarWithFilter(
            title = "Filter by entity",
            modifier = Modifier,
            onBack = onDismiss,
            topAppBarFilterState = state.filterState,
            additionalBar = {
                TabsBar(
                    tabsTitle = tabs.map { it.getTitle(strings) },
                    selectedTabIndex = selectedTabIndex,
                    onSelectTabIndex = { scope.launch { pagerState.animateScrollToPage(it) } },
                )
            },
        )

        HorizontalPager(
            state = pagerState,
        ) { page ->
            LazyColumn {
                items(
                    count = facets.itemCount,
                    key = { index -> facets[index]?.id.orEmpty() },
                ) {
                    facets[it]?.let { facet ->
                        val selected = state.selectedEntityFacet?.id == facet.id
                        Box(
                            modifier = Modifier
                                .background(
                                    if (selected) {
                                        MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                                    } else {
                                        Color.Unspecified
                                    },
                                )
                                .clickable {
                                    tabs[page].toMusicBrainzEntity()?.let { type ->
                                        onFacetClick(
                                            MusicBrainzEntity(
                                                id = facet.id,
                                                type = type,
                                            ),
                                        )
                                    }
                                }
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                        ) {
                            Column(
                                modifier = Modifier
                                    .align(Alignment.CenterStart)
                                    .fillMaxWidth()
                                    .padding(end = 32.dp),
                            ) {
                                val hasUnknownId = facet.id.isEmpty()
                                val title = buildAnnotatedString {
                                    append(
                                        if (hasUnknownId) {
                                            "(Unlinked listens)"
                                        } else {
                                            facet.getAnnotatedName()
                                        },
                                    )
                                    append(" (${facet.count})")
                                }
                                Text(
                                    text = title,
                                    modifier = Modifier.padding(vertical = if (hasUnknownId) 8.dp else 0.dp),
                                    style = TextStyles.getCardBodyTextStyle(),
                                )
                                if (!hasUnknownId) {
                                    Text(
                                        text = facet.formattedArtistCredits,
                                        modifier = Modifier.padding(top = 4.dp),
                                        style = TextStyles.getCardBodySubTextStyle(),
                                    )
                                }
                            }
                            if (selected) {
                                Icon(
                                    imageVector = CustomIcons.Check,
                                    modifier = Modifier.align(Alignment.CenterEnd),
                                    contentDescription = null,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
