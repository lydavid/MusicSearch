package ly.david.musicsearch.ui.common.topappbar

import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun TabsBar(
    modifier: Modifier = Modifier,
    tabsTitle: List<String> = listOf(),
    selectedTabIndex: Int = 0,
    onSelectTabIndex: (Int) -> Unit = {},
) {
    if (tabsTitle.isNotEmpty()) {
        ScrollableTabRow(
            selectedTabIndex = selectedTabIndex,
            modifier = modifier,
        ) {
            tabsTitle.forEachIndexed { index, title ->
                Tab(
                    text = { Text(title) },
                    selected = selectedTabIndex == index,
                    onClick = {
                        onSelectTabIndex(index)
                    },
                )
            }
        }
    }
}
