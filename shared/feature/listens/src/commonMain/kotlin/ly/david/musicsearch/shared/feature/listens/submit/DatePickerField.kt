package ly.david.musicsearch.shared.feature.listens.submit

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import kotlinx.datetime.TimeZone
import ly.david.musicsearch.shared.domain.common.getShortDateFormatted
import ly.david.musicsearch.shared.feature.listens.components.DatePickerDialog
import ly.david.musicsearch.ui.common.icons.CalendarMonth
import ly.david.musicsearch.ui.common.icons.CustomIcons
import ly.david.musicsearch.ui.common.modifier.onKeyboardClick
import musicsearch.ui.common.generated.resources.Res
import musicsearch.ui.common.generated.resources.date
import musicsearch.ui.common.generated.resources.selectDate
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Clock
import kotlin.time.Instant

/**
 * Modified from https://developer.android.com/develop/ui/compose/components/datepickers.
 */
@Composable
internal fun DatePickerField(
    dateTimeEpochSeconds: Long,
    timeZone: TimeZone,
    clock: Clock,
    modifier: Modifier = Modifier,
    onSelectDate: (dateSeconds: Long) -> Unit,
) {
    var showDialog by remember { mutableStateOf(false) }

    val formattedDate = Instant.fromEpochSeconds(dateTimeEpochSeconds).getShortDateFormatted(timeZone = timeZone)
    OutlinedTextField(
        value = formattedDate,
        onValueChange = { },
        label = { Text(stringResource(Res.string.date)) },
        trailingIcon = {
            Icon(imageVector = CustomIcons.CalendarMonth, contentDescription = stringResource(Res.string.selectDate))
        },
        readOnly = true,
        modifier = modifier
            .fillMaxWidth()
            .pointerInput(dateTimeEpochSeconds) {
                awaitEachGesture {
                    awaitFirstDown(pass = PointerEventPass.Initial)
                    val upEvent = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                    if (upEvent != null) {
                        showDialog = true
                    }
                }
            }
            .onKeyboardClick {
                showDialog = true
            },
    )

    if (showDialog) {
        DatePickerDialog(
            dateTimeEpochSeconds = dateTimeEpochSeconds,
            timeZone = timeZone,
            clock = clock,
            onSelectDate = onSelectDate,
            onDismiss = { showDialog = false },
        )
    }
}
