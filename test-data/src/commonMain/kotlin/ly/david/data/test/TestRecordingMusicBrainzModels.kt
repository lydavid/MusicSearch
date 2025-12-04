package ly.david.data.test

import ly.david.musicsearch.data.musicbrainz.models.common.AliasMusicBrainzNetworkModel
import ly.david.musicsearch.data.musicbrainz.models.common.ArtistCreditMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.RecordingMusicBrainzNetworkModel

val underPressureRecordingMusicBrainzModel = RecordingMusicBrainzNetworkModel(
    id = "32c7e292-14f1-4080-bddf-ef852e0a4c59",
    name = "Under Pressure",
    firstReleaseDate = "1981-10",
    length = 241000,
    artistCredits = listOf(
        ArtistCreditMusicBrainzModel(
            artist = queenArtistMusicBrainzModel,
            name = "Queen",
            joinPhrase = " & ",
        ),
        ArtistCreditMusicBrainzModel(
            artist = davidBowieArtistMusicBrainzModel,
            name = "David Bowie",
            joinPhrase = "",
        ),
    ),
    isrcs = listOf(
        "CBCEG8100001",
        "GBCEE0900136",
        "GBCEG0100041",
        "GBCEG8100001",
        "GBCEG9400046",
        "GBUM71029622",
        "GBUM71106916",
        "GBUM71404523",
    ),
)

val skycladObserverRecordingMusicBrainzModel = RecordingMusicBrainzNetworkModel(
    id = "6a8fc477-9b12-4001-9387-f5d936b05503",
    name = "スカイクラッドの観測者",
    firstReleaseDate = "2009-10-28",
    length = 275186,
    artistCredits = listOf(
        ArtistCreditMusicBrainzModel(
            artist = itouKanakoArtistMusicBrainzModel,
            name = "いとうかなこ",
            joinPhrase = "",
        ),
    ),
    isrcs = listOf(
        "JPK630905801",
        "JPR501002358",
    ),
    aliases = listOf(
        AliasMusicBrainzNetworkModel(
            name = "Skyclad Observer",
            locale = "en",
            isPrimary = true,
        ),
        AliasMusicBrainzNetworkModel(
            name = "スカイクラッドの観測者",
            locale = "ja",
            isPrimary = true,
        ),
    )
)

val skycladObserverCoverRecordingMusicBrainzModel = RecordingMusicBrainzNetworkModel(
    id = "108a3d66-d1ef-424d-a7cb-2f53a702ce45",
    name = "スカイクラッドの観測者",
    firstReleaseDate = "2023-09-06",
    length = 273826,
    artistCredits = listOf(
        ArtistCreditMusicBrainzModel(
            artist = roseliaArtistMusicBrainzModel,
            name = "Roselia",
            joinPhrase = "×",
        ),
        ArtistCreditMusicBrainzModel(
            artist = itouKanakoArtistMusicBrainzModel,
            name = "いとうかなこ",
            joinPhrase = "",
        ),
    ),
    isrcs = listOf(
        "JPR562300374",
    ),
)
