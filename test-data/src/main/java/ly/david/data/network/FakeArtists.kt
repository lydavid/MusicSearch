package ly.david.data.network

import ly.david.data.network.api.SearchArtistsResponse

val fakeArtist = ArtistMusicBrainzModel(
    id = "artist1",
    name = "Artist Name",
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

val searchArtistsResponse = SearchArtistsResponse(
    count = 1,
    offset = 0,
    listOf(element = fakeArtist)
)
