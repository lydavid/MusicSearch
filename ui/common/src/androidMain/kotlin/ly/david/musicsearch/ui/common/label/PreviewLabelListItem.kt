package ly.david.musicsearch.ui.common.label

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import ly.david.musicsearch.shared.domain.listitem.LabelListItemModel
import ly.david.musicsearch.ui.core.theme.PreviewTheme

@PreviewLightDark
@Composable
internal fun PreviewLabelListItem() {
    PreviewTheme {
        Surface {
            LabelListItem(
                label = LabelListItemModel(
                    id = "1",
                    name = "Music Label",
                ),
            )
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewLabelListItemAllInfo() {
    PreviewTheme {
        Surface {
            LabelListItem(
                label = LabelListItemModel(
                    id = "5",
                    name = "Sony Music",
                    disambiguation = "global brand, excluding JP, owned by Sony Music Entertainment",
                    type = "Original Production",
                    labelCode = 10746,
                    catalogNumber = "CAT-123",
                ),
            )
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewLabelListItemVisited() {
    PreviewTheme {
        Surface {
            LabelListItem(
                label = LabelListItemModel(
                    id = "5",
                    name = "Sony Music",
                    disambiguation = "global brand, excluding JP, owned by Sony Music Entertainment",
                    type = "Original Production",
                    labelCode = 10746,
                    catalogNumber = "CAT-123",
                    visited = true,
                ),
            )
        }
    }
}
