package ly.david.musicsearch.ui.common.release

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import ly.david.musicsearch.shared.domain.release.ReleaseStatus
import ly.david.musicsearch.ui.common.icons.CustomIcons
import ly.david.musicsearch.ui.common.icons.FilterAlt
import ly.david.musicsearch.ui.common.topappbar.OverflowMenuScope

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OverflowMenuScope.ShowStatusesMenuItem(
    selectedStatuses: Set<ReleaseStatus>,
    onClick: (ReleaseStatus) -> Unit,
    modifier: Modifier = Modifier,
) {
    var showBottomSheet by remember { mutableStateOf(false) }
    if (showBottomSheet) {
        ReleaseStatusesBottomSheet(
            selectedStatuses = selectedStatuses,
            onClick = {
                onClick(it)
            },
            onDismiss = {
                showBottomSheet = false
            },
        )
    }

    DropdownMenuItem(
        text = {
            Text("Filter statuses")
        },
        leadingIcon = {
            Icon(
                imageVector = CustomIcons.FilterAlt,
                contentDescription = null,
            )
        },
        onClick = {
            showBottomSheet = true
        },
        modifier = modifier,
    )
}
