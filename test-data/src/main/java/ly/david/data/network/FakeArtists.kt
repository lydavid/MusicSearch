package ly.david.data.network

val fakeArtist = ArtistMusicBrainzModel(
    id = "artist1",
    name = "Artist Name",
)

val fakeArtistCredit = ArtistCreditMusicBrainzModel(
    artist = fakeArtist,
    name = "Different Artist Name"
)

val fakeArtist2 = ArtistMusicBrainzModel(
    id = "artist2",
    name = "Other Artist",
)

val fakeArtistCredit2 = ArtistCreditMusicBrainzModel(
    artist = fakeArtist2,
    name = "Other Artist",
    joinPhrase = " feat. "
)
