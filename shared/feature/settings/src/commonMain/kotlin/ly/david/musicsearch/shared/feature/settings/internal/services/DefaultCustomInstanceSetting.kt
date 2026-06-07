package ly.david.musicsearch.shared.feature.settings.internal.services

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
import ly.david.musicsearch.ui.common.theme.TextStyles

@Composable
internal fun DefaultCustomInstanceSetting(
    title: String,
    subtitle: String,
    initialSelectedCustom: Boolean,
    initialTextInputValue: String,
    modifier: Modifier = Modifier.Companion,
    onConfirm: (isCustom: Boolean, url: String) -> Unit = { _, _ -> },
) {
    var showDialog by rememberSaveable { mutableStateOf(false) }

    if (showDialog) {
        DefaultCustomInstancePickerDialog(
            title = title,
            initialSelectedCustom = initialSelectedCustom,
            initialTextInputValue = initialTextInputValue,
            onConfirm = onConfirm,
            onDismiss = { showDialog = false },
        )
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                showDialog = true
            }
            .padding(16.dp),
    ) {
        Text(
            text = title,
            style = TextStyles.getCardBodyTextStyle(),
        )

        Text(
            text = subtitle,
            style = TextStyles.getCardBodySubTextStyle(),
        )
    }
}
