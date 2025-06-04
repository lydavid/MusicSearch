package ly.david.data.test

import ly.david.musicsearch.data.musicbrainz.models.core.GenreMusicBrainzNetworkModel

val fakeGenre = GenreMusicBrainzNetworkModel(
    id = "911c7bbb-172d-4df8-9478-dbff4296e791",
    name = "pop",
)

val fakeGenres = listOf(
    fakeGenre,
)
