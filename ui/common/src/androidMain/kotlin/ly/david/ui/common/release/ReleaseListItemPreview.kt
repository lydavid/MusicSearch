import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ly.david.musicsearch.core.models.listitem.ReleaseListItemModel
import ly.david.ui.core.preview.DefaultPreviews
import ly.david.ui.core.theme.PreviewTheme

internal class ReleasePreviewParameterProvider : PreviewParameterProvider<ReleaseListItemModel> {
    override val values: Sequence<ReleaseListItemModel> = sequenceOf(
        ReleaseListItemModel(
            id = "1",
            name = "Release title",
        ),
        ReleaseListItemModel(
            id = "2",
            name = "Release title",
            disambiguation = "Disambiguation text",
        ),
        ReleaseListItemModel(
            id = "3",
            name = "Release title",
            disambiguation = "Disambiguation text",
            countryCode = "US",
        ),
        ReleaseListItemModel(
            id = "4",
            name = "Release title",
            disambiguation = "",
            countryCode = "CA",
        ),
        ReleaseListItemModel(
            id = "5",
            name = "Release title",
            date = "2021-09-08",
            countryCode = "JP",
            formattedFormats = "2×CD + Blu-ray",
            formattedTracks = "15 + 8 + 24",
        ),
        ReleaseListItemModel(
            id = "6",
            name = "Release title",
            date = "2022-04-03",
            countryCode = "NL",
        ),
        ReleaseListItemModel(
            id = "7",
            name = "Release title",
            countryCode = "NL",
        ),
        ReleaseListItemModel(
            id = "8",
            name = "Release title",
            date = "2022-04-03",
            formattedArtistCredits = "Some artist feat. Other artist",
        ),
        ReleaseListItemModel(
            id = "9",
            name = "Release title",
            countryCode = "DZ",
            releaseCountryCount = 3,
            formattedArtistCredits = "Some artist feat. another",
        ),
    )
}

@DefaultPreviews
@Composable
private fun Preview(
    @PreviewParameter(ReleasePreviewParameterProvider::class) release: ReleaseListItemModel,
) {
    PreviewTheme {
        Surface {
            ReleaseListItem(
                release = release,
                showMoreInfo = true,
            )
        }
    }
}
