package ly.david.ui.common.work

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ListItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import java.util.Locale
import ly.david.musicsearch.data.core.common.ifNotNullOrEmpty
import ly.david.musicsearch.data.core.listitem.WorkListItemModel
import ly.david.ui.common.listitem.DisambiguationText
import ly.david.ui.core.preview.DefaultPreviews
import ly.david.ui.core.theme.PreviewTheme
import ly.david.ui.core.theme.TextStyles

@Composable
fun WorkListItem(
    work: WorkListItemModel,
    modifier: Modifier = Modifier,
    onWorkClick: WorkListItemModel.() -> Unit = {},
) {
    ListItem(
        headlineContent = {
            Text(
                text = work.name,
                style = TextStyles.getCardBodyTextStyle(),
            )
        },
        supportingContent = {
            Column {
                work.run {
                    DisambiguationText(disambiguation = disambiguation)
                    iswcs?.ifNotNullOrEmpty {
                        Text(
                            text = it.joinToString("\n"),
                            style = TextStyles.getCardBodySubTextStyle()
                        )
                    }
                    type?.ifNotNullOrEmpty {
                        Text(
                            text = it,
                            style = TextStyles.getCardBodySubTextStyle()
                        )
                    }
                    language?.ifNotNullOrEmpty {
                        Text(
                            text = Locale(it).displayLanguage,
                            style = TextStyles.getCardBodySubTextStyle()
                        )
                    }

                    // TODO: writers
                    //  these come from relations

                    // TODO: artists
                    //  these are the artists from each recording
                }
            }
        },
        modifier = modifier.clickable { onWorkClick(work) }
    )
}

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
