package ly.david.musicsearch.ui.common.button

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ly.david.musicsearch.ui.common.icons.CustomIcons
import ly.david.musicsearch.ui.common.icons.Refresh
import musicsearch.ui.common.generated.resources.Res
import musicsearch.ui.common.generated.resources.retry
import org.jetbrains.compose.resources.stringResource

@Composable
fun RetryButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    ButtonWithIcon(
        imageVector = CustomIcons.Refresh,
        text = stringResource(Res.string.retry),
        modifier = modifier,
        onClick = onClick,
    )
}
