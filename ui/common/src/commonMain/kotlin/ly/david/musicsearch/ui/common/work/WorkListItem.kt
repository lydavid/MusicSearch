package ly.david.musicsearch.ui.common.work

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
import ly.david.musicsearch.shared.domain.listitem.WorkListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.ui.common.getIcon
import ly.david.musicsearch.ui.common.icon.AddToCollectionIconButton
import ly.david.musicsearch.ui.common.icons.CustomIcons
import ly.david.musicsearch.ui.common.icons.Headphones
import ly.david.musicsearch.ui.common.image.ThumbnailImage
import ly.david.musicsearch.ui.common.listitem.listItemColors
import ly.david.musicsearch.ui.common.locale.getAnnotatedName
import ly.david.musicsearch.ui.common.text.TextWithIcon
import ly.david.musicsearch.ui.common.text.fontWeight
import ly.david.musicsearch.ui.common.theme.TINY_ICON_SIZE
import ly.david.musicsearch.ui.common.theme.TextStyles

@Composable
fun WorkListItem(
    work: WorkListItemModel,
    modifier: Modifier = Modifier,
    onWorkClick: WorkListItemModel.() -> Unit = {},
    isSelected: Boolean = false,
    onSelect: (String) -> Unit = {},
    onEditCollectionClick: (String) -> Unit = {},
) {
    ListItem(
        headlineContent = {
            Text(
                text = work.getAnnotatedName(),
                style = TextStyles.getCardBodyTextStyle(),
                fontWeight = work.fontWeight,
            )
        },
        supportingContent = {
            Column {
                work.run {
                    iswcs.ifNotEmpty {
                        Text(
                            text = it.joinToString(", "),
                            style = TextStyles.getCardBodySubTextStyle(),
                            fontWeight = work.fontWeight,
                        )
                    }
                    type.ifNotEmpty {
                        Text(
                            text = it,
                            style = TextStyles.getCardBodySubTextStyle(),
                            fontWeight = work.fontWeight,
                        )
                    }
                    languages.ifNotEmpty {
                        Text(
                            text = it.mapNotNull { language ->
                                language.getDisplayLanguage()
                            }.joinToString(", "),
                            style = TextStyles.getCardBodySubTextStyle(),
                            fontWeight = work.fontWeight,
                        )
                    }

                    // TODO: writers
                    //  these come from relations

                    when (val state = listenState) {
                        WorkListItemModel.ListenState.Hide -> {
                            // Do nothing
                        }

                        is WorkListItemModel.ListenState.Known,
                        is WorkListItemModel.ListenState.Unknown,
                        -> {
                            val hasListens = state is WorkListItemModel.ListenState.Known
                            TextWithIcon(
                                modifier = Modifier.padding(top = 4.dp),
                                imageVector = CustomIcons.Headphones,
                                iconSize = TINY_ICON_SIZE,
                                text = if (hasListens) state.listenCount.toString() else "?",
                                textStyle = TextStyles.getCardBodySubTextStyle(),
                            )
                        }
                    }
                }
            }
        },
        modifier = modifier.combinedClickable(
            onClick = { onWorkClick(work) },
            onLongClick = { onSelect(work.id) },
        ),
        colors = listItemColors(isSelected = isSelected),
        leadingContent = {
            ThumbnailImage(
                url = "",
                imageId = null,
                placeholderIcon = MusicBrainzEntityType.WORK.getIcon(),
                modifier = Modifier
                    .clickable {
                        onSelect(work.id)
                    },
                isSelected = isSelected,
            )
        },
        trailingContent = {
            AddToCollectionIconButton(
                entityListItemModel = work,
                onClick = onEditCollectionClick,
            )
        },
    )
}
