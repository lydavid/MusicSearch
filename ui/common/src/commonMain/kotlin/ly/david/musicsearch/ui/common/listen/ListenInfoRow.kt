package ly.david.musicsearch.ui.common.listen

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ly.david.musicsearch.shared.domain.DOT_SEPARATOR
import ly.david.musicsearch.shared.domain.common.getDateTimePeriod
import ly.david.musicsearch.shared.domain.listen.ListenInfo
import ly.david.musicsearch.ui.common.icons.CustomIcons
import ly.david.musicsearch.ui.common.icons.Headphones
import ly.david.musicsearch.ui.common.listitem.formatPeriod
import ly.david.musicsearch.ui.common.text.TextWithIcon
import ly.david.musicsearch.ui.common.theme.TINY_ICON_SIZE
import ly.david.musicsearch.ui.common.theme.TextStyles
import musicsearch.ui.common.generated.resources.Res
import musicsearch.ui.common.generated.resources.lastListenedXAgo
import musicsearch.ui.common.generated.resources.xListens
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Instant

@Composable
fun ListenInfoRow(
    listenInfo: ListenInfo,
    now: Instant,
    showLastListenedPeriod: Boolean,
) {
    ListenInfoRow(
        listenInfo = listenInfo,
        now = now,
        showLastListenedPeriod = showLastListenedPeriod,
        mostListenedCount = null,
    )
}

@Composable
fun ListenInfoRow(
    listenInfo: ListenInfo,
    now: Instant,
    showLastListenedPeriod: Boolean,
    mostListenedCount: Long? = null,
) {
    val listenCount = listenInfo.listenCount
    val listenCountOrNull = listenCount?.toInt()
    val formattedLastListenedPeriod = listenInfo.lastListenedAtMs?.let {
        formatPeriod(
            Instant.fromEpochMilliseconds(it).getDateTimePeriod(now = now),
        )
    }.takeIf { showLastListenedPeriod }
    if (listenCountOrNull != null) {
        TextWithIcon(
            modifier = Modifier.padding(top = 4.dp),
            imageVector = CustomIcons.Headphones,
            text = buildString {
                append(listenCount.toString())
                formattedLastListenedPeriod?.let {
                    append(DOT_SEPARATOR)
                    append(it)
                }
            },
            mergedContentDescription = buildString {
                append(pluralStringResource(Res.plurals.xListens, listenCountOrNull, listenCountOrNull))
                formattedLastListenedPeriod?.let {
                    append("\n")
                    append(stringResource(Res.string.lastListenedXAgo, it))
                }
            },
            iconSize = TINY_ICON_SIZE,
            textStyle = TextStyles.getCardBodySubTextStyle(),
        )
        if (mostListenedCount != null) {
            LinearProgressIndicator(
                progress = { listenCount / maxOf(mostListenedCount, 1).toFloat() },
                modifier = Modifier
                    .padding(top = 2.dp)
                    .height(4.dp)
                    .fillMaxWidth(),
                color = MaterialTheme.colorScheme.primary,
                trackColor = Color.Transparent,
            )
        }
    }
}
