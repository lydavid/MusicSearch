package ly.david.data.test

import ly.david.musicsearch.data.musicbrainz.models.core.GenreMusicBrainzModel

val fakeGenre = GenreMusicBrainzModel(
    id = "911c7bbb-172d-4df8-9478-dbff4296e791",
    name = "pop",
)

val fakeGenres = listOf(
    fakeGenre,
)
