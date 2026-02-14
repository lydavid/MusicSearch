package ly.david.musicsearch.ui.common.listitem

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.datetime.DateTimePeriod
import ly.david.musicsearch.shared.domain.common.getDateTimeFormatted
import ly.david.musicsearch.shared.domain.common.getDateTimePeriod
import ly.david.musicsearch.ui.common.theme.TextStyles
import ly.david.musicsearch.ui.common.theme.getSubTextColor
import musicsearch.ui.common.generated.resources.Res
import musicsearch.ui.common.generated.resources.daysAgo
import musicsearch.ui.common.generated.resources.hourAgo
import musicsearch.ui.common.generated.resources.hoursAgo
import musicsearch.ui.common.generated.resources.justNow
import musicsearch.ui.common.generated.resources.lastUpdatedFromMusicBrainz
import musicsearch.ui.common.generated.resources.minuteAgo
import musicsearch.ui.common.generated.resources.minutesAgo
import musicsearch.ui.common.generated.resources.monthAgo
import musicsearch.ui.common.generated.resources.monthsAgo
import musicsearch.ui.common.generated.resources.secondsAgo
import musicsearch.ui.common.generated.resources.weekAgo
import musicsearch.ui.common.generated.resources.weeksAgo
import musicsearch.ui.common.generated.resources.yearAgo
import musicsearch.ui.common.generated.resources.yearsAgo
import musicsearch.ui.common.generated.resources.yesterday
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Instant

@Composable
fun LastUpdatedFooterItem(
    lastUpdated: Instant,
    now: Instant,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
    ) {
        HorizontalDivider(
            modifier = Modifier
                .padding(bottom = 16.dp)
                .width(64.dp)
                .align(CenterHorizontally),
            color = getSubTextColor(),
        )

        LastUpdatedText(
            lastUpdated = lastUpdated,
            now = now,
        )
    }
}

@Composable
fun LastUpdatedText(
    lastUpdated: Instant,
    now: Instant,
    modifier: Modifier = Modifier,
) {
    SelectionContainer(modifier = modifier) {
        val formattedDateTimePeriod = formatPeriod(lastUpdated.getDateTimePeriod(now = now))
        val formattedDateTime = lastUpdated.getDateTimeFormatted()
        Text(
            text = stringResource(
                Res.string.lastUpdatedFromMusicBrainz,
                formattedDateTimePeriod,
                formattedDateTime,
            ),
            style = TextStyles.getCardBodySubTextStyle(),
        )
    }
}

@Composable
fun formatPeriod(period: DateTimePeriod): String {
    return when {
        period.years > 1 -> "${period.years} ${stringResource(Res.string.yearsAgo)}"
        period.years == 1 -> "1 ${stringResource(Res.string.yearAgo)}"

        period.months > 1 -> "${period.months} ${stringResource(Res.string.monthsAgo)}"
        period.months == 1 -> "1 ${stringResource(Res.string.monthAgo)}"

        period.months > 1 -> "${period.months} ${stringResource(Res.string.monthsAgo)}"
        period.months == 1 -> "1 ${stringResource(Res.string.monthAgo)}"

        period.days >= THREE_WEEKS_IN_DAYS -> "3 ${stringResource(Res.string.weeksAgo)}"
        period.days >= TWO_WEEKS_IN_DAYS -> "2 ${stringResource(Res.string.weeksAgo)}"
        period.days >= ONE_WEEK_IN_DAYS -> "1 ${stringResource(Res.string.weekAgo)}"

        period.days > 1 -> "${period.days} ${stringResource(Res.string.daysAgo)}"
        period.days == 1 -> stringResource(Res.string.yesterday)

        period.hours > 1 -> "${period.hours} ${stringResource(Res.string.hoursAgo)}"
        period.hours == 1 -> "1 ${stringResource(Res.string.hourAgo)}"

        period.minutes > 1 -> "${period.minutes} ${stringResource(Res.string.minutesAgo)}"
        period.minutes == 1 -> "1 ${stringResource(Res.string.minuteAgo)}"

        period.seconds > 0 -> stringResource(Res.string.secondsAgo)

        else -> stringResource(Res.string.justNow)
    }
}

private const val ONE_WEEK_IN_DAYS = 7
private const val TWO_WEEKS_IN_DAYS = ONE_WEEK_IN_DAYS * 2
private const val THREE_WEEKS_IN_DAYS = ONE_WEEK_IN_DAYS * 3
