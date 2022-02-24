package ly.david.musicbrainzjetpackcompose.ui.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Generic card with preset modifiers.
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ClickableCard(
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(8.dp),
//        shape = RoundedCornerShape(0.dp),
//        onClick = onClick,
//        elevation = 4.dp
////        border = BorderStroke(1.dp, getSubBackgroundColor())
//    ) {
//        content()
//    }
    ListItem(
        modifier = Modifier
            .clickable { onClick() }
            .fillMaxWidth(),
    ) {
        content()
    }
}

// TODO: ExpandableCard: has two content sections, one for always-show, one for expandable/collapsible
