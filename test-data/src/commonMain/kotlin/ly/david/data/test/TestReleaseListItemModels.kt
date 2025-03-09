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
    imageId = 0,
    formattedFormats = null,
    formattedTracks = null,
    formattedArtistCredits = "“Weird Al” Yankovic",
    visited = false,
)

val redRReleaseListItemModel = ReleaseListItemModel(
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
