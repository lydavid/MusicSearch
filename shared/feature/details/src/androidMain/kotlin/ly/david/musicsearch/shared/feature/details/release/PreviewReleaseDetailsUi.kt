package ly.david.musicsearch.shared.feature.details.release

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import kotlinx.datetime.Instant
import ly.david.musicsearch.shared.domain.area.AreaType.COUNTRY
import ly.david.musicsearch.shared.domain.details.ReleaseDetailsModel
import ly.david.musicsearch.shared.domain.listitem.AreaListItemModel
import ly.david.musicsearch.shared.domain.listitem.LabelListItemModel
import ly.david.musicsearch.shared.domain.listitem.RelationListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.release.CoverArtArchiveUiModel
import ly.david.musicsearch.shared.domain.release.TextRepresentationUiModel
import ly.david.musicsearch.shared.domain.releasegroup.ReleaseGroupForRelease
import ly.david.musicsearch.shared.feature.details.utils.DetailsTabUiState
import ly.david.musicsearch.ui.core.theme.PreviewTheme

private val release = ReleaseDetailsModel(
    id = "38650e8c-3c6b-431e-b10b-2cfb6db847d5",
    name = "ウタの歌 ONE PIECE FILM RED",
    disambiguation = "初回限定盤",
    date = "2022-08-10",
    barcode = "4988031519660",
    status = "Official",
    formattedFormats = "CD + DVD",
    formattedTracks = "10 + 3",
    countryCode = "JP",
    packaging = "Jewel Case",
    packagingId = null,
    asin = "B0B392M9SC",
    quality = "normal",
    coverArtArchive = CoverArtArchiveUiModel(count = 11),
    textRepresentation = TextRepresentationUiModel(script = "Jpan", language = "jpn"),
    releaseGroup = ReleaseGroupForRelease(
        id = "1",
        name = "ウタの歌 ONE PIECE FILM RED",
        firstReleaseDate = "",
        primaryType = "Album",
        secondaryTypes = listOf("Soundtrack"),
    ),
    areas = listOf(
        AreaListItemModel(
            id = "2db42837-c832-3c27-b4a3-08198f75693c",
            name = "Japan",
            type = COUNTRY,
            countryCodes = listOf("JP"),
            date = "2022-08-10",
        ),
    ),
    labels = listOf(
        LabelListItemModel(
            id = "7689c51f-e09e-4e85-80d0-b95a9e23d216",
            name = "Virgin Music " +
                "(a division of Universal Music Japan created in 2014 that replaces EMI R)",
            type = "Original Production",
            catalogNumbers = "TYBX-10260, TYCT-69245, TYCX-60187",
        ),
    ),
    releaseLength = 2836000,
    lastUpdated = Instant.parse("2024-06-05T19:42:20Z"),
    urls = listOf(
        RelationListItemModel(
            id = "1",
            label = "ASIN",
            name = "https://www.amazon.co.jp/gp/product/B0B392M9SC",
            linkedEntity = MusicBrainzEntity.URL,
            linkedEntityId = "1",
        ),
        RelationListItemModel(
            id = "2",
            label = "VGMdb",
            name = "https://vgmdb.net/album/120991",
            linkedEntity = MusicBrainzEntity.URL,
            linkedEntityId = "2",
        ),
    ),
)

@PreviewLightDark
@Composable
internal fun PreviewReleaseDetailsUi() {
    PreviewTheme {
        Surface {
            ReleaseDetailsTabUi(
                release = release,
                detailsTabUiState = DetailsTabUiState(
                    numberOfImages = 11,
                    now = Instant.parse("2025-06-05T19:42:20Z"),
                ),
            )
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewReleaseDetailsUiCollapsed() {
    PreviewTheme {
        Surface {
            ReleaseDetailsTabUi(
                release = release,
                detailsTabUiState = DetailsTabUiState(
                    numberOfImages = 11,
                    isReleaseEventsCollapsed = true,
                    isExternalLinksCollapsed = true,
                    now = Instant.parse("2025-06-05T19:42:20Z"),
                ),
            )
        }
    }
}
