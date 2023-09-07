package ly.david.mbjc.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import ly.david.ui.core.theme.BaseTheme
import ly.david.ui.settings.AppPreferences

@AndroidEntryPoint
internal class MainActivity : ComponentActivity() {

    @Inject
    lateinit var appPreferences: AppPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()

            BaseTheme(
                content = {
                    TopLevelScaffold(navController)
                }
            )
        }
    }
}
