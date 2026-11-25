package ly.david.musicsearch.ui.common.icon

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import musicsearch.ui.common.generated.resources.Res
import musicsearch.ui.common.generated.resources.editCollectionsForX
import org.jetbrains.compose.resources.stringResource

@Composable
fun EditCollectionIcon(
    collected: Boolean,
    modifier: Modifier = Modifier,
    nameWithDisambiguation: String = "",
) {
    CollectionIcon(
        collected = collected,
        modifier = modifier,
        contentDescription = stringResource(
            Res.string.editCollectionsForX,
            nameWithDisambiguation,
        ),
    )
}
