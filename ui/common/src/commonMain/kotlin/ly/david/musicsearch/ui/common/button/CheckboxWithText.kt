package ly.david.musicsearch.ui.common.button

import androidx.compose.material3.Checkbox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun CheckboxWithText(
    text: String,
    checked: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    CheckboxRowLayout(
        text = text,
        isCheckedLike = checked,
        modifier = modifier,
        onClick = onClick,
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = null,
        )
    }
}
