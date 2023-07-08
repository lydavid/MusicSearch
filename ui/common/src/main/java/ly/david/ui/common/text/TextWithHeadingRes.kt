package ly.david.ui.common.text

import androidx.annotation.StringRes
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ly.david.ui.common.R
import ly.david.ui.core.preview.DefaultPreviews
import ly.david.ui.core.theme.PreviewTheme

@Composable
fun TextWithHeadingRes(
    @StringRes headingRes: Int,
    text: String,
    modifier: Modifier = Modifier,
    filterText: String = ""
) {
    val heading = stringResource(id = headingRes)
    if (heading.contains(filterText) || text.contains(filterText)) {
        TextWithHeading(
            heading = heading,
            text = text,
            modifier = modifier
        )
    }
}

@DefaultPreviews
@Composable
private fun Preview() {
    PreviewTheme {
        Surface {
            TextWithHeadingRes(headingRes = R.string.format, text = "Digital Media")
        }
    }
}
