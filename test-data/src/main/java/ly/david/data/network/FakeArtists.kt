package ly.david.data.network

import ly.david.data.LifeSpan
import ly.david.data.network.api.BrowseArtistsResponse
import ly.david.data.network.api.SearchArtistsResponse

val bandAid = ArtistMusicBrainzModel(
    id = "0ecaa896-58fa-4dca-b53d-8da7bc5f59c5",
    name = "Band Aid",
    type = "Group",
    typeId = "e431f5f6-b5d2-343d-8b36-72607fffb74b",
    disambiguation = "UK charity supergroup"
)

val carlosAlomar = ArtistMusicBrainzModel(
    id = "0719999c-d1bf-4e4b-b0a7-61a5384d0039",
    name = "Carlos Alomar",
    type = "Person",
    typeId = "b6e035f4-3ce9-331c-97df-83397230b0df"
)

val davidBowie = ArtistMusicBrainzModel(
    id = "5441c29d-3602-4898-b1a1-b77fa23b8e50",
    name = "David Bowie",
    type = "Person",
    gender = "Male",
    lifeSpan = LifeSpan(
        begin = "1947-01-08",
        end = "2016-01-10",
        ended = true
    ),
    relations = listOf(
        RelationMusicBrainzModel(
            artist = bandAid,
            direction = Direction.FORWARD,
            targetType = MusicBrainzResource.ARTIST,
            attributes = listOf("minor"),
            type = "collaboration",
            typeId = "75c09861-6857-4ec0-9729-84eefde7fc86",
        ),
        RelationMusicBrainzModel(
            artist = carlosAlomar,
            direction = Direction.BACKWARD,
            targetType = MusicBrainzResource.ARTIST,
            attributes = listOf("guitar"),
            type = "instrumental supporting musician",
            typeId = "ed6a7891-ce70-4e08-9839-1f2f62270497"
        )
    )
)

val davidBowieArtistCredit = ArtistCreditMusicBrainzModel(
    artist = davidBowie,
    name = "Different Artist Name",
    joinPhrase = " & "
)

val queen = ArtistMusicBrainzModel(
    id = "0383dadf-2a4e-4d10-a46a-e9e041da8eb3",
    name = "Queen",
    type = "Group",
    typeId = "e431f5f6-b5d2-343d-8b36-72607fffb74b",
    disambiguation = "UK rock group"
)

val queenArtistCredit = ArtistCreditMusicBrainzModel(
    artist = queen,
    name = "Other Artist",
)

val fakeArtists = listOf(
    davidBowie,
    queen
)

val browseArtistsResponse = BrowseArtistsResponse(
    count = 1,
    offset = 0,
    listOf(element = davidBowie)
)

val searchArtistsResponse = SearchArtistsResponse(
    count = 1,
    offset = 0,
    listOf(element = davidBowie)
)
