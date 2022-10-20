package ly.david.mbjc.ui.work

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ly.david.data.domain.WorkUiModel
import ly.david.data.getNameWithDisambiguation
import ly.david.mbjc.ui.common.ClickableListItem
import ly.david.mbjc.ui.common.preview.DefaultPreviews
import ly.david.mbjc.ui.theme.PreviewTheme

@Composable
internal fun WorkCard(
    workUiModel: WorkUiModel,
    onWorkClick: WorkUiModel.() -> Unit = {}
) {
    ClickableListItem(
        onClick = { onWorkClick(workUiModel) },
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
        ) {
            // TODO: writers, artists, iswc
            Text(text = workUiModel.getNameWithDisambiguation())

            Text(text = workUiModel.language.orEmpty())
            Text(text = workUiModel.type.orEmpty())
        }
    }
}

@DefaultPreviews
@Composable
private fun Preview() {
    PreviewTheme {
        Surface {
            WorkCard(
                workUiModel = WorkUiModel(
                    id = "1",
                    name = "work name",
                    disambiguation = "that one",
                    type = "Song",
                )
            )
        }
    }
}
