package ly.david.ui.common.area

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import ly.david.musicsearch.core.models.area.AreaType.COUNTRY
import ly.david.musicsearch.core.models.area.AreaType.WORLDWIDE
import ly.david.musicsearch.core.models.listitem.AreaListItemModel
import ly.david.ui.core.theme.PreviewTheme

internal val areas = listOf(
    AreaListItemModel(
        id = "1",
        name = "Area Name",
    ),
    AreaListItemModel(
        id = "2",
        name = "Area Name",
        disambiguation = "That one",
    ),
    AreaListItemModel(
        id = "3",
        name = "Area Name with a very long name",
        disambiguation = "That one",
        type = COUNTRY,
        countryCodes = listOf("GB"),
    ),
    AreaListItemModel(
        id = "4",
        name = "Area Name with a very long name",
        type = WORLDWIDE,
        countryCodes = listOf("XW"),
    ),
)

// Cannot be private.
internal class AreaListItemPreviewParameterProvider : CollectionPreviewParameterProvider<AreaListItemModel>(areas)

@PreviewLightDark
@Composable
internal fun PreviewAreaListItem(
    @PreviewParameter(AreaListItemPreviewParameterProvider::class) area: AreaListItemModel,
) {
    PreviewTheme {
        Surface {
            AreaListItem(area)
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
                    id = "4",
                    name = "Area Name with a very long name",
                    disambiguation = "That one",
                    countryCodes = listOf("KR"),
                    date = "2022-10-29",
                    type = "Country",
                ),
                showType = false,
            )
        }
    }
}
