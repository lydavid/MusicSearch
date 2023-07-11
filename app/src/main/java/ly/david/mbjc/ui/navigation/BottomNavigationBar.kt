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
import androidx.compose.ui.res.stringResource
import ly.david.data.domain.Destination
import ly.david.ui.common.R

private enum class BottomNavigationItem(val stringRes: Int, val icon: ImageVector, val destination: Destination) {
    Search(R.string.search, Icons.Default.Search, Destination.LOOKUP),
    History(R.string.history, Icons.Default.History, Destination.HISTORY),
    Collection(R.string.collections, Icons.Default.CollectionsBookmark, Destination.COLLECTIONS),
    Settings(R.string.settings, Icons.Default.Settings, Destination.SETTINGS),
}

@Composable
internal fun BottomNavigationBar(
    currentTopLevelDestination: Destination,
    navigateToTopLevelDestination: (Destination) -> Unit = {},
) {
    NavigationBar {
        BottomNavigationItem.values().forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = null) },
                label = { Text(stringResource(id = item.stringRes)) },
                selected = currentTopLevelDestination == item.destination,
                onClick = {
                    navigateToTopLevelDestination(item.destination)
                }
            )
        }
    }
}
