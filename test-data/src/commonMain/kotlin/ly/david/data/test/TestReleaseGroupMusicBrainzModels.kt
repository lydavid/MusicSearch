package ly.david.data.test

import ly.david.musicsearch.data.musicbrainz.models.common.ArtistCreditMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.ArtistMusicBrainzNetworkModel
import ly.david.musicsearch.data.musicbrainz.models.core.ReleaseGroupMusicBrainzNetworkModel

val underPressureReleaseGroupMusicBrainzModel = ReleaseGroupMusicBrainzNetworkModel(
    id = "bdaeec2d-94f1-46b5-91f3-340ec6939c66",
    name = "Under Pressure",
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
    primaryType = "Single",
    firstReleaseDate = "1981-10",
)

val utaNoUtaReleaseGroupMusicBrainzModel = ReleaseGroupMusicBrainzNetworkModel(
    id = "22760f81-37ce-47ce-98b6-65f8a285f083",
    name = "ウタの歌 ONE PIECE FILM RED",
    primaryType = "Album",
    secondaryTypes = listOf(),
    disambiguation = "",
    artistCredits = listOf(adoArtistCreditMusicBrainzModel),
    firstReleaseDate = "2022-08-10",
)

val alsoSprachZarathustraReleaseGroupMusicBrainzModel = ReleaseGroupMusicBrainzNetworkModel(
    id = "d0c3b18b-060b-41a8-9629-b880b2a95c13",
    name = "Also sprach Zarathustra",
    artistCredits = listOf(
        ArtistCreditMusicBrainzModel(
            artist = ArtistMusicBrainzNetworkModel(
                id = "4cb43d82-824e-4034-b03d-1a98f36f6e16",
                name = "Richard Strauss",
                sortName = "Strauss, Richard",
                type = "Person",
                disambiguation = "German composer",
            ),
            name = "Richard Strauss",
            joinPhrase = "; ",
        ),
        ArtistCreditMusicBrainzModel(
            artist = ArtistMusicBrainzNetworkModel(
                id = "f4c349f9-9551-478c-b160-76f1d1c1295e",
                name = "Michel Schwalbé",
                sortName = "Schwalbé, Michel",
                type = "Person",
                disambiguation = "Polish violinist",
            ),
            name = "Michel Schwalbé",
            joinPhrase = ", ",
        ),
        ArtistCreditMusicBrainzModel(
            artist = ArtistMusicBrainzNetworkModel(
                id = "dea28aa9-1086-4ffa-8739-0ccc759de1ce",
                name = "Berliner Philharmoniker",
                sortName = "Berliner Philharmoniker",
                type = "Orchestra",
                disambiguation = "",
            ),
            name = "Berliner Philharmoniker",
            joinPhrase = ", ",
        ),
        ArtistCreditMusicBrainzModel(
            artist = ArtistMusicBrainzNetworkModel(
                id = "d2ced2f1-6b58-47cf-ae87-5943e2ab6d99",
                name = "Herbert von Karajan",
                sortName = "Karajan, Herbert von",
                type = "Person",
                disambiguation = "conductor",
            ),
            name = "Herbert von Karajan",
            joinPhrase = "",
        ),
    ),
    primaryType = "Album",
    firstReleaseDate = "1974",
    disambiguation = "1973 recordings",
)

val nutcrackerReleaseGroupMusicBrainzModel = ReleaseGroupMusicBrainzNetworkModel(
    id = "95959c5f-6011-4c20-b932-0a70b529e045",
    name = "The Nutcracker",
    artistCredits = listOf(
        ArtistCreditMusicBrainzModel(
            artist = ArtistMusicBrainzNetworkModel(
                id = "9ddd7abc-9e1b-471d-8031-583bc6bc8be9",
                name = "Пётр Ильич Чайковский",
                sortName = "Tchaikovsky, Pyotr Ilyich",
                type = "Person",
                disambiguation = "Russian romantic composer",
            ),
            name = "Tchaikovsky",
            joinPhrase = "; ",
        ),
        ArtistCreditMusicBrainzModel(
            artist = ArtistMusicBrainzNetworkModel(
                id = "dea28aa9-1086-4ffa-8739-0ccc759de1ce",
                name = "Berliner Philharmoniker",
                sortName = "Berliner Philharmoniker",
                type = "Orchestra",
                disambiguation = "",
            ),
            name = "Berliner Philharmoniker",
            joinPhrase = ", ",
        ),
        ArtistCreditMusicBrainzModel(
            artist = ArtistMusicBrainzNetworkModel(
                id = "80c3bce9-0b54-409c-aa10-3b0c7dbbc597",
                name = "Simon Rattle",
                sortName = "Rattle, Simon",
                type = "Person",
                disambiguation = "conductor",
            ),
            name = "Simon Rattle",
            joinPhrase = "",
        ),
    ),
    primaryType = "Album",
    firstReleaseDate = "2010-10-11",
    disambiguation = "",
)

val tchaikovskyOverturesReleaseGroupMusicBrainzModel = ReleaseGroupMusicBrainzNetworkModel(
    id = "3e76b16f-c8ef-342a-b909-ca50d92766d2",
    name = "“1812” Overture / Romeo and Juliet / Marche slave / The Tempest",
    artistCredits = listOf(
        ArtistCreditMusicBrainzModel(
            artist = ArtistMusicBrainzNetworkModel(
                id = "9ddd7abc-9e1b-471d-8031-583bc6bc8be9",
                name = "Пётр Ильич Чайковский",
                sortName = "Tchaikovsky, Pyotr Ilyich",
                type = "Person",
                disambiguation = "Russian romantic composer",
            ),
            name = "Tchaikovsky",
            joinPhrase = "; ",
        ),
        ArtistCreditMusicBrainzModel(
            artist = ArtistMusicBrainzNetworkModel(
                id = "dea28aa9-1086-4ffa-8739-0ccc759de1ce",
                name = "Berliner Philharmoniker",
                sortName = "Berliner Philharmoniker",
                type = "Orchestra",
                disambiguation = "",
            ),
            name = "Berliner Philharmoniker",
            joinPhrase = ", ",
        ),
        ArtistCreditMusicBrainzModel(
            artist = ArtistMusicBrainzNetworkModel(
                id = "39e84597-3e0f-4ccc-89d2-6ee1dd6fb050",
                name = "Claudio Abbado",
                sortName = "Abbado, Claudio",
                type = "Person",
                disambiguation = "conductor",
            ),
            name = "Claudio Abbado",
            joinPhrase = "",
        ),
    ),
    primaryType = "Album",
    firstReleaseDate = "2000-02-01",
    disambiguation = "",
)
