package ly.david.musicsearch.shared.feature.listens.submit

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import kotlinx.datetime.TimeZone
import ly.david.musicsearch.ui.common.preview.PreviewTheme

@PreviewLightDark
@Composable
internal fun PreviewTimePickerField() {
    PreviewTheme {
        Surface {
            TimePickerField(
                dateTimeEpochSeconds = 60L,
                timeZone = TimeZone.UTC,
                onSelectTime = {},
            )
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewTimePickerDialog() {
    PreviewTheme {
        Surface {
            TimePickerDialog(
                dateTimeEpochSeconds = 60L * 30,
                timeZone = TimeZone.UTC,
                onDismiss = {},
                onSelectTime = {},
            )
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewTimePickerDialogDaylightSaving() {
    PreviewTheme {
        Surface {
            TimePickerDialog(
                dateTimeEpochSeconds = 1772953200,
                timeZone = TimeZone.of("America/Toronto"),
                onDismiss = {},
                onSelectTime = {},
            )
        }
    }
}
