package ly.david.ui.common.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
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
import ly.david.ui.common.R

import ly.david.ui.core.preview.DefaultPreviews
import ly.david.ui.core.theme.PreviewTheme
import ly.david.ui.core.theme.TextStyles

@Composable
fun MultipleChoiceDialog(
    title: String,
    choices: List<String>,
    selectedChoiceIndex: Int,
    modifier: Modifier = Modifier,
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
                modifier = modifier.padding(24.dp),
            ) {
                Text(
                    modifier = Modifier.padding(bottom = 16.dp),
                    text = title,
                    style = TextStyles.getHeaderTextStyle(),
                )

                Column(modifier = Modifier.selectableGroup()) {
                    choices.forEachIndexed { index, choice ->
                        Row(
                            modifier = Modifier
                                .selectable(
                                    selected = index == selectedChoiceIndex,
                                    onClick = {
                                        onSelectChoiceIndex(index)
                                        onDismiss()
                                    },
                                    role = Role.RadioButton
                                )
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp)
                                .heightIn(min = 48.dp),
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

@DefaultPreviews
@Composable
private fun Preview() {
    PreviewTheme {
        Surface {
            MultipleChoiceDialog(
                title = "Theme",
                choices = listOf(
                    "Light",
                    "Dark",
                    "System"
                ),
                selectedChoiceIndex = 0,
                onSelectChoiceIndex = {}
            )
        }
    }
}
