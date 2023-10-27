package ly.david.musicsearch.feature.search

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ly.david.musicsearch.core.models.network.MusicBrainzEntity

@Composable
actual fun SearchScaffold(
    modifier: Modifier,
    onItemClick: (entity: MusicBrainzEntity, id: String, title: String?) -> Unit,
    initialQuery: String?,
    initialEntity: MusicBrainzEntity?,
) {
    Text("Search screen")
}
