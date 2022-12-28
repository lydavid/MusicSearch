package ly.david.mbjc.ui.settings

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
import ly.david.mbjc.ExcludeFromJacocoGeneratedReport
import ly.david.mbjc.R
import ly.david.mbjc.ui.common.MultipleChoiceDialog
import ly.david.mbjc.ui.common.preview.DefaultPreviews
import ly.david.mbjc.ui.theme.PreviewTheme
import ly.david.mbjc.ui.theme.TextStyles

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
            .padding(horizontal = 16.dp, vertical = 8.dp),
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

@ExcludeFromJacocoGeneratedReport
@DefaultPreviews
@Composable
private fun Preview() {
    PreviewTheme {
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
