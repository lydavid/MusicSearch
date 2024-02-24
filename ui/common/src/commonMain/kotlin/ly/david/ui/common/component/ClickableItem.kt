package ly.david.ui.common.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import ly.david.ui.core.theme.TextStyles

@Composable
fun ClickableItem(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    endIcon: ImageVector? = null,
    onClick: () -> Unit = {},
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = if (endIcon == null) 0.dp else 24.dp),
        ) {
            Text(
                text = title,
                style = TextStyles.getCardBodyTextStyle(),
            )

            subtitle?.let {
                Text(
                    text = subtitle,
                    style = TextStyles.getCardBodySubTextStyle(),
                )
            }
        }

        endIcon?.let {
            Icon(
                imageVector = endIcon,
                contentDescription = null,
                modifier = Modifier.align(Alignment.CenterEnd),
            )
        }
    }
}
