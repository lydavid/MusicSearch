package ly.david.data.test

import ly.david.musicsearch.data.musicbrainz.models.core.AreaMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.CoverArtArchiveMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.LabelInfo
import ly.david.musicsearch.data.musicbrainz.models.core.LabelMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.ReleaseEventMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.ReleaseGroupMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.ReleaseMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.TextRepresentationMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.relation.Direction
import ly.david.musicsearch.data.musicbrainz.models.relation.SerializableMusicBrainzEntity
import ly.david.musicsearch.data.musicbrainz.models.relation.RelationMusicBrainzModel

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
            label = elektraMusicGroup,
        ),
    ),
    relations = listOf(underPressureRemasterOf),
)

val releaseWith3CatalogNumbersWithSameLabel = ReleaseMusicBrainzModel(
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
    releaseGroup = ReleaseGroupMusicBrainzModel(
        id = "22760f81-37ce-47ce-98b6-65f8a285f083",
        name = "ウタの歌 ONE PIECE FILM RED",
        primaryType = "Album",
        secondaryTypes = listOf(),
        disambiguation = "",
        artistCredits = listOf(adoArtistCreditMusicBrainzModel),
        firstReleaseDate = "2022-08-10",
    ),
    releaseEvents = listOf(
        ReleaseEventMusicBrainzModel(
            area = AreaMusicBrainzModel(
                id = "2db42837-c832-3c27-b4a3-08198f75693c",
                name = "Japan",
                disambiguation = "",
                countryCodes = listOf("JP"),
                sortName = "Japan",
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
