package ly.david.musicsearch.ui.common.area

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import ly.david.musicsearch.shared.domain.area.AreaType.COUNTRY
import ly.david.musicsearch.shared.domain.listitem.AreaListItemModel
import ly.david.musicsearch.ui.core.theme.PreviewTheme

@PreviewLightDark
@Composable
internal fun PreviewAreaListItem() {
    PreviewTheme {
        Surface {
            AreaListItem(
                area = AreaListItemModel(
                    id = "1",
                    name = "Area Name",
                ),
                showIcon = false,
            )
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewAreaListItemDisambiguation() {
    PreviewTheme {
        Surface {
            AreaListItem(
                area = AreaListItemModel(
                    id = "2",
                    name = "Area Name",
                    disambiguation = "That one",
                ),
                showIcon = false,
            )
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewAreaListItemCountry() {
    PreviewTheme {
        Surface {
            AreaListItem(
                area = AreaListItemModel(
                    id = "38ce2215-162b-3f3c-af41-34800017e1d8",
                    name = "South Georgia and the South Sandwich Islands",
                    type = COUNTRY,
                    countryCodes = listOf("GS"),
                ),
            )
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewAreaListItemWorldwide() {
    PreviewTheme {
        Surface {
            AreaListItem(
                area = AreaListItemModel(
                    id = "ba484a95-2fa3-4cf6-ab25-ed6da47fe677",
                    name = "[Worldwide]",
                    countryCodes = listOf("XW"),
                ),
            )
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewReleaseEvent() {
    PreviewTheme {
        Surface {
            AreaListItem(
                area = AreaListItemModel(
                    id = "38ce2215-162b-3f3c-af41-34800017e1d8",
                    name = "South Georgia and the South Sandwich Islands",
                    type = COUNTRY,
                    countryCodes = listOf("GS"),
                    date = "2006-03-21",
                ),
                showType = false,
                showIcon = false,
            )
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewReleaseEventNoDate() {
    PreviewTheme {
        Surface {
            AreaListItem(
                area = AreaListItemModel(
                    id = "ba484a95-2fa3-4cf6-ab25-ed6da47fe677",
                    name = "[Worldwide]",
                    countryCodes = listOf("XW"),
                ),
                showType = false,
            )
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewAreaListItemVisited() {
    PreviewTheme {
        Surface {
            AreaListItem(
                area = AreaListItemModel(
                    id = "38ce2215-162b-3f3c-af41-34800017e1d8",
                    name = "South Georgia and the South Sandwich Islands",
                    type = COUNTRY,
                    countryCodes = listOf("GS"),
                    visited = true,
                ),
            )
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewReleaseEventVisited() {
    PreviewTheme {
        Surface {
            AreaListItem(
                area = AreaListItemModel(
                    id = "38ce2215-162b-3f3c-af41-34800017e1d8",
                    name = "South Georgia and the South Sandwich Islands",
                    type = COUNTRY,
                    countryCodes = listOf("GS"),
                    date = "2006-03-21",
                    visited = true,
                ),
                showType = false,
            )
        }
    }
}
