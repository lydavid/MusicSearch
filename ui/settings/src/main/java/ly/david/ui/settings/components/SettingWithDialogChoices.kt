package ly.david.ui.settings.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ly.david.ui.common.R
import ly.david.ui.common.dialog.MultipleChoiceDialog
import ly.david.ui.core.preview.DefaultPreviews
import ly.david.ui.core.theme.PreviewTheme
import ly.david.ui.core.theme.TextStyles
import ly.david.ui.settings.AppPreferences

@Composable
internal fun SettingWithDialogChoices(
    titleRes: Int,
    choices: List<String>,
    selectedChoiceIndex: Int,
    onSelectChoiceIndex: (Int) -> Unit,
) {
    var showThemeDialog by rememberSaveable { mutableStateOf(false) }

    if (showThemeDialog) {
        MultipleChoiceDialog(
            title = stringResource(id = titleRes),
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
            text = stringResource(id = titleRes),
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
private fun Preview() {
    PreviewTheme {
        // Preview is intentionally max sized so that we can see the dialog pop up in the middle of the screen
        Surface(modifier = Modifier.fillMaxSize()) {
            var theme by remember { mutableStateOf(AppPreferences.Theme.SYSTEM) }

            SettingWithDialogChoices(
                titleRes = R.string.theme,
                choices = AppPreferences.Theme.values().map { stringResource(id = it.textRes) },
                selectedChoiceIndex = theme.ordinal,
                onSelectChoiceIndex = { theme = AppPreferences.Theme.values()[it] }
            )
        }
    }
}
