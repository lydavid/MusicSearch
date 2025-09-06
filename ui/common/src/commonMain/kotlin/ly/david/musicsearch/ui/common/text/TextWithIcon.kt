package ly.david.musicsearch.ui.common.text

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import ly.david.musicsearch.ui.common.theme.TextStyles

@Composable
fun TextWithIcon(
    imageVector: ImageVector,
    text: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = null,
        )
        Text(
            text = text,
            modifier = Modifier.padding(start = 8.dp),
            style = TextStyles.getCardBodyTextStyle(),
        )
    }
}
