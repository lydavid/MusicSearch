package ly.david.musicsearch.android.app

import android.content.IntentFilter
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.DisposableEffect
import androidx.core.content.ContextCompat
import com.slack.circuit.backstack.SaveableBackStack
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.foundation.Circuit
import com.slack.circuit.foundation.rememberCircuitNavigator
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.screen.Screen
import kotlinx.collections.immutable.toImmutableList
import ly.david.musicsearch.android.feature.spotify.BroadcastTypes
import ly.david.musicsearch.android.feature.spotify.SpotifyBroadcastReceiver
import ly.david.musicsearch.shared.AppRoot
import ly.david.musicsearch.shared.domain.network.toMusicBrainzEntity
import ly.david.musicsearch.shared.domain.preferences.AppPreferences
import ly.david.musicsearch.shared.useDarkTheme
import ly.david.musicsearch.ui.common.screen.CollectionListScreen
import ly.david.musicsearch.ui.common.screen.CollectionScreen
import ly.david.musicsearch.ui.common.screen.DetailsScreen
import ly.david.musicsearch.ui.common.screen.HistoryScreen
import ly.david.musicsearch.ui.common.screen.SearchScreen
import ly.david.musicsearch.ui.common.screen.SettingsScreen
import ly.david.musicsearch.ui.core.theme.BaseTheme
import org.koin.android.ext.android.inject

internal class MainActivity : ComponentActivity() {

    private val appPreferences: AppPreferences by inject()
    private val circuit: Circuit by inject()
    private val broadcastReceiver by lazy { SpotifyBroadcastReceiver() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        val intentFilter = IntentFilter().apply {
            addAction(BroadcastTypes.METADATA_CHANGED)
            addAction(BroadcastTypes.QUEUE_CHANGED)
            addAction(BroadcastTypes.PLAYBACK_STATE_CHANGED)
        }
        ContextCompat.registerReceiver(
            this,
            broadcastReceiver,
            intentFilter,
            ContextCompat.RECEIVER_EXPORTED,
        )

        setContent {
            val darkTheme = appPreferences.useDarkTheme()

            // We call this again when our theme changes
            DisposableEffect(darkTheme) {
                enableEdgeToEdge(
                    statusBarStyle = SystemBarStyle.auto(
                        Color.TRANSPARENT,
                        Color.TRANSPARENT,
                    ) { darkTheme },
                    navigationBarStyle = SystemBarStyle.auto(
                        Color.TRANSPARENT,
                        Color.TRANSPARENT,
                    ) { darkTheme },
                )
                onDispose {}
            }

            BaseTheme(
                appPreferences = appPreferences,
                darkTheme = darkTheme,
                content = {
                    val backStack: SaveableBackStack = rememberSaveableBackStack(
                        initialScreens = getInitialScreens(intent.data).toImmutableList(),
                    )
                    val navigator: Navigator = rememberCircuitNavigator(
                        backStack = backStack,
                        onRootPop = {},
                    )

                    if (Build.VERSION.SDK_INT <= 33) {
                        // This unfortunately disables predictive back
                        BackHandler {
                            if (navigator.pop() == null) finish()
                        }
                    }

                    AppRoot(
                        backStack = backStack,
                        navigator = navigator,
                        circuit = circuit,
                    )
                },
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(broadcastReceiver)
    }
}

private const val QUERY = "query"
private const val NAME = "name"
private const val ID = "id"
private const val TYPE = "type"

@Suppress("LongMethod")
private fun getInitialScreens(
    uri: Uri?,
): List<Screen> {
    if (uri == null) return listOf(SearchScreen())

    val initialScreens: MutableList<Screen> = mutableListOf()
    val pathSegments = uri.pathSegments

    when (pathSegments.first()) {
        "search" -> {
            initialScreens.add(
                SearchScreen(
                    query = uri.getQueryParameter(QUERY),
                    entity = uri.getQueryParameter(TYPE)?.toMusicBrainzEntity(),
                ),
            )
        }

        "history" -> {
            initialScreens.add(HistoryScreen)
        }

        "collection" -> {
            initialScreens.addAll(getCollectionScreens(uri))
        }

        "settings" -> {
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

private fun getCollectionScreens(uri: Uri): List<Screen> {
    if (uri.pathSegments.size <= 1) return listOf(CollectionListScreen())

    return when (uri.pathSegments[1]) {
        "create" -> listOf(
            CollectionListScreen(
                newCollectionId = uri.getQueryParameter(ID),
                newCollectionName = uri.getQueryParameter(NAME),
                newCollectionEntity = uri.getQueryParameter(TYPE)?.toMusicBrainzEntity(),
            ),
        )

        else -> {
            val collectionId = uri.pathSegments[1]
            val isAddOperation = uri.pathSegments.getOrNull(2) == "add"

            listOf(
                CollectionListScreen(),
                CollectionScreen(
                    collectionId = collectionId,
                    collectableId = if (isAddOperation) uri.getQueryParameter(ID) else null,
                ),
            )
        }
    }
}
