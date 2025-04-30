package ly.david.musicsearch.ui.common.topappbar

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ly.david.musicsearch.ui.common.icons.CustomIcons
import ly.david.musicsearch.ui.common.icons.Edit
import ly.david.musicsearch.ui.common.icons.ImportContacts

@Composable
fun EditToggle(
    topAppBarEditState: TopAppBarEditState,
    modifier: Modifier = Modifier,
    includeTextSeparately: Boolean = false,
) {
    val text = if (topAppBarEditState.isEditMode) {
        "View"
    } else {
        "Edit"
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
    ) {
        Icon(
            imageVector = if (topAppBarEditState.isEditMode) {
                CustomIcons.ImportContacts
            } else {
                CustomIcons.Edit
            },
            contentDescription = text.takeIf { !includeTextSeparately },
        )
        if (includeTextSeparately) {
            Text(
                text = text,
                modifier = Modifier.padding(start = 8.dp),
            )
        }
    }
}
