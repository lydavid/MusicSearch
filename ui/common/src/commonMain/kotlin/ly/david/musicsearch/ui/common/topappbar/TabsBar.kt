package ly.david.musicsearch.ui.common.topappbar

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SecondaryScrollableTabRow
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
        SecondaryScrollableTabRow(
            selectedTabIndex = selectedTabIndex,
            modifier = modifier,
        ) {
            tabsTitle.forEachIndexed { index, title ->
                Tab(
                    text = { Text(title) },
                    selected = selectedTabIndex == index,
                    selectedContentColor = MaterialTheme.colorScheme.primary,
                    unselectedContentColor = MaterialTheme.colorScheme.onSurface,
                    onClick = {
                        onSelectTabIndex(index)
                    },
                )
            }
        }
    }
}
