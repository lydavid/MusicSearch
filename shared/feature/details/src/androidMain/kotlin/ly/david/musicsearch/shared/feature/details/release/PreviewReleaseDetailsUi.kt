package ly.david.musicsearch.shared.feature.details.release

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import ly.david.musicsearch.core.models.area.AreaType.COUNTRY
import ly.david.musicsearch.core.models.area.AreaType.WORLDWIDE
import ly.david.musicsearch.core.models.listitem.AreaListItemModel
import ly.david.musicsearch.core.models.listitem.LabelListItemModel
import ly.david.musicsearch.core.models.release.ReleaseScaffoldModel
import ly.david.musicsearch.core.models.release.TextRepresentationUiModel
import ly.david.ui.core.theme.PreviewTheme

@PreviewLightDark
@Composable
internal fun PreviewReleaseDetailsUi() {
    PreviewTheme {
        Surface {
            ReleaseDetailsUi(
                release = ReleaseScaffoldModel(
                    id = "r1",
                    name = "Release",
                    date = "1000-10-10",
                    barcode = "123456789",
                    status = "Official",
                    countryCode = "CA",
                    packaging = "Box",
                    asin = "B12341234",
                    quality = "normal",
                    textRepresentation = TextRepresentationUiModel(
                        script = "Latn",
                        language = "eng",
                    ),
                    formattedFormats = "2xCD + Blu-ray",
                    formattedTracks = "15 + 8 + 24",
                    areas = listOf(
                        AreaListItemModel(
                            id = "a1",
                            name = "Canada",
                            type = COUNTRY,
                            countryCodes = listOf("CA"),
                            date = "2022-11-29",
                        ),
                        AreaListItemModel(
                            id = "a2",
                            name = WORLDWIDE,
                            countryCodes = listOf("XW"),
                            date = "2022-11-30",
                        ),
                    ),
                    labels = listOf(
                        LabelListItemModel(
                            id = "l1",
                            name = "Label 1",
                            type = "Imprint",
                            catalogNumber = "ASDF-1010",
                        ),
                        LabelListItemModel(
                            id = "l1",
                            name = "Label 1",
                            catalogNumber = "ASDF-1011",
                        ),
                    ),
                    releaseLength = 8000,
                    hasNullLength = true,
                ),
                imageUrl = "https://i.scdn.co/image/ab6761610000f1786761852cd2852fceb64e8cd9",
            )
        }
    }
}
