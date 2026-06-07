package ly.david.musicsearch.shared.feature.settings.internal.services

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import ly.david.musicsearch.ui.common.dialog.BasicDialog
import ly.david.musicsearch.ui.common.text.SingleLineTextField
import ly.david.musicsearch.ui.common.theme.TextStyles
import musicsearch.ui.common.generated.resources.Res
import musicsearch.ui.common.generated.resources.custom
import musicsearch.ui.common.generated.resources.default
import musicsearch.ui.common.generated.resources.ok
import musicsearch.ui.common.generated.resources.url
import org.jetbrains.compose.resources.stringResource

@Composable
fun DefaultCustomInstancePickerDialog(
    title: String,
    initialSelectedCustom: Boolean,
    initialTextInputValue: String,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit = {},
    onConfirm: (isCustom: Boolean, url: String) -> Unit = { _, _ -> },
) {
    var selectedCustom by rememberSaveable { mutableStateOf(initialSelectedCustom) }
    var textInputValue by rememberSaveable { mutableStateOf(initialTextInputValue) }

    BasicDialog(onDismiss = onDismiss) {
        Column(modifier = modifier.padding(24.dp)) {
            Text(
                modifier = Modifier.padding(bottom = 16.dp),
                text = title,
                style = TextStyles.getHeaderTextStyle(),
            )

            Column(modifier = Modifier.selectableGroup()) {
                listOf(
                    false to stringResource(Res.string.default),
                    true to stringResource(Res.string.custom),
                ).forEach { (isCustom, label) ->
                    Row(
                        modifier = Modifier
                            .selectable(
                                selected = isCustom == selectedCustom,
                                onClick = { selectedCustom = isCustom },
                                role = Role.RadioButton,
                            )
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp)
                            .heightIn(min = 48.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        RadioButton(
                            selected = isCustom == selectedCustom,
                            onClick = null,
                            modifier = Modifier.padding(end = 8.dp),
                        )
                        Text(text = label, style = TextStyles.getCardBodyTextStyle())
                    }
                }
            }

            if (selectedCustom) {
                SingleLineTextField(
                    textLabel = stringResource(Res.string.url),
                    text = textInputValue,
                    textHint = "",
                    onTextChange = { textInputValue = it },
                )
            }

            TextButton(
                modifier = Modifier.align(Alignment.End),
                onClick = {
                    onConfirm(selectedCustom, textInputValue)
                    onDismiss()
                },
            ) {
                Text(stringResource(Res.string.ok))
            }
        }
    }
}
