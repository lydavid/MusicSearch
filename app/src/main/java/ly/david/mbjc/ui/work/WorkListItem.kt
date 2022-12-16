package ly.david.mbjc.ui.work

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.util.Locale
import ly.david.data.common.ifNotNullOrEmpty
import ly.david.data.domain.WorkListItemModel
import ly.david.mbjc.ExcludeFromJacocoGeneratedReport
import ly.david.mbjc.ui.common.listitem.ClickableListItem
import ly.david.mbjc.ui.common.listitem.DisambiguationText
import ly.david.mbjc.ui.common.preview.DefaultPreviews
import ly.david.mbjc.ui.theme.PreviewTheme
import ly.david.mbjc.ui.theme.TextStyles

@Composable
internal fun WorkListItem(
    work: WorkListItemModel,
    onWorkClick: WorkListItemModel.() -> Unit = {}
) {
    ClickableListItem(
        onClick = { onWorkClick(work) },
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
        ) {
            work.run {
                Text(
                    text = name,
                    style = TextStyles.getCardTitleTextStyle(),
                )
                DisambiguationText(disambiguation = disambiguation)
                iswcs?.ifNotNullOrEmpty {
                    Text(
                        text = it.joinToString("\n"),
                        style = TextStyles.getCardBodySubTextStyle()
                    )
                }
                type?.let {
                    Text(
                        text = it,
                        style = TextStyles.getCardBodyTextStyle()
                    )
                }
                language?.ifNotNullOrEmpty {
                    Text(
                        text = Locale(it).displayLanguage,
                        style = TextStyles.getCardBodyTextStyle()
                    )
                }

                // TODO: writers
                //  these come from relations

                // TODO: artists
                //  these are the artists from each recording
            }
        }
    }
}

@ExcludeFromJacocoGeneratedReport
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
                )
            )
        }
    }
}
