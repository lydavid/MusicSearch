package ly.david.ui.common.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
actual fun ClickableItem(
    title: String,
    modifier: Modifier,
    subtitle: String?,
    endIcon: ImageVector?,
    onClick: () -> Unit,
) {
    // TODO: implement desktop clickable item
}
