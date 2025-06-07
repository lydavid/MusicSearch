package ly.david.musicsearch.ui.common.release

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import ly.david.musicsearch.shared.domain.listitem.ReleaseListItemModel
import ly.david.musicsearch.ui.common.preview.PreviewWithSharedElementTransition

@PreviewLightDark
@Composable
internal fun PreviewReleaseListItem() {
    PreviewWithSharedElementTransition {
        ReleaseListItem(
            release = ReleaseListItemModel(
                id = "1",
                name = "Release title",
            ),
            showMoreInfo = true,
        )
    }
}

@PreviewLightDark
@Composable
internal fun PreviewReleaseListItemDisambiguation() {
    PreviewWithSharedElementTransition {
        ReleaseListItem(
            release = ReleaseListItemModel(
                id = "2",
                name = "Release title",
                disambiguation = "Disambiguation text",
            ),
            showMoreInfo = true,
        )
    }
}

@PreviewLightDark
@Composable
internal fun PreviewReleaseListItemDisambiguationCountry() {
    PreviewWithSharedElementTransition {
        ReleaseListItem(
            release = ReleaseListItemModel(
                id = "3",
                name = "Release title",
                disambiguation = "Disambiguation text",
                countryCode = "US",
            ),
            showMoreInfo = true,
        )
    }
}

@PreviewLightDark
@Composable
internal fun PreviewReleaseListItemCountry() {
    PreviewWithSharedElementTransition {
        ReleaseListItem(
            release = ReleaseListItemModel(
                id = "4",
                name = "Release title",
                disambiguation = "",
                countryCode = "CA",
            ),
            showMoreInfo = true,
        )
    }
}

@PreviewLightDark
@Composable
internal fun PreviewReleaseListItemDateCountryFormatsTracks() {
    PreviewWithSharedElementTransition {
        ReleaseListItem(
            release = ReleaseListItemModel(
                id = "5",
                name = "Release title",
                date = "2021-09-08",
                countryCode = "JP",
                formattedFormats = "2×CD + Blu-ray",
                formattedTracks = "15 + 8 + 24",
            ),
            showMoreInfo = true,
        )
    }
}

@PreviewLightDark
@Composable
internal fun PreviewReleaseListItemCountryDate() {
    PreviewWithSharedElementTransition {
        ReleaseListItem(
            release = ReleaseListItemModel(
                id = "6",
                name = "Release title",
                date = "2022-04-03",
                countryCode = "NL",
            ),
            showMoreInfo = true,
        )
    }
}

@PreviewLightDark
@Composable
internal fun PreviewReleaseListItemDateArtistCredits() {
    PreviewWithSharedElementTransition {
        ReleaseListItem(
            release = ReleaseListItemModel(
                id = "8",
                name = "Release title",
                date = "2022-04-03",
                formattedArtistCredits = "Some artist feat. Other artist",
            ),
            showMoreInfo = true,
        )
    }
}

@PreviewLightDark
@Composable
internal fun PreviewReleaseListItemMultipleCountries() {
    PreviewWithSharedElementTransition {
        ReleaseListItem(
            release = ReleaseListItemModel(
                id = "9",
                name = "Release title",
                countryCode = "DZ",
                releaseCountryCount = 3,
                formattedArtistCredits = "Some artist feat. another",
            ),
            showMoreInfo = true,
        )
    }
}

@PreviewLightDark
@Composable
internal fun PreviewReleaseListItemShowLessInfo() {
    PreviewWithSharedElementTransition {
        ReleaseListItem(
            release = ReleaseListItemModel(
                id = "5",
                name = "Release title",
                date = "2021-09-08",
                countryCode = "JP",
                formattedFormats = "2×CD + Blu-ray",
                formattedTracks = "15 + 8 + 24",
                catalogNumbers = "TYBX-10260",
            ),
            showMoreInfo = false,
        )
    }
}

@PreviewLightDark
@Composable
internal fun PreviewReleaseListItemCatalog() {
    PreviewWithSharedElementTransition {
        ReleaseListItem(
            release = ReleaseListItemModel(
                id = "38650e8c-3c6b-431e-b10b-2cfb6db847d5",
                name = "ウタの歌 ONE PIECE FILM RED",
                disambiguation = "初回限定盤",
                date = "2022-08-10",
                countryCode = "JP",
                catalogNumbers = "TYBX-10260, TYCT-69245, TYCX-60187",
            ),
            showMoreInfo = true,
        )
    }
}

@PreviewLightDark
@Composable
internal fun PreviewReleaseListItemVisited() {
    PreviewWithSharedElementTransition {
        ReleaseListItem(
            release = ReleaseListItemModel(
                id = "38650e8c-3c6b-431e-b10b-2cfb6db847d5",
                name = "ウタの歌 ONE PIECE FILM RED",
                disambiguation = "初回限定盤",
                date = "2022-08-10",
                countryCode = "JP",
                catalogNumbers = "TYBX-10260, TYCT-69245, TYCX-60187",
                formattedFormats = "2×CD + Blu-ray",
                formattedTracks = "15 + 8 + 24",
                visited = true,
            ),
            showMoreInfo = true,
        )
    }
}
