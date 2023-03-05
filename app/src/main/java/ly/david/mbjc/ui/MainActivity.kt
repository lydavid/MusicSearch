package ly.david.mbjc.ui

import android.content.Intent
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
import timber.log.Timber

@AndroidEntryPoint
internal class MainActivity : ComponentActivity() {

    @Inject
    lateinit var appPreferences: AppPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        Timber.d("onCreate")
//        val resp = AuthorizationResponse.fromIntent(intent)
//        val ex = AuthorizationException.fromIntent(intent)
//        if (resp != null) {
//            Timber.d(resp.toString())
//        } else {
//            Timber.d(ex)
//            // authorization failed, check ex for more details
//        }

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

    override fun onResume() {
        super.onResume()
        Timber.d("onResume")
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Timber.d("onNewIntent")

//        setIntent(intent)
    }
}
