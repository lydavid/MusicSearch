package ly.david.ui.settings.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ly.david.ui.common.dialog.MultipleChoiceDialog
import ly.david.ui.core.preview.DefaultPreviews
import ly.david.ui.core.theme.PreviewTheme
import ly.david.ui.core.theme.TextStyles

@Composable
internal fun SettingWithDialogChoices(
    title: String,
    choices: List<String>,
    selectedChoiceIndex: Int,
    onSelectChoiceIndex: (Int) -> Unit = {},
) {
    var showThemeDialog by rememberSaveable { mutableStateOf(false) }

    if (showThemeDialog) {
        MultipleChoiceDialog(
            title = title,
            choices = choices,
            selectedChoiceIndex = selectedChoiceIndex,
            onSelectChoiceIndex = onSelectChoiceIndex,
            onDismiss = { showThemeDialog = false }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                showThemeDialog = true
            }
            .padding(16.dp),
    ) {
        Text(
            text = title,
            style = TextStyles.getCardBodyTextStyle(),
        )

        Text(
            text = choices[selectedChoiceIndex],
            style = TextStyles.getCardBodySubTextStyle(),
        )
    }
}

@DefaultPreviews
@Composable
internal fun PreviewSettingWithDialogChoices() {
    PreviewTheme {
        Surface {
            SettingWithDialogChoices(
                title = "Theme",
                choices = listOf("Light"),
                selectedChoiceIndex = 0,
            )
        }
    }
}
