package ly.david.data.network

import ly.david.data.LifeSpan
import ly.david.data.network.api.BrowseArtistsResponse
import ly.david.data.network.api.SearchArtistsResponse

val fakeArtistGroup = ArtistMusicBrainzModel(
    id = "artist2",
    name = "An Artist Group",
    type = "Group"
)

val fakeArtist = ArtistMusicBrainzModel(
    id = "artist1",
    name = "Artist Name",
    type = "Person",
    gender = "Male",
    lifeSpan = LifeSpan(
        begin = "2000-10-10",
    ),
    relations = listOf(
        RelationMusicBrainzModel(
            type = "",
            typeId = "6ed4bfc4-0a0d-44c0-b025-b7fc4d900b67",
            direction = Direction.FORWARD,
            targetType = MusicBrainzResource.ARTIST,
            artist = fakeArtistGroup
        )
    )
)

val fakeArtistCredit = ArtistCreditMusicBrainzModel(
    artist = fakeArtist,
    name = "Different Artist Name",
    joinPhrase = " feat. "
)

val fakeArtist2 = ArtistMusicBrainzModel(
    id = "artist2",
    name = "Other Artist",
)

val fakeArtistCredit2 = ArtistCreditMusicBrainzModel(
    artist = fakeArtist2,
    name = "Other Artist",
)

val fakeArtists = listOf(
    fakeArtist,
    fakeArtist2
)

val browseArtistsResponse = BrowseArtistsResponse(
    count = 1,
    offset = 0,
    listOf(element = fakeArtist)
)

val searchArtistsResponse = SearchArtistsResponse(
    count = 1,
    offset = 0,
    listOf(element = fakeArtist)
)
