package ly.david.musicbrainzjetpackcompose.ui.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ly.david.musicbrainzjetpackcompose.ui.theme.getSubBackgroundColor

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ClickableCard(
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        onClick = onClick,
        border = BorderStroke(1.dp, getSubBackgroundColor())
    ) {
        content()
    }
}
