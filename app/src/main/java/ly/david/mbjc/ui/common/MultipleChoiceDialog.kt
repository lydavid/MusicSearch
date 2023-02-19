package ly.david.mbjc.ui.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import ly.david.mbjc.ExcludeFromJacocoGeneratedReport
import ly.david.mbjc.R
import ly.david.mbjc.ui.common.preview.DefaultPreviews
import ly.david.mbjc.ui.settings.AppPreferences
import ly.david.mbjc.ui.theme.PreviewTheme
import ly.david.mbjc.ui.theme.TextStyles

@Composable
internal fun MultipleChoiceDialog(
    title: String,
    choices: List<String>,
    selectedChoiceIndex: Int,
    onSelectChoiceIndex: (Int) -> Unit = {},
    onDismiss: () -> Unit = {},
) {
    Dialog(
        onDismissRequest = {
            onDismiss()
        }
    ) {
        Surface(
            shape = RoundedCornerShape(28.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
            ) {
                Text(
                    modifier = Modifier.padding(bottom = 16.dp),
                    text = title,
                    style = TextStyles.getCardTitleTextStyle(),
                )

                Column(modifier = Modifier.selectableGroup()) {
                    choices.forEachIndexed { index, choice ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .selectable(
                                    selected = index == selectedChoiceIndex,
                                    onClick = {
                                        onSelectChoiceIndex(index)
                                        onDismiss()
                                    },
                                    role = Role.RadioButton
                                )
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = index == selectedChoiceIndex,
                                onClick = null,
                                modifier = Modifier.padding(end = 8.dp)
                            )
                            Text(
                                text = choice,
                                style = TextStyles.getCardBodyTextStyle(),
                            )
                        }
                    }
                }

                TextButton(
                    modifier = Modifier
                        .align(Alignment.End),
                    onClick = onDismiss
                ) {
                    Text(
                        text = stringResource(id = R.string.cancel),
                    )
                }
            }
        }
    }
}

@ExcludeFromJacocoGeneratedReport
@DefaultPreviews
@Composable
private fun Preview() {
    PreviewTheme {
        Surface {
            MultipleChoiceDialog(
                title = "Theme",
                choices = AppPreferences.Theme.values().map { stringResource(id = it.textRes) },
                selectedChoiceIndex = 0,
                onSelectChoiceIndex = {}
            )
        }
    }
}
