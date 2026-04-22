package ly.david.musicsearch.ui.common.button

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TriStateCheckbox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

// TODO: generalize with checkbox
@Composable
fun TriStateCheckboxWithText(
    text: String,
    toggleableState: ToggleableState,
    modifier: Modifier = Modifier.Companion,
    onClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .padding(2.dp)
            .clip(shape = RoundedCornerShape(28.dp))
            .clickable {
                onClick()
            }
            .semantics(mergeDescendants = true) {
                role = Role.Checkbox
            }
            .padding(horizontal = 14.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TriStateCheckbox(
            state = toggleableState,
            colors = CheckboxDefaults.colors(
                checkedColor = MaterialTheme.colorScheme.primary,
            ),
            onClick = null,
        )
        val checkedLikeState = toggleableState == ToggleableState.On || toggleableState == ToggleableState.Indeterminate
        Text(
            text = text,
            modifier = Modifier.padding(start = 8.dp),
            color = if (checkedLikeState) MaterialTheme.colorScheme.primary else Color.Unspecified,
            fontWeight = if (checkedLikeState) FontWeight.SemiBold else FontWeight.Normal,
        )
    }
}
