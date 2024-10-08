package ly.david.musicsearch.ui.common.work

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ly.david.musicsearch.shared.domain.common.ifNotNullOrEmpty
import ly.david.musicsearch.shared.domain.listitem.WorkListItemModel
import ly.david.musicsearch.ui.common.listitem.DisambiguationText
import ly.david.musicsearch.ui.core.LocalStrings
import ly.david.musicsearch.ui.core.theme.TextStyles

@Composable
fun WorkListItem(
    work: WorkListItemModel,
    modifier: Modifier = Modifier,
    onWorkClick: WorkListItemModel.() -> Unit = {},
) {
    val strings = LocalStrings.current

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
                            style = TextStyles.getCardBodySubTextStyle(),
                        )
                    }
                    type?.ifNotNullOrEmpty {
                        Text(
                            text = it,
                            style = TextStyles.getCardBodySubTextStyle(),
                        )
                    }
                    language?.getDisplayLanguage(strings).ifNotNullOrEmpty {
                        Text(
                            text = it,
                            style = TextStyles.getCardBodySubTextStyle(),
                        )
                    }

                    // TODO: writers
                    //  these come from relations

                    // TODO: artists
                    //  these are the artists from each recording
                }
            }
        },
        modifier = modifier.clickable { onWorkClick(work) },
    )
}
