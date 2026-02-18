package ly.david.musicsearch.shared

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.slack.circuit.runtime.screen.Screen
import musicsearch.ui.common.generated.resources.Res
import musicsearch.ui.common.generated.resources.collections
import musicsearch.ui.common.generated.resources.database
import musicsearch.ui.common.generated.resources.search
import musicsearch.ui.common.generated.resources.settings
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun AppNavigationItem.getText(): String =
    stringResource(
        when (this) {
            AppNavigationItem.Search -> Res.string.search
            AppNavigationItem.Database -> Res.string.database
            AppNavigationItem.Collection -> Res.string.collections
            AppNavigationItem.Settings -> Res.string.settings
        },
    )

@Composable
internal fun AppBottomNavigationBar(
    currentTopLevelScreen: Screen,
    navigateToTopLevelScreen: (Screen) -> Unit = {},
) {
    NavigationBar {
        AppNavigationItem.entries.forEach { item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = null,
                    )
                },
                label = { Text(item.getText()) },
                selected = currentTopLevelScreen::class == item.screen::class,
                onClick = {
                    navigateToTopLevelScreen(item.screen)
                },
            )
        }
    }
}
