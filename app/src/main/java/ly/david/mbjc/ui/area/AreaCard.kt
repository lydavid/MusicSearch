package ly.david.mbjc.ui.area

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import ly.david.mbjc.data.AreaType
import ly.david.mbjc.data.domain.AreaUiModel
import ly.david.mbjc.data.getLifeSpanForDisplay
import ly.david.mbjc.data.getNameWithDisambiguation
import ly.david.mbjc.ui.common.ClickableListItem
import ly.david.mbjc.ui.common.preview.DefaultPreviews
import ly.david.mbjc.ui.common.toFlagEmoji
import ly.david.mbjc.ui.common.transformThisIfNotNullOrEmpty
import ly.david.mbjc.ui.theme.PreviewTheme
import ly.david.mbjc.ui.theme.TextStyles

@Composable
internal fun AreaCard(
    area: AreaUiModel,
    onAreaClick: AreaUiModel.() -> Unit = {}
) {
    ClickableListItem(
        onClick = { onAreaClick(area) },
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
        ) {
            val type = area.type

            val areaName = if (type == AreaType.COUNTRY) {
                val flags = area.iso_3166_1_codes?.joinToString { it.toFlagEmoji() }
                flags.transformThisIfNotNullOrEmpty { "$it " } + area.getNameWithDisambiguation()
            } else {
                area.getNameWithDisambiguation()
            }

            Text(
                text = areaName,
                style = TextStyles.getCardTitleTextStyle()
            )

            if (!type.isNullOrEmpty()) {
                Text(
                    modifier = Modifier.padding(top = 4.dp),
                    text = type,
                    style = TextStyles.getCardBodyTextStyle(),
                )
            }

            val lifeSpan = area.lifeSpan.getLifeSpanForDisplay()
            if (lifeSpan.isNotEmpty()) {
                Text(
                    modifier = Modifier.padding(top = 4.dp),
                    text = lifeSpan,
                    style = TextStyles.getCardBodySubTextStyle(),
                )
            }
        }
    }
}

// Cannot be private.
internal class AreaCardPreviewParameterProvider : PreviewParameterProvider<AreaUiModel> {
    override val values = sequenceOf(
        AreaUiModel(
            id = "1",
            name = "Area Name",
        ),
        AreaUiModel(
            id = "2",
            name = "Area Name",
            disambiguation = "That one",
        ),
        AreaUiModel(
            id = "3",
            name = "Area Name",
            disambiguation = "That one",
            type = "Country",
            iso_3166_1_codes = listOf("GB")
        ),
    )
}

@DefaultPreviews
@Composable
private fun AreaCardPreview(
    @PreviewParameter(AreaCardPreviewParameterProvider::class) area: AreaUiModel
) {
    PreviewTheme {
        Surface {
            AreaCard(area)
        }
    }
}
