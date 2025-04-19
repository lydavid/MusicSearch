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

val underPressureRemasteredReleaseMusicBrainzModel = ReleaseMusicBrainzModel(
    id = "eac6d0cd-1ed0-4e17-b5b0-d3cfc40547b2",
    name = "Under Pressure",
    date = "1988-11",
    artistCredits = listOf(
        ArtistCreditMusicBrainzModel(
            artist = ArtistMusicBrainzModel(
                id = "0383dadf-2a4e-4d10-a46a-e9e041da8eb3",
                name = "Queen",
                sortName = "Queen",
                type = "Group",
                typeId = "e431f5f6-b5d2-343d-8b36-72607fffb74b",
                disambiguation = "UK rock group",
            ),
            name = "Queen",
            joinPhrase = " & ",
        ),
        ArtistCreditMusicBrainzModel(
            artist = ArtistMusicBrainzModel(
                id = "5441c29d-3602-4898-b1a1-b77fa23b8e50",
                name = "David Bowie",
                sortName = "Bowie, David",
                type = "Person",
                typeId = "b6e035f4-3ce9-331c-97df-83397230b0df",
                disambiguation = "English singer‐songwriter",
            ),
            name = "David Bowie",
            joinPhrase = "",
        ),
    ),
    releaseEvents = listOf(
        ReleaseEventMusicBrainzModel(
            area = AreaMusicBrainzModel(
                id = "8a754a16-0027-3a29-b6d7-2b40ea0481ed",
                name = "United Kingdom",
                sortName = "United Kingdom",
                countryCodes = listOf("GB"),
            ),
            date = "1988-11",
        ),
    ),
    countryCode = "GB",
    coverArtArchive = CoverArtArchiveMusicBrainzModel(
        count = 4,
    ),
    quality = "normal",
    status = "Official",
    statusId = "4e304316-386d-3409-af2e-78857eec5cfe",
    textRepresentation = TextRepresentationMusicBrainzModel(
        script = "Latn",
        language = "eng",
    ),
    barcode = "5099920305833",
    asin = "B000LX0GZA",
    packaging = null,
    packagingId = null,
    disambiguation = "",
)

val underPressureRemasterOf = RelationMusicBrainzModel(
    type = "remaster",
    typeId = "48e327b5-2d04-4518-93f1-fed5f0f0fa3c",
    direction = Direction.BACKWARD,
    targetType = SerializableMusicBrainzEntity.RELEASE,
    release = underPressureRemasteredReleaseMusicBrainzModel,
)

val underPressureReleaseMusicBrainzModel = ReleaseMusicBrainzModel(
    id = "61735bf8-219e-3164-a94c-b74b1482fd01",
    name = "Under Pressure",
    date = "1981-10",
    artistCredits = listOf(
        ArtistCreditMusicBrainzModel(
            artist = ArtistMusicBrainzModel(
                id = "0383dadf-2a4e-4d10-a46a-e9e041da8eb3",
                name = "Queen",
                sortName = "Queen",
                type = "Group",
                typeId = "e431f5f6-b5d2-343d-8b36-72607fffb74b",
                disambiguation = "UK rock group",
            ),
            name = "Queen",
            joinPhrase = " & ",
        ),
        ArtistCreditMusicBrainzModel(
            artist = ArtistMusicBrainzModel(
                id = "5441c29d-3602-4898-b1a1-b77fa23b8e50",
                name = "David Bowie",
                sortName = "Bowie, David",
                type = "Person",
                typeId = "b6e035f4-3ce9-331c-97df-83397230b0df",
                disambiguation = "English singer‐songwriter",
            ),
            name = "David Bowie",
            joinPhrase = "",
        ),
    ),
    releaseEvents = listOf(
        ReleaseEventMusicBrainzModel(
            area = AreaMusicBrainzModel(
                id = "489ce91b-6658-3307-9877-795b68554c98",
                name = "United States",
                sortName = "United States",
                countryCodes = listOf("US"),
            ),
            date = "1981-10",
        ),
    ),
    countryCode = "US",
    coverArtArchive = CoverArtArchiveMusicBrainzModel(
        count = 1,
    ),
    quality = "normal",
    status = "Official",
    statusId = "4e304316-386d-3409-af2e-78857eec5cfe",
    textRepresentation = TextRepresentationMusicBrainzModel(
        script = "Latn",
        language = "eng",
    ),
    disambiguation = "",
)

val underPressureReleaseMusicBrainzModelWithLabel = underPressureReleaseMusicBrainzModel.copy(
    labelInfoList = listOf(
        underPressureLabelInfo,
        LabelInfo(
            label = elektraMusicGroupLabelMusicBrainzModel,
        ),
    ),
)

