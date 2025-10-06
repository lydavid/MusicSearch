package ly.david.musicsearch.ui.common.text

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.semantics
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
    iconTint: Color = LocalContentColor.current,
    contentDescription: String? = null,
    textStyle: TextStyle = TextStyles.getCardBodyTextStyle(),
) {
    Row(
        modifier = modifier.semantics(mergeDescendants = true) {},
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = imageVector,
            modifier = Modifier.size(iconSize.dp),
            tint = iconTint,
            contentDescription = contentDescription,
        )
        Text(
            text = text,
            modifier = Modifier.padding(start = 4.dp),
            style = textStyle,
        )
    }
}
