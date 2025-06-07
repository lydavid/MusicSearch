package ly.david.musicsearch.ui.common

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.network.searchableEntities
import ly.david.musicsearch.ui.common.preview.PreviewTheme

@PreviewLightDark
@Composable
internal fun PreviewExposedDropdownMenuBox() {
    PreviewTheme {
        Surface {
            ResourceDropdownPicker(
                options = searchableEntities,
                selectedOption = MusicBrainzEntity.ARTIST,
            )
        }
    }
}
