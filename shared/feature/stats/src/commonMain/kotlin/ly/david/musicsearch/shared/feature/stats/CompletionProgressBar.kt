package ly.david.musicsearch.shared.feature.stats

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ly.david.musicsearch.ui.common.theme.TextStyles

@Composable
internal fun CompletionProgressBar(
    totalCount: Int?,
    currentCount: Int,
    formatProgressText: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
    ) {
        Text(
            style = TextStyles.getCardBodyTextStyle(),
            text = "$formatProgressText $currentCount / ${(totalCount ?: "?")}",
        )

        if (totalCount != null) {
            Surface(
                shape = RoundedCornerShape(8.dp),
            ) {
                LinearProgressIndicator(
                    progress = { currentCount / totalCount.toFloat() },
                    modifier = Modifier
                        .height(8.dp)
                        .fillMaxWidth(),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                )
            }
        }
    }
}
