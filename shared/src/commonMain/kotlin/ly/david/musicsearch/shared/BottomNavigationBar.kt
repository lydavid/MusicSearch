package ly.david.musicsearch.shared

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CollectionsBookmark
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.slack.circuit.runtime.screen.Screen
import ly.david.musicsearch.shared.strings.AppStrings
import ly.david.ui.common.screen.CollectionListScreen
import ly.david.ui.common.screen.HistoryScreen
import ly.david.ui.common.screen.SearchScreen
import ly.david.ui.common.screen.SettingsScreen
import ly.david.ui.core.LocalStrings

private enum class BottomNavigationItem(
    val icon: ImageVector,
    val screen: Screen,
) {
    Search(
        Icons.Default.Search,
        SearchScreen(),
    ),
    History(
        Icons.Default.History,
        HistoryScreen,
    ),
    Collection(
        Icons.Default.CollectionsBookmark,
        CollectionListScreen,
    ),
    Settings(
        Icons.Default.Settings,
        SettingsScreen,
    ),
}

private fun BottomNavigationItem.getText(strings: AppStrings): String =
    when (this) {
        BottomNavigationItem.Search -> strings.search
        BottomNavigationItem.History -> strings.history
        BottomNavigationItem.Collection -> strings.collections
        BottomNavigationItem.Settings -> strings.settings
    }

@Composable
internal fun BottomNavigationBar(
    currentTopLevelScreen: Screen,
    navigateToTopLevelScreen: (Screen) -> Unit = {},
) {
    val strings = LocalStrings.current

    NavigationBar {
        BottomNavigationItem.entries.forEach { item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = null,
                    )
                },
                label = { Text(item.getText(strings)) },
                selected = currentTopLevelScreen::class == item.screen::class,
                onClick = {
                    navigateToTopLevelScreen(item.screen)
                },
            )
        }
    }
}
