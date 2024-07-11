package ly.david.data.test

import ly.david.musicsearch.data.musicbrainz.models.relation.Direction
import ly.david.musicsearch.data.musicbrainz.models.core.LabelInfo
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.data.musicbrainz.models.relation.RelationMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.ReleaseEventMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.ReleaseMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.api.BrowseReleasesResponse
import ly.david.musicsearch.data.musicbrainz.api.SearchReleasesResponse
import ly.david.musicsearch.data.musicbrainz.models.common.ArtistCreditMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.ArtistMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.LabelMusicBrainzModel

val underPressureRemastered = ReleaseMusicBrainzModel(
    id = "eac6d0cd-1ed0-4e17-b5b0-d3cfc40547b2",
    name = "Under Pressure",
    artistCredits = listOf(queenArtistCredit, davidBowieArtistCredit),
    releaseGroup = underPressureReleaseGroup,
)

val underPressureRemasterOf = RelationMusicBrainzModel(
    type = "remaster",
    typeId = "48e327b5-2d04-4518-93f1-fed5f0f0fa3c",
    direction = Direction.BACKWARD,
    targetType = MusicBrainzEntity.RELEASE,
    release = underPressureRemastered,
)

val fakeReleaseEvent = ReleaseEventMusicBrainzModel(
    area = canada,
    date = "2022-10-29",
)

val underPressure = ReleaseMusicBrainzModel(
    id = "61735bf8-219e-3164-a94c-b74b1482fd01",
    name = "Under Pressure",
    barcode = "123",
    date = "1981-10",
    artistCredits = listOf(queenArtistCredit, davidBowieArtistCredit),
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
    artistCredits = listOf(
        ArtistCreditMusicBrainzModel(
            artist = ArtistMusicBrainzModel(
                id = "e134b52f-2e9e-4734-9bc3-bea9648d1fa1",
                type = "Person",
                sortName = "Ado",
                typeId = "b6e035f4-3ce9-331c-97df-83397230b0df",
                disambiguation = "Japanese vocalist",
                name = "Ado",
            ),
            name = "Ado",
            joinPhrase = "",
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
)

val fakeReleases = listOf(
    underPressure,
    underPressureRemastered,
)

val browseReleasesResponse = BrowseReleasesResponse(
    count = 1,
    offset = 0,
    musicBrainzModels = listOf(
        releaseWith3CatalogNumbersWithSameLabel,
        underPressure,
    ),
)

val searchReleasesResponse = SearchReleasesResponse(
    count = 1,
    offset = 0,
    listOf(underPressure),
)
