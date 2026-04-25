package ly.david.musicsearch.ui.common.button

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
internal fun CheckboxRowLayout(
    text: String,
    isCheckedLike: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    checkbox: @Composable () -> Unit,
) {
    Row(
        modifier = modifier
            .padding(2.dp)
            .clip(shape = RoundedCornerShape(28.dp))
            .clickable { onClick() }
            .semantics(mergeDescendants = true) { role = Role.Checkbox }
            .padding(horizontal = 14.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        checkbox()
        Text(
            text = text,
            modifier = Modifier.padding(start = 8.dp),
            color = if (isCheckedLike) MaterialTheme.colorScheme.primary else Color.Unspecified,
            fontWeight = if (isCheckedLike) FontWeight.SemiBold else FontWeight.Normal,
        )
    }
}
