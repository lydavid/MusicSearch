package ly.david.musicsearch.shared.screens

import com.slack.circuit.runtime.screen.Screen
import ly.david.musicsearch.core.models.network.MusicBrainzEntity

@CommonParcelize
data class SearchScreen(
    val query: String? = null,
    val entity: MusicBrainzEntity? = null,
) : Screen

@CommonParcelize
data object HistoryScreen : Screen

@CommonParcelize
data class DetailsScreen(
    val entity: MusicBrainzEntity,
    val id: String,
    val title: String?,
) : Screen

@CommonParcelize
data object SettingsScreen : Screen

@CommonParcelize
data object LicensesScreen : Screen
