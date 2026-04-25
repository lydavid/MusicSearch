package ly.david.musicsearch.ui.common.button

import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TriStateCheckbox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.state.ToggleableState

@Composable
fun TriStateCheckboxWithText(
    text: String,
    toggleableState: ToggleableState,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    CheckboxRowLayout(
        text = text,
        isCheckedLike = toggleableState != ToggleableState.Off,
        modifier = modifier,
        onClick = onClick,
    ) {
        TriStateCheckbox(
            state = toggleableState,
            colors = CheckboxDefaults.colors(
                checkedColor = MaterialTheme.colorScheme.primary,
            ),
            onClick = null,
        )
    }
}
