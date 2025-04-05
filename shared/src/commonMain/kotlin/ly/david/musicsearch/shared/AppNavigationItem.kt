package ly.david.musicsearch.shared

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CollectionsBookmark
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.slack.circuit.runtime.screen.Screen
import ly.david.musicsearch.ui.common.screen.CollectionListScreen
import ly.david.musicsearch.ui.common.screen.DatabaseScreen
import ly.david.musicsearch.ui.common.screen.SearchScreen
import ly.david.musicsearch.ui.common.screen.SettingsScreen

internal enum class AppNavigationItem(
    val icon: ImageVector,
    val screen: Screen,
) {
    Search(
        Icons.Default.Search,
        SearchScreen(),
    ),
    Database(
        CustomIcons.Database,
        DatabaseScreen,
    ),
    Collection(
        Icons.Default.CollectionsBookmark,
        CollectionListScreen(),
    ),
    Settings(
        Icons.Default.Settings,
        SettingsScreen,
    ),
}
