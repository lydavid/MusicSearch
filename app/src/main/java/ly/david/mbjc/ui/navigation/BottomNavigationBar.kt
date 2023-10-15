package ly.david.mbjc.ui.navigation

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
import ly.david.musicsearch.core.models.navigation.Destination
import ly.david.musicsearch.strings.AppStrings
import ly.david.musicsearch.strings.LocalStrings

private enum class BottomNavigationItem(val icon: ImageVector, val destination: Destination) {
    Search(Icons.Default.Search, Destination.LOOKUP),
    History(Icons.Default.History, Destination.HISTORY),
    Collection(Icons.Default.CollectionsBookmark, Destination.COLLECTIONS),
    Settings(Icons.Default.Settings, Destination.SETTINGS),
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
    currentTopLevelDestination: Destination,
    navigateToTopLevelDestination: (Destination) -> Unit = {},
) {
    val strings = LocalStrings.current

    NavigationBar {
        BottomNavigationItem.values().forEach { item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = null,
                    )
                },
                label = { Text(item.getText(strings)) },
                selected = currentTopLevelDestination == item.destination,
                onClick = {
                    navigateToTopLevelDestination(item.destination)
                },
            )
        }
    }
}
