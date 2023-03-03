package ly.david.mbjc.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import ly.david.mbjc.ui.settings.AppPreferences
import ly.david.mbjc.ui.settings.useDarkTheme
import ly.david.mbjc.ui.settings.useMaterialYou
import ly.david.mbjc.ui.theme.BaseTheme

@AndroidEntryPoint
internal class MainActivity : ComponentActivity() {

    @Inject
    lateinit var appPreferences: AppPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            BaseTheme(
                context = this,
                darkTheme = appPreferences.useDarkTheme(),
                materialYou = appPreferences.useMaterialYou()
            ) {
                TopLevelScaffold(navController)
            }
        }
    }
}
