package ly.david.musicsearch.ui.common.icon

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun AddToCollectionIcon(
    collected: Boolean,
    modifier: Modifier = Modifier,
    nameWithDisambiguation: String = "",
) {
    val contentDescription = if (collected) {
        "Add $nameWithDisambiguation to another collection"
    } else {
        "Add $nameWithDisambiguation to collection"
    }
    CollectionIcon(
        collected = collected,
        modifier = modifier,
        contentDescription = contentDescription,
    )
}
