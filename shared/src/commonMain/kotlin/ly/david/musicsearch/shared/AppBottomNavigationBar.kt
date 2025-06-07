package ly.david.musicsearch.shared

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.slack.circuit.runtime.screen.Screen
import ly.david.musicsearch.shared.strings.AppStrings
import ly.david.musicsearch.ui.common.theme.LocalStrings

internal fun AppNavigationItem.getText(strings: AppStrings): String =
    when (this) {
        AppNavigationItem.Search -> strings.search
        AppNavigationItem.Database -> strings.database
        AppNavigationItem.Collection -> strings.collections
        AppNavigationItem.Settings -> strings.settings
    }

@Composable
internal fun AppBottomNavigationBar(
    currentTopLevelScreen: Screen,
    navigateToTopLevelScreen: (Screen) -> Unit = {},
) {
    val strings = LocalStrings.current

    NavigationBar {
        AppNavigationItem.entries.forEach { item ->
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
