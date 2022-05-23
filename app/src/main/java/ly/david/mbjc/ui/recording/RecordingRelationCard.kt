package ly.david.mbjc.ui.recording

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ly.david.mbjc.ui.common.ClickableListItem
import ly.david.mbjc.ui.theme.PreviewTheme
import ly.david.mbjc.ui.theme.TextStyles
import ly.david.mbjc.ui.theme.getSubTextColor

@Composable
internal fun RecordingRelationCard(
    label: String,
    name: String,
    disambiguation: String? = null,
    attributes: String? = null,
    onClick: () -> Unit = {}
) {
    ClickableListItem(onClick = onClick) {
        Column(modifier = Modifier.padding(vertical = 16.dp)) {
            Text(
                text = "$label:",
                style = TextStyles.getCardBodyTextStyle()
            )
            Text(
                text = name,
                style = TextStyles.getCardTitleTextStyle()
            )

            if (!disambiguation.isNullOrEmpty()) {
                Text(
                    text = "($disambiguation)",
                    style = TextStyles.getCardBodyTextStyle(),
                    color = getSubTextColor()
                )
            }

            if (!attributes.isNullOrEmpty()) {
                Text(
                    text = "($attributes)",
                    style = TextStyles.getCardBodyTextStyle(),
                )
            }
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
internal fun RecordingRelationCardPreview() {
    PreviewTheme {
        Surface {
            RecordingRelationCard(
                label = "miscellaneous support",
                name = "Artist Name",
                disambiguation = "that guy",
                attributes = "task: director & organizer, strings"
            )
        }
    }
}
