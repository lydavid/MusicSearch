package ly.david.musicsearch.shared

import androidx.compose.ui.graphics.vector.ImageVector
import com.slack.circuit.runtime.screen.Screen
import ly.david.musicsearch.ui.common.icons.CollectionsBookmark
import ly.david.musicsearch.ui.common.icons.CustomIcons
import ly.david.musicsearch.ui.common.icons.Database
import ly.david.musicsearch.ui.common.icons.Search
import ly.david.musicsearch.ui.common.icons.Settings
import ly.david.musicsearch.ui.common.screen.CollectionListScreen
import ly.david.musicsearch.ui.common.screen.DatabaseScreen
import ly.david.musicsearch.ui.common.screen.SearchScreen
import ly.david.musicsearch.ui.common.screen.SettingsScreen

internal enum class AppNavigationItem(
    val icon: ImageVector,
    val screen: Screen,
) {
    Search(
        CustomIcons.Search,
        SearchScreen(),
    ),
    Database(
        CustomIcons.Database,
        DatabaseScreen,
    ),
    Collection(
        CustomIcons.CollectionsBookmark,
        CollectionListScreen(),
    ),
    Settings(
        CustomIcons.Settings,
        SettingsScreen,
    ),
}
