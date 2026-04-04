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
import ly.david.musicsearch.shared.domain.common.ifNotEmpty
import ly.david.musicsearch.shared.domain.common.ifNotNull
import ly.david.musicsearch.shared.domain.common.ifNotNullOrEmpty
import ly.david.musicsearch.shared.domain.listitem.LabelListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.ui.common.getIcon
import ly.david.musicsearch.ui.common.icon.AddToCollectionIconButton
import ly.david.musicsearch.ui.common.image.ThumbnailImage
import ly.david.musicsearch.ui.common.listitem.HighlightableText
import ly.david.musicsearch.ui.common.listitem.listItemColors
import ly.david.musicsearch.ui.common.locale.getAnnotatedName
import ly.david.musicsearch.ui.common.theme.TextStyles
import ly.david.musicsearch.ui.common.theme.getSubTextColor
import musicsearch.ui.common.generated.resources.Res
import musicsearch.ui.common.generated.resources.lc
import org.jetbrains.compose.resources.stringResource

@Composable
fun LabelListItem(
    label: LabelListItemModel,
    filterText: String,
    modifier: Modifier = Modifier,
    showIcon: Boolean = true,
    showEditCollection: Boolean = true,
    onLabelClick: LabelListItemModel.() -> Unit = {},
    isSelected: Boolean = false,
    onSelect: (String) -> Unit = {},
    onEditCollectionClick: (String) -> Unit = {},
) {
    val leadingContent: @Composable (() -> Unit)? =
        if (showIcon) {
            {
                ThumbnailImage(
                    imageMetadata = null,
                    placeholderIcon = MusicBrainzEntityType.LABEL.getIcon(),
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
                HighlightableText(
                    text = label.getAnnotatedName(),
                    highlightedText = filterText,
                    style = TextStyles.getCardBodyTextStyle(),
                )

                label.type.ifNotEmpty {
                    Text(
                        modifier = Modifier.padding(top = 4.dp),
                        text = it,
                        color = getSubTextColor(),
                        style = TextStyles.getCardBodySubTextStyle(),
                    )
                }

                label.labelCode?.ifNotNull { labelCode ->
                    HighlightableText(
                        modifier = Modifier.padding(top = 4.dp),
                        text = stringResource(Res.string.lc, labelCode),
                        highlightedText = filterText,
                        style = TextStyles.getCardBodySubTextStyle(),
                    )
                }

                label.catalogNumbers.ifNotNullOrEmpty {
                    HighlightableText(
                        modifier = Modifier.padding(top = 4.dp),
                        text = it,
                        highlightedText = filterText,
                        style = TextStyles.getCardBodySubTextStyle(),
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
        trailingContent = {
            if (showEditCollection) {
                AddToCollectionIconButton(
                    entityListItemModel = label,
                    onClick = onEditCollectionClick,
                )
            }
        },
    )
}
