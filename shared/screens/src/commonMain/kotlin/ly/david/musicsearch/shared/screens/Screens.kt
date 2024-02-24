package ly.david.musicsearch.shared.screens

import com.slack.circuit.runtime.screen.Screen
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.core.parcelize.CommonParcelize

@CommonParcelize
data class SearchScreen(
    val query: String? = null,
    val entity: MusicBrainzEntity? = null,
) : Screen

@CommonParcelize
data class DetailsScreen(
    val entity: MusicBrainzEntity,
    val id: String,
    val title: String?,
) : Screen
