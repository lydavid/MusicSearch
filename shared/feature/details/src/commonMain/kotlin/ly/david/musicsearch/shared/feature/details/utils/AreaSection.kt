package ly.david.musicsearch.shared.feature.details.utils

import androidx.compose.foundation.lazy.LazyListScope
import ly.david.musicsearch.shared.domain.listitem.AreaListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.domain.network.MusicBrainzItemClickHandler
import ly.david.musicsearch.ui.common.area.AreaListItem
import ly.david.musicsearch.ui.common.listitem.CollapsibleListSeparatorHeader
import ly.david.musicsearch.ui.common.locale.getAnnotatedName
import musicsearch.ui.common.generated.resources.Res
import musicsearch.ui.common.generated.resources.area
import org.jetbrains.compose.resources.stringResource

internal fun LazyListScope.areaSection(
    areaListItemModel: AreaListItemModel?,
    filterText: String = "",
    onItemClick: MusicBrainzItemClickHandler = { _, _ -> },
    collapsed: Boolean = false,
    onCollapseExpand: () -> Unit = {},
) {
    areaListItemModel?.run {
        stickyHeader {
            CollapsibleListSeparatorHeader(
                text = stringResource(Res.string.area),
                collapsed = collapsed,
                onClick = onCollapseExpand,
            )
        }

        item {
            if (getAnnotatedName().contains(filterText, ignoreCase = true) && !collapsed) {
                AreaListItem(
                    area = this@run,
                    filterText = filterText,
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
}
