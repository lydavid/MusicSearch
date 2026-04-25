package ly.david.musicsearch.ui.common.button

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.tooling.preview.PreviewLightDark
import ly.david.musicsearch.ui.common.preview.PreviewTheme

@PreviewLightDark
@Composable
internal fun PreviewTriStateCheckboxWithTextOff() {
    PreviewTheme {
        Surface {
            TriStateCheckboxWithText(
                text = "Tri-State Checkbox",
                toggleableState = ToggleableState.Off,
                onClick = {},
            )
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewTriStateCheckboxWithTextIndeterminate() {
    PreviewTheme {
        Surface {
            TriStateCheckboxWithText(
                text = "Tri-State Checkbox",
                toggleableState = ToggleableState.Indeterminate,
                onClick = {},
            )
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewTriStateCheckboxWithTextOn() {
    PreviewTheme {
        Surface {
            TriStateCheckboxWithText(
                text = "Tri-State Checkbox",
                toggleableState = ToggleableState.On,
                onClick = {},
            )
        }
    }
}
