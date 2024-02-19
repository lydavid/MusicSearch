package ly.david.ui.common.label

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ly.david.musicsearch.core.models.common.ifNotNull
import ly.david.musicsearch.core.models.common.ifNotNullOrEmpty
import ly.david.musicsearch.core.models.getNameWithDisambiguation
import ly.david.musicsearch.core.models.listitem.LabelListItemModel
import ly.david.musicsearch.strings.LocalStrings
import ly.david.ui.core.theme.TextStyles
import ly.david.ui.core.theme.getSubTextColor

@Composable
fun LabelListItem(
    label: LabelListItemModel,
    modifier: Modifier = Modifier,
    onLabelClick: LabelListItemModel.() -> Unit = {},
) {
    val strings = LocalStrings.current

    ListItem(
        headlineContent = {
            Column {
                Text(
                    text = label.getNameWithDisambiguation(),
                    style = TextStyles.getCardBodyTextStyle(),
                )

                label.type?.ifNotNullOrEmpty {
                    Text(
                        modifier = Modifier.padding(top = 4.dp),
                        text = it,
                        color = getSubTextColor(),
                        style = TextStyles.getCardBodySubTextStyle(),
                    )
                }

                label.labelCode?.ifNotNull {
                    Text(
                        modifier = Modifier.padding(top = 4.dp),
                        text = strings.lc(it),
                        style = TextStyles.getCardBodySubTextStyle(),
                    )
                }

                // TODO: area

                // TODO: lifespan

                label.catalogNumber.ifNotNullOrEmpty {
                    Text(
                        modifier = Modifier.padding(top = 4.dp),
                        text = it,
                        style = TextStyles.getCardBodySubTextStyle(),
                    )
                }
            }
        },
        modifier = modifier.clickable { onLabelClick(label) },
    )
}
