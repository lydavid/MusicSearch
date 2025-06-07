package ly.david.musicsearch.shared.feature.graph

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.network.collaboratableEntities
import ly.david.musicsearch.ui.common.component.ClickableItem
import ly.david.musicsearch.ui.common.getName
import ly.david.musicsearch.ui.common.icons.Check
import ly.david.musicsearch.ui.common.icons.CustomIcons
import ly.david.musicsearch.ui.common.theme.LocalStrings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun EntityCollaborationBottomSheet(
    selectedEntity: MusicBrainzEntity,
    onClick: (MusicBrainzEntity) -> Unit = {},
    bottomSheetState: SheetState = rememberModalBottomSheetState(),
    onDismiss: () -> Unit = {},
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = bottomSheetState,
    ) {
        EntityCollaborationBottomSheetContent(
            selectedEntity = selectedEntity,
            onClick = onClick,
        )
    }
}

@Composable
internal fun EntityCollaborationBottomSheetContent(
    selectedEntity: MusicBrainzEntity,
    onClick: (MusicBrainzEntity) -> Unit = {},
) {
    val strings = LocalStrings.current

    Column {
        collaboratableEntities.forEach {
            ClickableItem(
                title = it.getName(strings),
                endIcon = if (selectedEntity == it) CustomIcons.Check else null,
                onClick = {
                    onClick(it)
                },
            )
        }
    }
}
