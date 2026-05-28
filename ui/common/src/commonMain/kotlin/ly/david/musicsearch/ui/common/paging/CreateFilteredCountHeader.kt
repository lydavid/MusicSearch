package ly.david.musicsearch.ui.common.paging

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ly.david.musicsearch.shared.domain.list.FilteredCount
import musicsearch.ui.common.generated.resources.Res
import musicsearch.ui.common.generated.resources.showingXOfY
import org.jetbrains.compose.resources.stringResource

@Composable
fun FilteredCount.createFilteredCountHeader(): @Composable (() -> Unit)? {
    val filteredCount = filteredCount
    val totalCount = totalCount
    val showHeader = filteredCount != 0 && totalCount != 0 && filteredCount != totalCount
    val header: @Composable (() -> Unit)? = if (showHeader) {
        {
            Text(
                modifier = Modifier.Companion
                    .padding(16.dp)
                    .fillMaxWidth(),
                style = MaterialTheme.typography.bodyMedium,
                text = stringResource(
                    Res.string.showingXOfY,
                    filteredCount,
                    totalCount,
                ),
            )
        }
    } else {
        null
    }
    return header
}
