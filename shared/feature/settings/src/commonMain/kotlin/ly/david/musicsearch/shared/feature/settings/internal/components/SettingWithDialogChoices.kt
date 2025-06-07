package ly.david.musicsearch.shared.feature.settings.internal.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import ly.david.musicsearch.ui.common.dialog.MultipleChoiceDialog
import ly.david.musicsearch.ui.common.theme.TextStyles

@Composable
internal fun SettingWithDialogChoices(
    title: String,
    choices: ImmutableList<String>,
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
            onDismiss = { showThemeDialog = false },
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
