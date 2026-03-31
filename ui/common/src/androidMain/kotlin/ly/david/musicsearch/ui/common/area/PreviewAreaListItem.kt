package ly.david.musicsearch.ui.common.area

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import kotlinx.collections.immutable.persistentListOf
import ly.david.musicsearch.shared.domain.area.AreaType.COUNTRY
import ly.david.musicsearch.shared.domain.listitem.AreaListItemModel
import ly.david.musicsearch.ui.common.preview.PreviewWithTransitionAndOverlays

@PreviewLightDark
@Composable
internal fun PreviewAreaListItem() {
    PreviewWithTransitionAndOverlays {
        AreaListItem(
            area = AreaListItemModel(
                id = "1",
                name = "Area Name",
            ),
            filterText = "",
            showIcon = false,
        )
    }
}

@PreviewLightDark
@Composable
internal fun PreviewAreaListItemDisambiguation() {
    PreviewWithTransitionAndOverlays {
        AreaListItem(
            area = AreaListItemModel(
                id = "2",
                name = "Area Name",
                disambiguation = "That one",
            ),
            filterText = "",
            showIcon = false,
        )
    }
}

@PreviewLightDark
@Composable
internal fun PreviewAreaListItemCountry() {
    PreviewWithTransitionAndOverlays {
        AreaListItem(
            area = AreaListItemModel(
                id = "38ce2215-162b-3f3c-af41-34800017e1d8",
                name = "South Georgia and the South Sandwich Islands",
                type = COUNTRY,
                countryCodes = persistentListOf("GS"),
            ),
            filterText = "south",
        )
    }
}

@PreviewLightDark
@Composable
internal fun PreviewAreaListItemWorldwide() {
    PreviewWithTransitionAndOverlays {
        AreaListItem(
            area = AreaListItemModel(
                id = "ba484a95-2fa3-4cf6-ab25-ed6da47fe677",
                name = "[Worldwide]",
                countryCodes = persistentListOf("XW"),
            ),
            filterText = "",
        )
    }
}

@PreviewLightDark
@Composable
internal fun PreviewReleaseEvent() {
    PreviewWithTransitionAndOverlays {
        AreaListItem(
            area = AreaListItemModel(
                id = "38ce2215-162b-3f3c-af41-34800017e1d8",
                name = "South Georgia and the South Sandwich Islands",
                type = COUNTRY,
                countryCodes = persistentListOf("GS"),
                date = "2006-03-21",
            ),
            filterText = "200",
            showType = false,
            showIcon = false,
            showEditCollection = false,
        )
    }
}

@PreviewLightDark
@Composable
internal fun PreviewReleaseEventNoDate() {
    PreviewWithTransitionAndOverlays {
        AreaListItem(
            area = AreaListItemModel(
                id = "ba484a95-2fa3-4cf6-ab25-ed6da47fe677",
                name = "[Worldwide]",
                countryCodes = persistentListOf("XW"),
            ),
            filterText = "",
            showType = false,
        )
    }
}

@PreviewLightDark
@Composable
internal fun PreviewAreaListItemVisited() {
    PreviewWithTransitionAndOverlays {
        AreaListItem(
            area = AreaListItemModel(
                id = "38ce2215-162b-3f3c-af41-34800017e1d8",
                name = "South Georgia and the South Sandwich Islands",
                type = COUNTRY,
                countryCodes = persistentListOf("GS"),
                visited = true,
            ),
            filterText = "",
        )
    }
}

@PreviewLightDark
@Composable
internal fun PreviewReleaseEventVisited() {
    PreviewWithTransitionAndOverlays {
        AreaListItem(
            area = AreaListItemModel(
                id = "38ce2215-162b-3f3c-af41-34800017e1d8",
                name = "South Georgia and the South Sandwich Islands",
                type = COUNTRY,
                countryCodes = persistentListOf("GS"),
                date = "2006-03-21",
                visited = true,
            ),
            filterText = "",
            showType = false,
            showEditCollection = false,
        )
    }
}
