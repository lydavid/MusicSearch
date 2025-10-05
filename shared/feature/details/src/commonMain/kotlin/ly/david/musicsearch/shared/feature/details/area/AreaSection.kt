package ly.david.musicsearch.shared.feature.details.area

import androidx.compose.runtime.Composable
import ly.david.musicsearch.shared.domain.listitem.AreaListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.domain.network.MusicBrainzItemClickHandler
import ly.david.musicsearch.ui.common.area.AreaListItem
import ly.david.musicsearch.ui.common.listitem.ListSeparatorHeader
import ly.david.musicsearch.ui.common.theme.LocalStrings

@Composable
internal fun AreaSection(
    areaListItemModel: AreaListItemModel?,
    filterText: String = "",
    onItemClick: MusicBrainzItemClickHandler = { _, _ -> },
) {
    val strings = LocalStrings.current

    areaListItemModel?.run {
        ListSeparatorHeader(text = strings.area)

        if (name.contains(filterText, ignoreCase = true)) {
            AreaListItem(
                area = this,
                showType = false,
                showIcon = false,
                showEditCollection = false,
                onAreaClick = {
                    onItemClick(
                        MusicBrainzEntityType.AREA,
                        id,
                    )
                },
            )
        }
    }
}
