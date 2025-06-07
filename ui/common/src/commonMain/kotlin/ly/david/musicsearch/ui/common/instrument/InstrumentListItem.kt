package ly.david.musicsearch.ui.common.instrument

import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ly.david.musicsearch.shared.domain.common.ifNotNullOrEmpty
import ly.david.musicsearch.shared.domain.listitem.InstrumentListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.ui.common.getIcon
import ly.david.musicsearch.ui.common.image.ThumbnailImage
import ly.david.musicsearch.ui.common.listitem.DisambiguationText
import ly.david.musicsearch.ui.common.listitem.listItemColors
import ly.david.musicsearch.ui.common.text.fontWeight
import ly.david.musicsearch.ui.common.theme.TextStyles

@Composable
fun InstrumentListItem(
    instrument: InstrumentListItemModel,
    modifier: Modifier = Modifier,
    onInstrumentClick: InstrumentListItemModel.() -> Unit = {},
    isSelected: Boolean = false,
    onSelect: (String) -> Unit = {},
) {
    ListItem(
        headlineContent = {
            Column {
                instrument.run {
                    Text(
                        text = name,
                        style = TextStyles.getCardBodyTextStyle(),
                        fontWeight = instrument.fontWeight,
                    )

                    DisambiguationText(
                        disambiguation = disambiguation,
                        fontWeight = instrument.fontWeight,
                    )

                    type.ifNotNullOrEmpty {
                        Text(
                            text = it,
                            modifier = Modifier.padding(top = 4.dp),
                            style = TextStyles.getCardBodySubTextStyle(),
                            fontWeight = instrument.fontWeight,
                        )
                    }

                    description.ifNotNullOrEmpty {
                        Text(
                            modifier = Modifier.padding(top = 4.dp),
                            text = it,
                            style = TextStyles.getCardBodySubTextStyle(),
                            fontWeight = instrument.fontWeight,
                        )
                    }
                }
            }
        },
        modifier = modifier.combinedClickable(
            onClick = { onInstrumentClick(instrument) },
            onLongClick = { onSelect(instrument.id) },
        ),
        colors = listItemColors(isSelected = isSelected),
        leadingContent = {
            ThumbnailImage(
                url = "",
                imageId = null,
                placeholderIcon = MusicBrainzEntity.INSTRUMENT.getIcon(),
                modifier = Modifier
                    .clickable {
                        onSelect(instrument.id)
                    },
                isSelected = isSelected,
            )
        },
    )
}
