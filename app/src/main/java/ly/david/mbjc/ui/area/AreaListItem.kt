package ly.david.mbjc.ui.area

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
import ly.david.data.AreaType.COUNTRY
import ly.david.data.AreaType.WORLDWIDE
import ly.david.data.common.ifNotNull
import ly.david.data.common.ifNotNullOrEmpty
import ly.david.data.common.toFlagEmoji
import ly.david.data.common.transformThisIfNotNullOrEmpty
import ly.david.data.domain.AreaListItemModel
import ly.david.data.getLifeSpanForDisplay
import ly.david.data.showReleases
import ly.david.mbjc.ExcludeFromJacocoGeneratedReport
import ly.david.mbjc.ui.common.listitem.DisambiguationText
import ly.david.mbjc.ui.common.preview.DefaultPreviews
import ly.david.mbjc.ui.theme.PreviewTheme
import ly.david.mbjc.ui.theme.TextStyles

/**
 * Also used for release event cards since their Destination is also an Area.
 */
@Composable
internal fun AreaListItem(
    area: AreaListItemModel,
    modifier: Modifier = Modifier,
    showType: Boolean = true,
    onAreaClick: AreaListItemModel.() -> Unit = {}
) {
    ListItem(
        headlineText = {
            Column {
                // Misnomer here, but it's the same condition to show this tab and to show flags
                val areaName = if (area.showReleases()) {
                    val flags = area.countryCodes?.joinToString { it.toFlagEmoji() }
                    flags.transformThisIfNotNullOrEmpty { "$it " } + area.name
                } else {
                    area.name
                }
                Text(
                    text = areaName,
                    style = TextStyles.getCardTitleTextStyle(),
                )

                DisambiguationText(disambiguation = area.disambiguation)

                val type = area.type
                if (showType && !type.isNullOrEmpty()) {
                    Text(
                        modifier = Modifier.padding(top = 4.dp),
                        text = type,
                        style = TextStyles.getCardBodyTextStyle(),
                    )
                }

                area.lifeSpan.ifNotNull {
                    Text(
                        modifier = Modifier.padding(top = 4.dp),
                        text = it.getLifeSpanForDisplay(),
                        style = TextStyles.getCardBodySubTextStyle(),
                    )
                }
            }
        },
        modifier = modifier.clickable {
            onAreaClick(area)
        },
        trailingContent = {
            area.date.ifNotNullOrEmpty {
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = it,
                        style = TextStyles.getCardBodyTextStyle(),
                    )
                }
            }
        }
    )
}

// Cannot be private.
@ExcludeFromJacocoGeneratedReport
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
            countryCodes = listOf("GB")
        ),
        AreaListItemModel(
            id = "4",
            name = "Area Name with a very long name",
            type = WORLDWIDE,
            countryCodes = listOf("XW")
        )
    )
}

@ExcludeFromJacocoGeneratedReport
@DefaultPreviews
@Composable
private fun Preview(
    @PreviewParameter(AreaListItemPreviewParameterProvider::class) area: AreaListItemModel
) {
    PreviewTheme {
        Surface {
            AreaListItem(area)
        }
    }
}

@ExcludeFromJacocoGeneratedReport
@DefaultPreviews
@Composable
private fun ReleaseEventPreview() {
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
                showType = false
            )
        }
    }
}
