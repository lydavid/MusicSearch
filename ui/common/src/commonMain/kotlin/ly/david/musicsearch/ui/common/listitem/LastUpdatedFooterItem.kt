package ly.david.musicsearch.ui.common.listitem

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimePeriod
import kotlinx.datetime.Instant
import ly.david.musicsearch.shared.domain.common.getDateTimeFormatted
import ly.david.musicsearch.shared.domain.common.getDateTimePeriod
import ly.david.musicsearch.ui.core.LocalStrings
import ly.david.musicsearch.ui.core.theme.TextStyles
import ly.david.musicsearch.ui.core.theme.getSubTextColor

@Composable
fun LastUpdatedFooterItem(
    lastUpdated: Instant,
    modifier: Modifier = Modifier,
    now: Instant = Clock.System.now(),
) {
    Column(
        modifier = modifier.padding(16.dp),
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
    modifier: Modifier = Modifier,
    now: Instant = Clock.System.now(),
) {
    val strings = LocalStrings.current
    SelectionContainer(modifier = modifier) {
        val formattedDateTimePeriod = formatLastUpdatedPeriod(lastUpdated.getDateTimePeriod(now = now))
        val formattedDateTime = lastUpdated.getDateTimeFormatted()
        Text(
            text = strings.lastUpdatedFromMusicBrainz(
                formattedDateTimePeriod,
                formattedDateTime,
            ),
            style = TextStyles.getCardBodySubTextStyle(),
        )
    }
}

@Composable
private fun formatLastUpdatedPeriod(period: DateTimePeriod): String {
    val strings = LocalStrings.current
    return when {
        period.years > 1 -> "${period.years} ${strings.yearsAgo}"
        period.years == 1 -> "1 ${strings.yearAgo}"

        period.months > 1 -> "${period.months} ${strings.monthsAgo}"
        period.months == 1 -> "1 ${strings.monthAgo}"

        period.months > 1 -> "${period.months} ${strings.monthsAgo}"
        period.months == 1 -> "1 ${strings.monthAgo}"

        period.days >= THREE_WEEKS_IN_DAYS -> "3 ${strings.weeksAgo}"
        period.days >= TWO_WEEKS_IN_DAYS -> "2 ${strings.weeksAgo}"
        period.days >= ONE_WEEK_IN_DAYS -> "1 ${strings.weekAgo}"

        period.days > 1 -> "${period.days} ${strings.daysAgo}"
        period.days == 1 -> strings.yesterday

        period.hours > 1 -> "${period.hours} ${strings.hoursAgo}"
        period.hours == 1 -> "1 ${strings.hourAgo}"

        period.minutes > 1 -> "${period.minutes} ${strings.minutesAgo}"
        period.minutes == 1 -> "1 ${strings.minuteAgo}"

        period.seconds > 0 -> strings.secondsAgo

        else -> strings.justNow
    }
}

private const val ONE_WEEK_IN_DAYS = 7
private const val TWO_WEEKS_IN_DAYS = ONE_WEEK_IN_DAYS * 2
private const val THREE_WEEKS_IN_DAYS = ONE_WEEK_IN_DAYS * 3
