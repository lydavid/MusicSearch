package ly.david.musicsearch.shared.feature.settings.internal.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.unit.dp
import ly.david.musicsearch.shared.domain.common.ifNotEmpty
import ly.david.musicsearch.ui.common.theme.TextStyles

@Composable
internal fun SettingSwitch(
    header: String,
    checked: Boolean,
    modifier: Modifier = Modifier,
    subtitle: String = "",
    onCheckedChange: (Boolean) -> Unit = {},
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .toggleable(
                value = checked,
                role = Role.Switch,
                onValueChange = onCheckedChange,
            )
            .padding(
                horizontal = 16.dp,
                vertical = 8.dp,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column {
            Text(
                text = header,
                style = TextStyles.getCardBodyTextStyle(),
            )
            subtitle.ifNotEmpty {
                Text(
                    text = it,
                    style = TextStyles.getCardBodySubTextStyle(),
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            modifier = Modifier.clearAndSetSemantics {},
        )
    }
}
