package ly.david.mbjc.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.compose.rememberNavController
import com.slack.circuit.foundation.Circuit
import com.slack.circuit.foundation.CircuitCompositionLocals
import com.slack.circuit.foundation.CircuitContent
import ly.david.mbjc.ui.release.ReleaseTab
import ly.david.musicsearch.core.preferences.AppPreferences
import ly.david.musicsearch.feature.stats.circuit.ReleaseStatsScreen
import ly.david.ui.core.theme.BaseTheme
import org.koin.android.ext.android.inject

internal class MainActivity : ComponentActivity() {

    private val appPreferences: AppPreferences by inject()
    private val circuit: Circuit by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()

            BaseTheme(
                darkTheme = appPreferences.useDarkTheme(),
                materialYou = appPreferences.useMaterialYou(),
                content = {
                    CircuitCompositionLocals(circuit) {
                        CircuitContent(
                            ReleaseStatsScreen(
                                releaseId = "e56065c8-709e-4df8-952b-57031c352a03",
                                tabs = ReleaseTab.values().map { it.tab },
                            ),
                        )
                    }

//                    TopLevelScaffold(navController)
                },
            )
        }
    }
}

@Composable
private fun AppPreferences.useDarkTheme(): Boolean {
    val themePreference = theme.collectAsState(initial = AppPreferences.Theme.SYSTEM)
    return when (themePreference.value) {
        AppPreferences.Theme.LIGHT -> false
        AppPreferences.Theme.DARK -> true
        else -> isSystemInDarkTheme()
    }
}

@Composable
private fun AppPreferences.useMaterialYou(): Boolean {
    val useMaterialYou = useMaterialYou.collectAsState(initial = true)
    return useMaterialYou.value
}
