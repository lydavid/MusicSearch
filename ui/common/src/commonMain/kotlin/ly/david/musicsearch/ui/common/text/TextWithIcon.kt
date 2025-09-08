package ly.david.musicsearch.ui.common.text

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import ly.david.musicsearch.ui.common.theme.STANDARD_ICON_SIZE
import ly.david.musicsearch.ui.common.theme.TextStyles

@Composable
fun TextWithIcon(
    imageVector: ImageVector,
    text: String,
    modifier: Modifier = Modifier,
    iconSize: Int = STANDARD_ICON_SIZE,
    style: TextStyle = TextStyles.getCardBodyTextStyle(),
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = imageVector,
            modifier = Modifier.size(iconSize.dp),
            contentDescription = null,
        )
        Text(
            text = text,
            modifier = Modifier.padding(start = 8.dp),
            style = style,
        )
    }
}
