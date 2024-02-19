package ly.david.ui.common.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
expect fun ClickableItem(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    endIcon: ImageVector? = null,
    onClick: () -> Unit = {},
)