val underPressureJapanReleaseMusicBrainzModel = ReleaseMusicBrainzModel(
    id = "3e8fe20d-8d8b-454d-9350-2078007d4788",
    name = "Under Pressure",
    date = "1991",
    artistCredits = listOf(
        ArtistCreditMusicBrainzModel(
            artist = ArtistMusicBrainzModel(
                id = "0383dadf-2a4e-4d10-a46a-e9e041da8eb3",
                name = "Queen",
                sortName = "Queen",
                type = "Group",
                typeId = "e431f5f6-b5d2-343d-8b36-72607fffb74b",
                disambiguation = "UK rock group",
            ),
            name = "Queen",
            joinPhrase = " & ",
        ),
        ArtistCreditMusicBrainzModel(
            artist = ArtistMusicBrainzModel(
                id = "5441c29d-3602-4898-b1a1-b77fa23b8e50",
                name = "David Bowie",
                sortName = "Bowie, David",
                type = "Person",
                typeId = "b6e035f4-3ce9-331c-97df-83397230b0df",
                disambiguation = "English singer‐songwriter",
            ),
            name = "David Bowie",
            joinPhrase = "",
        ),
    ),
    releaseEvents = listOf(
        ReleaseEventMusicBrainzModel(
            area = AreaMusicBrainzModel(
                id = "2db42837-c832-3c27-b4a3-08198f75693c",
                name = "Japan",
                sortName = "Japan",
                countryCodes = listOf("JP"),
                disambiguation = "",
            ),
            date = "1991",
        ),
    ),
    countryCode = "JP",
    coverArtArchive = CoverArtArchiveMusicBrainzModel(
        count = 0,
    ),
    quality = "normal",
    status = "Official",
    statusId = "4e304316-386d-3409-af2e-78857eec5cfe",
    textRepresentation = TextRepresentationMusicBrainzModel(
        script = "Latn",
        language = "eng",
    ),
    disambiguation = "",
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
    coverArtArchive = CoverArtArchiveMusicBrainzModel(
        count = 11,
    ),
)

val releaseWith3CatalogNumbersWithSameLabel = utaNoUtaReleaseMusicBrainzModel.copy(
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

val redReleaseMusicBrainzModel = ReleaseMusicBrainzModel(
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
    textRepresentation = TextRepresentationMusicBrainzModel(
        script = "Latn",
        language = "eng",
    ),
    artistCredits = listOf(
        ArtistCreditMusicBrainzModel(
            artist = ArtistMusicBrainzModel(
                id = "20244d07-534f-4eff-b4d4-930878889970",
                name = "Taylor Swift",
                sortName = "Swift, Taylor",
                type = "Person",
                typeId = "b6e035f4-3ce9-331c-97df-83397230b0df",
            ),
            name = "Taylor Swift",
            joinPhrase = "",
        ),
    ),
    releaseEvents = listOf(
        ReleaseEventMusicBrainzModel(
            area = unitedKingdomAreaMusicBrainzModel.copy(
                type = null,
            ),
            date = "2012-10-22",
        ),
        ReleaseEventMusicBrainzModel(
            area = AreaMusicBrainzModel(
                id = "c6500277-9a3d-349b-bf30-41afdbf42add",
                name = "Italy",
                sortName = "Italy",
                countryCodes = listOf("IT"),
                type = null,
                typeId = null,
            ),
            date = "2012-10-23",
        ),
        ReleaseEventMusicBrainzModel(
            area = AreaMusicBrainzModel(
                id = "471c46a7-afc5-31c4-923c-d0444f5053a4",
                name = "Spain",
                sortName = "Spain",
                countryCodes = listOf("ES"),
                type = null,
                typeId = null,
            ),
            date = "2012-10-23",
        ),
        ReleaseEventMusicBrainzModel(
            area = AreaMusicBrainzModel(
                id = "ef1b7cc0-cd26-36f4-8ea0-04d9623786c7",
                name = "Netherlands",
                sortName = "Netherlands",
                countryCodes = listOf("NL"),
                type = null,
                typeId = null,
            ),
            date = "2012-10-26",
        ),
        ReleaseEventMusicBrainzModel(
            area = AreaMusicBrainzModel(
                id = "08310658-51eb-3801-80de-5a0739207115",
                name = "France",
                sortName = "France",
                countryCodes = listOf("FR"),
                type = null,
                typeId = null,
            ),
            date = "2012-11-05",
        ),
    ),
    coverArtArchive = CoverArtArchiveMusicBrainzModel(
        count = 25,
    ),
)

val releaseWithSameCatalogNumberWithDifferentLabels = redReleaseMusicBrainzModel.copy(
    labelInfoList = listOf(
        LabelInfo(
            catalogNumber = "3717453",
            label = LabelMusicBrainzModel(
                id = "1a917e6f-54f5-4964-bebf-5d4e2442ceb4",
                name = "Big Machine Records",
                type = "Production",
                typeId = "a2426aab-2dd4-339c-b47d-b4923a241678",
                labelCode = null,
                disambiguation = "",
            ),
        ),
        LabelInfo(
            catalogNumber = "3717453",
            label = mercuryRecordsLabelMusicBrainzModel,
        ),
    ),
)

val bandoriCoverCollection8ReleaseMusicBrainzModel = ReleaseMusicBrainzModel(
    id = "06fecdc4-dbfa-484f-a03b-5da975fadf0e",
    name = "バンドリ！ ガールズバンドパーティ！ カバーコレクション Vol.8",
)
