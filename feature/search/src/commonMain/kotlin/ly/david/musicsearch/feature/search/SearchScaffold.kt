package ly.david.musicsearch.feature.search

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ly.david.musicsearch.core.models.network.MusicBrainzEntity

@Composable
expect fun SearchScaffold(
    modifier: Modifier = Modifier,
    onItemClick: (entity: MusicBrainzEntity, id: String, title: String?) -> Unit = { _, _, _ -> },
    initialQuery: String? = null,
    initialEntity: MusicBrainzEntity? = null,
)
