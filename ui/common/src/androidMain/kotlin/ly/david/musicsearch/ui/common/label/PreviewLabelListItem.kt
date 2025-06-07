package ly.david.musicsearch.ui.common.label

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import ly.david.musicsearch.shared.domain.listitem.LabelListItemModel
import ly.david.musicsearch.ui.common.preview.PreviewWithSharedElementTransition

@PreviewLightDark
@Composable
internal fun PreviewLabelListItem() {
    PreviewWithSharedElementTransition {
        LabelListItem(
            label = LabelListItemModel(
                id = "1",
                name = "Music Label",
            ),
        )
    }
}

@PreviewLightDark
@Composable
internal fun PreviewLabelListItemAllInfo() {
    PreviewWithSharedElementTransition {
        LabelListItem(
            label = LabelListItemModel(
                id = "5",
                name = "Sony Music",
                disambiguation = "global brand, excluding JP, owned by Sony Music Entertainment",
                type = "Original Production",
                labelCode = 10746,
                catalogNumbers = "CAT-123",
            ),
        )
    }
}

@PreviewLightDark
@Composable
internal fun PreviewLabelListItemVisited() {
    PreviewWithSharedElementTransition {
        LabelListItem(
            label = LabelListItemModel(
                id = "5",
                name = "Sony Music",
                disambiguation = "global brand, excluding JP, owned by Sony Music Entertainment",
                type = "Original Production",
                labelCode = 10746,
                catalogNumbers = "CAT-123",
                visited = true,
            ),
        )
    }
}
