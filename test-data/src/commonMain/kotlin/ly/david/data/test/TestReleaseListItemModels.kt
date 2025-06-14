package ly.david.data.test

import ly.david.musicsearch.shared.domain.listitem.ReleaseListItemModel
import ly.david.musicsearch.shared.domain.release.CoverArtArchiveUiModel
import ly.david.musicsearch.shared.domain.release.TextRepresentationUiModel

val utaNoUtaReleaseListItemModel = ReleaseListItemModel(
    id = "38650e8c-3c6b-431e-b10b-2cfb6db847d5",
    name = "ウタの歌 ONE PIECE FILM RED",
    disambiguation = "初回限定盤",
    date = "2022-08-10",
    barcode = "4988031519660",
    status = "Official",
    statusId = null,
    countryCode = "JP",
    packaging = "Jewel Case",
    packagingId = null,
    asin = "B0B392M9SC",
    quality = "normal",
    coverArtArchive = CoverArtArchiveUiModel(count = 11),
    textRepresentation = TextRepresentationUiModel(script = "Jpan", language = "jpn"),
    formattedFormats = null,
    formattedTracks = null,
    formattedArtistCredits = "Ado",
    visited = false,
)

val weirdAlGreatestHitsReleaseListItemModel = ReleaseListItemModel(
    id = "9eef0b6f-9aa2-4573-8f3e-53d1a4826e3f",
    name = "“Weird Al” Yankovic’s Greatest Hits",
    disambiguation = "",
    date = "",
    barcode = "614223200828",
    status = "Official",
    statusId = null,
    countryCode = "AF",
    packaging = "None",
    packagingId = "119eba76-b343-3e02-a292-f0f00644bb9b",
    asin = "B00138CYEI",
    quality = "normal",
    catalogNumbers = null,
    coverArtArchive = CoverArtArchiveUiModel(count = 2),
    textRepresentation = TextRepresentationUiModel(script = "Latn", language = "eng"),
    imageUrl = null,
    formattedFormats = null,
    formattedTracks = null,
    formattedArtistCredits = "“Weird Al” Yankovic",
    visited = false,
)

val redReleaseListItemModel = ReleaseListItemModel(
    id = "5dc1f2db-867c-4de5-92f0-9d8440b672e3",
    name = "Red",
    disambiguation = "",
    quality = "normal",
    status = "Official",
    packaging = "Jewel Case",
    packagingId = "ec27701a-4a22-37f4-bfac-6616e0f9750a",
    date = "2012-10-22",
    countryCode = "GB", // first country it was released in
    barcode = "602537174539",
    textRepresentation = TextRepresentationUiModel(
        script = "Latn",
        language = "eng",
    ),
    formattedArtistCredits = "Taylor Swift",
    coverArtArchive = CoverArtArchiveUiModel(count = 25),
)

val underPressureRemasteredReleaseListItemModel = ReleaseListItemModel(
    id = "eac6d0cd-1ed0-4e17-b5b0-d3cfc40547b2",
    name = "Under Pressure",
    date = "1988-11",
    formattedArtistCredits = "Queen & David Bowie",
    countryCode = "GB",
    coverArtArchive = CoverArtArchiveUiModel(
        count = 4,
    ),
    quality = "normal",
    status = "Official",
    statusId = "4e304316-386d-3409-af2e-78857eec5cfe",
    textRepresentation = TextRepresentationUiModel(
        script = "Latn",
        language = "eng",
    ),
    barcode = "5099920305833",
    asin = "B000LX0GZA",
    packaging = null,
    packagingId = null,
    disambiguation = "",
)

val underPressureReleaseListItemModel = ReleaseListItemModel(
    id = "61735bf8-219e-3164-a94c-b74b1482fd01",
    name = "Under Pressure",
    date = "1981-10",
    formattedArtistCredits = "Queen & David Bowie",
    countryCode = "US",
    coverArtArchive = CoverArtArchiveUiModel(
        count = 1,
    ),
    quality = "normal",
    status = "Official",
    statusId = "4e304316-386d-3409-af2e-78857eec5cfe",
    textRepresentation = TextRepresentationUiModel(
        script = "Latn",
        language = "eng",
    ),
    disambiguation = "",
)

val underPressureJapanReleaseListItemModel = ReleaseListItemModel(
    id = "3e8fe20d-8d8b-454d-9350-2078007d4788",
    name = "Under Pressure",
    date = "1991",
    formattedArtistCredits = "Queen & David Bowie",
    countryCode = "JP",
    coverArtArchive = CoverArtArchiveUiModel(
        count = 0,
    ),
    quality = "normal",
    status = "Official",
    statusId = "4e304316-386d-3409-af2e-78857eec5cfe",
    textRepresentation = TextRepresentationUiModel(
        script = "Latn",
        language = "eng",
    ),
    disambiguation = "",
)
