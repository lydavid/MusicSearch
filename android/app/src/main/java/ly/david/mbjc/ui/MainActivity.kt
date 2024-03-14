package ly.david.mbjc.ui

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.foundation.Circuit
import com.slack.circuit.foundation.CircuitCompositionLocals
import com.slack.circuit.foundation.NavigableCircuitContent
import com.slack.circuit.foundation.rememberCircuitNavigator
import com.slack.circuit.overlay.ContentWithOverlays
import com.slack.circuit.runtime.screen.Screen
import ly.david.mbjc.ui.navigation.BottomNavigationBar
import ly.david.musicsearch.core.models.navigation.Destination
import ly.david.musicsearch.core.models.network.toMusicBrainzEntity
import ly.david.musicsearch.core.preferences.AppPreferences
import ly.david.ui.common.screen.CollectionListScreen
import ly.david.ui.common.screen.CollectionScreen
import ly.david.ui.common.screen.DetailsScreen
import ly.david.ui.common.screen.HistoryScreen
import ly.david.ui.common.screen.SearchScreen
import ly.david.ui.common.screen.SettingsScreen
import ly.david.ui.core.theme.BaseTheme
import org.koin.android.ext.android.inject

internal class MainActivity : ComponentActivity() {

    private val appPreferences: AppPreferences by inject()
    private val circuit: Circuit by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            BaseTheme(
                darkTheme = appPreferences.useDarkTheme(),
                materialYou = appPreferences.useMaterialYou(),
                content = {
                    Root(
                        circuit = circuit,
                        initialScreens = getInitialScreens(intent.data),
                    )
                },
            )
        }
    }
}

private const val QUERY = "query"
private const val TYPE = "type"

private fun getInitialScreens(
    uri: Uri?,
): List<Screen> {
    if (uri == null) return listOf(SearchScreen())

    val initialScreens: MutableList<Screen> = mutableListOf()
    val pathSegments = uri.pathSegments
    when (pathSegments.first()) {
        Destination.LOOKUP.route -> {
            initialScreens.add(
                SearchScreen(
                    query = uri.getQueryParameter(QUERY),
                    entity = uri.getQueryParameter(TYPE)?.toMusicBrainzEntity(),
                ),
            )
        }

        Destination.HISTORY.route -> {
            initialScreens.add(HistoryScreen)
        }

        Destination.COLLECTIONS.route -> {
            initialScreens.add(CollectionListScreen)
            if (pathSegments.size > 1) {
                val collectionId = pathSegments.last()
                initialScreens.add(
                    CollectionScreen(
                        id = collectionId,
                    ),
                )
            }
        }

        Destination.SETTINGS.route -> {
            initialScreens.add(SettingsScreen)
        }

        else -> {
            initialScreens.add(SearchScreen())
            val entity = pathSegments.first().toMusicBrainzEntity()
            if (entity != null && pathSegments.size > 1) {
                val id = pathSegments.last()
                initialScreens.add(
                    DetailsScreen(
                        entity = entity,
                        id = id,
                        title = null,
                    ),
                )
            }
        }
    }

    return initialScreens
}

@Composable
private fun Root(
    circuit: Circuit,
    initialScreens: List<Screen>,
) {
    CircuitCompositionLocals(circuit) {
        ContentWithOverlays {
            val backStack = rememberSaveableBackStack(
                initialScreens = initialScreens,
            )
            val navigator = rememberCircuitNavigator(backStack)

            Scaffold(
                bottomBar = {
                    BottomNavigationBar(
                        currentTopLevelScreen = backStack.last().screen,
                        navigateToTopLevelScreen = { screen ->
                            navigator.resetRoot(screen)
                        },
                    )
                },
            ) { innerPadding ->

                NavigableCircuitContent(
                    navigator = navigator,
                    backStack = backStack,
                    modifier = Modifier.padding(innerPadding),
                )
            }
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
