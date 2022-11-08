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
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import ly.david.data.common.toFlagEmoji
import ly.david.data.common.transformThisIfNotNullOrEmpty
import ly.david.data.domain.AreaUiModel
import ly.david.data.domain.showReleases
import ly.david.data.getLifeSpanForDisplay
import ly.david.mbjc.ExcludeFromJacocoGeneratedReport
import ly.david.mbjc.ui.common.ClickableListItem
import ly.david.mbjc.ui.common.preview.DefaultPreviews
import ly.david.mbjc.ui.theme.PreviewTheme
import ly.david.mbjc.ui.theme.TextStyles
import ly.david.mbjc.ui.theme.getSubTextColor

/**
 * Also used for release event cards since their Destination is also an Area.
 */
@Composable
internal fun AreaCard(
    area: AreaUiModel,
    showType: Boolean = true,
    onAreaClick: AreaUiModel.() -> Unit = {}
) {
    ClickableListItem(
        onClick = { onAreaClick(area) },
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            val (mainSection, endSection) = createRefs()

            Column(
                modifier = Modifier
                    .constrainAs(mainSection) {
                        width = Dimension.fillToConstraints
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(endSection.start)
                    }
            ) {
                // Misnomer here, but it's the same condition to show this tab and to show flags
                val areaName = if (area.showReleases()) {
                    val flags = area.iso_3166_1_codes?.joinToString { it.toFlagEmoji() }
                    flags.transformThisIfNotNullOrEmpty { "$it " } + area.name
                } else {
                    area.name
                }
                Text(
                    text = areaName,
                    style = TextStyles.getCardTitleTextStyle(),
                )

                val disambiguation = area.disambiguation
                if (!disambiguation.isNullOrEmpty()) {
                    Text(
                        modifier = Modifier.padding(top = 4.dp),
                        text = "($disambiguation)",
                        color = getSubTextColor(),
                        style = TextStyles.getCardBodyTextStyle()
                    )
                }

                val type = area.type
                if (showType && !type.isNullOrEmpty()) {
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

            val date = area.date
            if (!date.isNullOrEmpty()) {
                Text(
                    text = date,
                    style = TextStyles.getCardBodyTextStyle(),
                    modifier = Modifier
                        .constrainAs(endSection) {
                            width = Dimension.wrapContent
                            top.linkTo(parent.top)
                            start.linkTo(mainSection.end, margin = 4.dp)
                            end.linkTo(parent.end)
                        }
                )
            }
        }
    }
}

// Cannot be private.
@ExcludeFromJacocoGeneratedReport
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
            name = "Area Name with a very long name",
            disambiguation = "That one",
            type = "Country",
            iso_3166_1_codes = listOf("GB")
        )
    )
}

@ExcludeFromJacocoGeneratedReport
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

@ExcludeFromJacocoGeneratedReport
@DefaultPreviews
@Composable
private fun ReleaseEventPreview() {
    PreviewTheme {
        Surface {
            AreaCard(
                area = AreaUiModel(
                    id = "4",
                    name = "Area Name with a very long name",
                    disambiguation = "That one",
                    iso_3166_1_codes = listOf("KR"),
                    date = "2022-10-29",
                    type = "Country",
                ),
                showType = false
            )
        }
    }
}
