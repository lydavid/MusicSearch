package ly.david.musicsearch.ui.common.work

import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ly.david.musicsearch.shared.domain.common.ifNotNullOrEmpty
import ly.david.musicsearch.shared.domain.listitem.WorkListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.ui.common.getIcon
import ly.david.musicsearch.ui.common.icon.AddToCollectionIconButton
import ly.david.musicsearch.ui.common.image.ThumbnailImage
import ly.david.musicsearch.ui.common.listitem.listItemColors
import ly.david.musicsearch.ui.common.locale.getAnnotatedName
import ly.david.musicsearch.ui.common.text.fontWeight
import ly.david.musicsearch.ui.common.theme.LocalStrings
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
    val strings = LocalStrings.current

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
                    iswcs.ifNotNullOrEmpty {
                        Text(
                            text = it.joinToString(", "),
                            style = TextStyles.getCardBodySubTextStyle(),
                            fontWeight = work.fontWeight,
                        )
                    }
                    type?.ifNotNullOrEmpty {
                        Text(
                            text = it,
                            style = TextStyles.getCardBodySubTextStyle(),
                            fontWeight = work.fontWeight,
                        )
                    }
                    languages.ifNotNullOrEmpty {
                        Text(
                            text = it.mapNotNull { language ->
                                language.getDisplayLanguage(strings)
                            }.joinToString(", "),
                            style = TextStyles.getCardBodySubTextStyle(),
                            fontWeight = work.fontWeight,
                        )
                    }

                    // TODO: writers
                    //  these come from relations
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
                placeholderIcon = MusicBrainzEntity.WORK.getIcon(),
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
