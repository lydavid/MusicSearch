package ly.david.data.test

import ly.david.musicsearch.data.musicbrainz.models.common.ArtistCreditMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.AreaMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.ArtistMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.CoverArtArchiveMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.LabelInfo
import ly.david.musicsearch.data.musicbrainz.models.core.LabelMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.ReleaseEventMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.ReleaseMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.TextRepresentationMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.relation.Direction
import ly.david.musicsearch.data.musicbrainz.models.relation.RelationMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.relation.SerializableMusicBrainzEntity

val underPressureRemastered = ReleaseMusicBrainzModel(
    id = "eac6d0cd-1ed0-4e17-b5b0-d3cfc40547b2",
    name = "Under Pressure",
    artistCredits = listOf(
        queenArtistCredit,
        davidBowieArtistCredit,
    ),
    releaseGroup = underPressureReleaseGroup,
)

val underPressureRemasterOf = RelationMusicBrainzModel(
    type = "remaster",
    typeId = "48e327b5-2d04-4518-93f1-fed5f0f0fa3c",
    direction = Direction.BACKWARD,
    targetType = SerializableMusicBrainzEntity.RELEASE,
    release = underPressureRemastered,
)

val fakeReleaseEvent = ReleaseEventMusicBrainzModel(
    area = canadaAreaMusicBrainzModel,
    date = "2022-10-29",
)

val underPressure = ReleaseMusicBrainzModel(
    id = "61735bf8-219e-3164-a94c-b74b1482fd01",
    name = "Under Pressure",
    barcode = "123",
    date = "1981-10",
    artistCredits = listOf(
        queenArtistCredit,
        davidBowieArtistCredit,
    ),
    releaseGroup = underPressureReleaseGroup,
    releaseEvents = listOf(
        fakeReleaseEvent,
    ),
    media = listOf(underPressureMedia),
    labelInfoList = listOf(
        underPressureLabelInfo,
        LabelInfo(
            label = elektraMusicGroupLabelMusicBrainzModel,
        ),
    ),
    relations = listOf(underPressureRemasterOf),
)

val utaNoUtaReleaseMusicBrainzModel = ReleaseMusicBrainzModel(
    id = "38650e8c-3c6b-431e-b10b-2cfb6db847d5",
    name = "ウタの歌 ONE PIECE FILM RED",
    disambiguation = "初回限定盤",
    quality = "normal",
    status = "Official",
    asin = "B0B392M9SC",
    packaging = "Jewel Case",
    date = "2022-08-10",
    countryCode = "JP",
    barcode = "4988031519660",
    textRepresentation = TextRepresentationMusicBrainzModel(
        script = "Jpan",
        language = "jpn",
    ),
    artistCredits = listOf(
        adoArtistCreditMusicBrainzModel,
    ),
    releaseEvents = listOf(
        ReleaseEventMusicBrainzModel(
            area = japanAreaMusicBrainzModel.copy(
                type = null,
            ),
            date = "2022-08-10",
        ),
    ),
    labelInfoList = listOf(
        LabelInfo(
            catalogNumber = "TYBX-10260",
            label = LabelMusicBrainzModel(
                id = "7689c51f-e09e-4e85-80d0-b95a9e23d216",
                name = "Virgin Music",
                type = "Original Production",
                labelCode = null,
                disambiguation = "a division of Universal Music Japan created in 2014 that replaces EMI R",
                typeId = "7aaa37fe-2def-3476-b359-80245850062d",
            ),
        ),
        LabelInfo(
            catalogNumber = "TYCT-69245",
            label = LabelMusicBrainzModel(
                id = "7689c51f-e09e-4e85-80d0-b95a9e23d216",
                name = "Virgin Music",
                type = "Original Production",
                labelCode = null,
                disambiguation = "a division of Universal Music Japan created in 2014 that replaces EMI R",
                typeId = "7aaa37fe-2def-3476-b359-80245850062d",
            ),
        ),
        LabelInfo(
            catalogNumber = "TYCX-60187",
            label = LabelMusicBrainzModel(
                id = "7689c51f-e09e-4e85-80d0-b95a9e23d216",
                name = "Virgin Music",
                type = "Original Production",
                labelCode = null,
                disambiguation = "a division of Universal Music Japan created in 2014 that replaces EMI R",
                typeId = "7aaa37fe-2def-3476-b359-80245850062d",
            ),
        ),
    ),
    coverArtArchive = CoverArtArchiveMusicBrainzModel(
        count = 11,
    ),
)

val releaseWith3CatalogNumbersWithSameLabel = utaNoUtaReleaseMusicBrainzModel

val weirdAlGreatestHitsReleaseMusicBrainzModel = ReleaseMusicBrainzModel(
    id = "9eef0b6f-9aa2-4573-8f3e-53d1a4826e3f",
    name = "“Weird Al” Yankovic’s Greatest Hits",
    disambiguation = "",
    quality = "normal",
    status = "Official",
    asin = "B00138CYEI",
    packaging = "None",
    packagingId = "119eba76-b343-3e02-a292-f0f00644bb9b",
    date = "",
    countryCode = "AF", // first country listed
    barcode = "614223200828",
    textRepresentation = TextRepresentationMusicBrainzModel(
        script = "Latn",
        language = "eng",
    ),
    artistCredits = listOf(
        ArtistCreditMusicBrainzModel(
            artist = ArtistMusicBrainzModel(
                id = "7746d775-9550-4360-b8d5-c37bd448ce01",
                name = "“Weird Al” Yankovic",
                sortName = "Yankovic, Weird Al",
                type = "Person",
                typeId = "b6e035f4-3ce9-331c-97df-83397230b0df",
            ),
            name = "“Weird Al” Yankovic",
            joinPhrase = "",
        ),
    ),
    releaseEvents = listOf(
        ReleaseEventMusicBrainzModel(
            area = AreaMusicBrainzModel(
                id = "aa95182f-df0a-3ad6-8bfb-4b63482cd276",
                name = "Afghanistan",
                sortName = "Afghanistan",
                disambiguation = "",
                countryCodes = listOf("AF"),
                type = null,
            ),
            date = "",
        ),
        // many more that I've truncated here
        ReleaseEventMusicBrainzModel(
            area = japanAreaMusicBrainzModel.copy(
                type = null,
            ),
            date = "",
        ),
    ),
    coverArtArchive = CoverArtArchiveMusicBrainzModel(
        count = 2,
    ),
)
