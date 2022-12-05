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
import ly.david.mbjc.ui.common.ClickableListItem
import ly.david.mbjc.ui.common.preview.DefaultPreviews
import ly.david.mbjc.ui.theme.PreviewTheme
import ly.david.mbjc.ui.theme.TextStyles
import ly.david.mbjc.ui.theme.getSubTextColor

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
            Text(
                text = work.name,
                style = TextStyles.getCardTitleTextStyle(),
            )
            work.disambiguation.ifNotNullOrEmpty {
                Text(
                    text = "($it)",
                    style = TextStyles.getCardBodyTextStyle(),
                    color = getSubTextColor()
                )
            }
            work.iswcs?.ifNotNullOrEmpty {
                Text(
                    text = it.joinToString("\n"),
                    style = TextStyles.getCardBodySubTextStyle()
                )
            }
            work.type?.let {
                Text(
                    text = it,
                    style = TextStyles.getCardBodyTextStyle()
                )
            }
            work.language?.ifNotNullOrEmpty {
                Text(
                    text = Locale(it).displayLanguage,
                    style = TextStyles.getCardBodyTextStyle()
                )
            }

            // TODO: writers, artists

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
