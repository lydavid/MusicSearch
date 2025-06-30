package ly.david.musicsearch.ui.common.icon

import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ly.david.musicsearch.shared.domain.getNameWithDisambiguation
import ly.david.musicsearch.shared.domain.listitem.EntityListItemModel

@Composable
fun AddToCollectionIconButton(
    entityListItemModel: EntityListItemModel,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    IconButton(
        modifier = modifier,
        onClick = {
            onClick(entityListItemModel.id)
        },
    ) {
        AddToCollectionIcon(
            partOfACollection = entityListItemModel.collected,
            nameWithDisambiguation = entityListItemModel.getNameWithDisambiguation(),
        )
    }
}
