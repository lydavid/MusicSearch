package ly.david.musicsearch.ui.common.release

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import kotlinx.collections.immutable.persistentListOf
import ly.david.musicsearch.shared.domain.alias.BasicAlias
import ly.david.musicsearch.shared.domain.listitem.ReleaseListItemModel
import ly.david.musicsearch.test.image.InitializeFakeImageLoader
import ly.david.musicsearch.ui.common.preview.PreviewWithTransitionAndOverlays

@PreviewLightDark
@Composable
internal fun PreviewReleaseListItem() {
    PreviewWithTransitionAndOverlays {
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
    PreviewWithTransitionAndOverlays {
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
    PreviewWithTransitionAndOverlays {
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
    PreviewWithTransitionAndOverlays {
        ReleaseListItem(
            release = ReleaseListItemModel(
                id = "4",
                name = "Release title",
                countryCode = "CA",
            ),
            showMoreInfo = true,
        )
    }
}

@PreviewLightDark
@Composable
internal fun PreviewReleaseListItemDateCountryFormatsTracks() {
    PreviewWithTransitionAndOverlays {
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
    PreviewWithTransitionAndOverlays {
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
    PreviewWithTransitionAndOverlays {
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
    PreviewWithTransitionAndOverlays {
        ReleaseListItem(
            release = ReleaseListItemModel(
                id = "9",
                name = "Release title",
                countryCode = "DZ",
                formattedArtistCredits = "Some artist feat. another",
                releaseCountryCount = 3,
            ),
            showMoreInfo = true,
        )
    }
}

@PreviewLightDark
@Composable
internal fun PreviewReleaseListItemShowLessInfo() {
    PreviewWithTransitionAndOverlays {
        ReleaseListItem(
            release = ReleaseListItemModel(
                id = "5",
                name = "Release title",
                date = "2021-09-08",
                countryCode = "JP",
                catalogNumbers = "TYBX-10260",
                formattedFormats = "2×CD + Blu-ray",
                formattedTracks = "15 + 8 + 24",
            ),
            showMoreInfo = false,
        )
    }
}

private val release = ReleaseListItemModel(
    id = "38650e8c-3c6b-431e-b10b-2cfb6db847d5",
    name = "ウタの歌 ONE PIECE FILM RED",
    disambiguation = "初回限定盤",
    date = "2022-08-10",
    countryCode = "JP",
    catalogNumbers = "TYBX-10260, TYCT-69245, TYCX-60187",
    formattedFormats = "CD + DVD",
    formattedTracks = "10 + 3",
    aliases = persistentListOf(
        BasicAlias(
            name = "Uta no Uta ONE PIECE FILM RED",
            locale = "en",
            isPrimary = true,
        ),
    ),
)

@PreviewLightDark
@Composable
internal fun PreviewReleaseListItemCatalog() {
    PreviewWithTransitionAndOverlays {
        ReleaseListItem(
            release = release,
            showMoreInfo = true,
        )
    }
}

@PreviewLightDark
@Composable
internal fun PreviewReleaseListItemVisited() {
    PreviewWithTransitionAndOverlays {
        ReleaseListItem(
            release = release.copy(
                visited = true,
            ),
            showMoreInfo = true,
        )
    }
}

@PreviewLightDark
@Composable
internal fun PreviewReleaseListItemWithListens() {
    PreviewWithTransitionAndOverlays {
        ReleaseListItem(
            release = release.copy(
                visited = true,
                listenState = ReleaseListItemModel.ListenState.Known(
                    listenCount = 23,
                    completeListenCount = 1,
                ),
            ),
            showMoreInfo = true,
        )
    }
}

@PreviewLightDark
@Composable
internal fun PreviewReleaseListItemWithUnknownNumberOfListens() {
    PreviewWithTransitionAndOverlays {
        ReleaseListItem(
            release = release.copy(
                visited = false,
                listenState = ReleaseListItemModel.ListenState.Unknown,
            ),
            showMoreInfo = true,
        )
    }
}

@PreviewLightDark
@Composable
internal fun PreviewReleaseListItemWithCoverArt() {
    InitializeFakeImageLoader()
    PreviewWithTransitionAndOverlays {
        ReleaseListItem(
            release = ReleaseListItemModel(
                id = "1",
                name = "Release title",
                imageUrl = "www.example.com/image",
            ),
            showMoreInfo = true,
        )
    }
}
