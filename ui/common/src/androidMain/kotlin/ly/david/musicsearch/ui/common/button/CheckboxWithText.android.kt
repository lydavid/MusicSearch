package ly.david.musicsearch.ui.common.button

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import ly.david.musicsearch.ui.common.preview.PreviewTheme

@PreviewLightDark
@Composable
internal fun PreviewCheckboxWithTextOff() {
    PreviewTheme {
        Surface {
            CheckboxWithText(
                text = "Checkbox",
                checked = false,
                onClick = {},
            )
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewCheckboxWithTextOn() {
    PreviewTheme {
        Surface {
            CheckboxWithText(
                text = "Checkbox",
                checked = true,
                onClick = {},
            )
        }
    }
}
