package ly.david.data.test

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
)
