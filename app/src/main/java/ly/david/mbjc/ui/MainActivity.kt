package ly.david.mbjc.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import ly.david.ui.core.theme.BaseTheme
import ly.david.ui.settings.AppPreferences
import ly.david.ui.settings.useDarkTheme
import ly.david.ui.settings.useMaterialYou
import org.koin.android.ext.android.inject

internal class MainActivity : ComponentActivity() {

    private val appPreferences: AppPreferences by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()

            BaseTheme(
                darkTheme = appPreferences.useDarkTheme(),
                materialYou = appPreferences.useMaterialYou(),
                content = {
                    TopLevelScaffold(navController)
                }
            )
        }
    }
}
