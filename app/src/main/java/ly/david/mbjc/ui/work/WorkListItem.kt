package ly.david.mbjc.ui.work

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ListItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import java.util.Locale
import ly.david.data.common.ifNotNullOrEmpty
import ly.david.data.domain.WorkListItemModel
import ly.david.mbjc.ExcludeFromJacocoGeneratedReport
import ly.david.ui.common.listitem.DisambiguationText
import ly.david.ui.common.preview.DefaultPreviews
import ly.david.ui.common.theme.PreviewTheme
import ly.david.ui.common.theme.TextStyles

@Composable
internal fun WorkListItem(
    work: WorkListItemModel,
    modifier: Modifier = Modifier,
    onWorkClick: WorkListItemModel.() -> Unit = {}
) {
    ListItem(
        headlineContent = {
            Column {
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
        },
        modifier = modifier.clickable{ onWorkClick(work) }
    )
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
