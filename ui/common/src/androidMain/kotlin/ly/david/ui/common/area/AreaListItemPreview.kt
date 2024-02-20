package ly.david.ui.common.area

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ListItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import ly.david.musicsearch.core.models.area.AreaType.COUNTRY
import ly.david.musicsearch.core.models.area.AreaType.WORLDWIDE
import ly.david.musicsearch.core.models.common.ifNotNull
import ly.david.musicsearch.core.models.common.ifNotNullOrEmpty
import ly.david.musicsearch.core.models.common.toFlagEmoji
import ly.david.musicsearch.core.models.common.transformThisIfNotNullOrEmpty
import ly.david.musicsearch.core.models.getLifeSpanForDisplay
import ly.david.musicsearch.core.models.listitem.AreaListItemModel
import ly.david.ui.common.listitem.DisambiguationText
import ly.david.ui.core.preview.DefaultPreviews
import ly.david.ui.core.theme.PreviewTheme
import ly.david.ui.core.theme.TextStyles

// Cannot be private.
internal class AreaListItemPreviewParameterProvider : PreviewParameterProvider<AreaListItemModel> {
    override val values = sequenceOf(
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
}

@DefaultPreviews
@Composable
private fun Preview(
    @PreviewParameter(AreaListItemPreviewParameterProvider::class) area: AreaListItemModel,
) {
    PreviewTheme {
        Surface {
            AreaListItem(area)
        }
    }
}

@DefaultPreviews
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