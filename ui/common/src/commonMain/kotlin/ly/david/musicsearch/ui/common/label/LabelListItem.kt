package ly.david.musicsearch.ui.common.label

import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ly.david.musicsearch.shared.domain.common.ifNotNull
import ly.david.musicsearch.shared.domain.common.ifNotNullOrEmpty
import ly.david.musicsearch.shared.domain.getNameWithDisambiguation
import ly.david.musicsearch.shared.domain.listitem.LabelListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.ui.common.getIcon
import ly.david.musicsearch.ui.common.image.ThumbnailImage
import ly.david.musicsearch.ui.common.listitem.listItemColors
import ly.david.musicsearch.ui.common.text.fontWeight
import ly.david.musicsearch.ui.core.LocalStrings
import ly.david.musicsearch.ui.core.theme.TextStyles
import ly.david.musicsearch.ui.core.theme.getSubTextColor

@Composable
fun LabelListItem(
    label: LabelListItemModel,
    modifier: Modifier = Modifier,
    showIcon: Boolean = true,
    onLabelClick: LabelListItemModel.() -> Unit = {},
    isSelected: Boolean = false,
    onSelect: (String) -> Unit = {},
) {
    val strings = LocalStrings.current

    val leadingContent: @Composable (() -> Unit)? =
        if (showIcon) {
            {
                ThumbnailImage(
                    url = "",
                    placeholderKey = null,
                    placeholderIcon = MusicBrainzEntity.LABEL.getIcon(),
                    modifier = Modifier
                        .clickable {
                            onSelect(label.id)
                        },
                    isSelected = isSelected,
                )
            }
        } else {
            null
        }

    ListItem(
        headlineContent = {
            Column {
                Text(
                    text = label.getNameWithDisambiguation(),
                    style = TextStyles.getCardBodyTextStyle(),
                    fontWeight = label.fontWeight,
                )

                label.type?.ifNotNullOrEmpty {
                    Text(
                        modifier = Modifier.padding(top = 4.dp),
                        text = it,
                        color = getSubTextColor(),
                        style = TextStyles.getCardBodySubTextStyle(),
                        fontWeight = label.fontWeight,
                    )
                }

                label.labelCode?.ifNotNull {
                    Text(
                        modifier = Modifier.padding(top = 4.dp),
                        text = strings.lc(it),
                        style = TextStyles.getCardBodySubTextStyle(),
                        fontWeight = label.fontWeight,
                    )
                }

                label.catalogNumbers.ifNotNullOrEmpty {
                    Text(
                        modifier = Modifier.padding(top = 4.dp),
                        text = it,
                        style = TextStyles.getCardBodySubTextStyle(),
                        fontWeight = label.fontWeight,
                    )
                }
            }
        },
        modifier = modifier.combinedClickable(
            onClick = { onLabelClick(label) },
            onLongClick = { onSelect(label.id) },
        ),
        colors = listItemColors(isSelected = isSelected),
        leadingContent = leadingContent,
    )
}
