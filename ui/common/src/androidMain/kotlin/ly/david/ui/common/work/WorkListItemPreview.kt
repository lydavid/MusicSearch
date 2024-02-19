package ly.david.ui.common.work

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import ly.david.musicsearch.core.models.listitem.WorkListItemModel
import ly.david.ui.core.preview.DefaultPreviews
import ly.david.ui.core.theme.PreviewTheme

@DefaultPreviews
@Composable
private fun Preview() {
    PreviewTheme {
        Surface {
            WorkListItem(
                work = WorkListItemModel(
                    id = "1",
                    name = "work name",
                    disambiguation = "that one",
                    type = "Song",
                ),
            )
        }
    }
}
