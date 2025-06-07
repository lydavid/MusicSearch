package ly.david.musicsearch.shared

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.slack.circuit.runtime.screen.Screen
import ly.david.musicsearch.ui.common.theme.LocalStrings

@Composable
fun AppNavigationRail(
    currentTopLevelScreen: Screen,
    modifier: Modifier = Modifier,
    navigateToTopLevelScreen: (Screen) -> Unit = {},
) {
    val strings = LocalStrings.current

    NavigationRail(modifier = modifier) {
        AppNavigationItem.entries.forEach { item ->
            NavigationRailItem(
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
