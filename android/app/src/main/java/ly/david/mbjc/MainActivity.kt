package ly.david.mbjc

import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.content.ContextCompat
import com.slack.circuit.foundation.Circuit
import com.slack.circuit.runtime.screen.Screen
import kotlinx.collections.immutable.toImmutableList
import ly.david.musicsearch.android.feature.spotify.BroadcastTypes
import ly.david.musicsearch.android.feature.spotify.SpotifyBroadcastReceiver
import ly.david.musicsearch.core.models.network.toMusicBrainzEntity
import ly.david.musicsearch.core.preferences.AppPreferences
import ly.david.musicsearch.shared.AppRoot
import ly.david.musicsearch.shared.useDarkTheme
import ly.david.musicsearch.shared.useMaterialYou
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
            BaseTheme(
                darkTheme = appPreferences.useDarkTheme(),
                materialYou = appPreferences.useMaterialYou(),
                content = {
                    AppRoot(
                        circuit = circuit,
                        initialScreens = getInitialScreens(intent.data).toImmutableList(),
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
private const val TYPE = "type"

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
