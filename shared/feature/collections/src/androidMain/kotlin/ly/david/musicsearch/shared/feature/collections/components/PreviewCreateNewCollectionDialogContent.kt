package ly.david.musicsearch.shared.feature.collections.components

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.feature.collections.create.CreateNewCollectionDialogContent
import ly.david.musicsearch.ui.common.preview.PreviewTheme

@PreviewLightDark
@Composable
internal fun PreviewCreateNewCollectionDialogContent() {
    PreviewTheme {
        Surface {
            CreateNewCollectionDialogContent()
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewCreateNewCollectionDialogContentDefaultEntity() {
    PreviewTheme {
        Surface {
            CreateNewCollectionDialogContent(
                defaultEntity = MusicBrainzEntityType.ARTIST,
            )
        }
    }
}
