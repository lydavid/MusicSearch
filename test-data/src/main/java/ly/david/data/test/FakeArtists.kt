package ly.david.data.test

import ly.david.data.core.LifeSpanMusicBrainzModel
import ly.david.data.musicbrainz.ArtistCreditMusicBrainzModel
import ly.david.data.musicbrainz.ArtistMusicBrainzModel
import ly.david.data.musicbrainz.Direction
import ly.david.data.core.network.MusicBrainzEntity
import ly.david.data.musicbrainz.RelationMusicBrainzModel
import ly.david.data.musicbrainz.UrlMusicBrainzModel
import ly.david.data.musicbrainz.api.BrowseArtistsResponse
import ly.david.data.musicbrainz.api.SearchArtistsResponse

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

val davidBowieSpotify = UrlMusicBrainzModel(
    id = "63debbe5-79e0-4047-882d-a0dc8280b4b9",
    resource = "https://open.spotify.com/artist/0oSGxfWSnnOXhD2fKuz2Gy"
)

val davidBowieDeezer = UrlMusicBrainzModel(
    id = "cb9f2aef-c926-4163-9ce1-d906283bbf6c",
    resource = "https://www.deezer.com/artist/997"
)

val davidBowie = ArtistMusicBrainzModel(
    id = "5441c29d-3602-4898-b1a1-b77fa23b8e50",
    name = "David Bowie",
    type = "Person",
    gender = "Male",
    lifeSpan = LifeSpanMusicBrainzModel(
        begin = "1947-01-08",
        end = "2016-01-10",
        ended = true
    ),
    relations = listOf(
        RelationMusicBrainzModel(
            artist = bandAid,
            direction = Direction.FORWARD,
            targetType = MusicBrainzEntity.ARTIST,
            attributes = listOf("minor"),
            type = "collaboration",
            typeId = "75c09861-6857-4ec0-9729-84eefde7fc86",
        ),
        RelationMusicBrainzModel(
            artist = carlosAlomar,
            direction = Direction.BACKWARD,
            targetType = MusicBrainzEntity.ARTIST,
            attributes = listOf("guitar"),
            type = "instrumental supporting musician",
            typeId = "ed6a7891-ce70-4e08-9839-1f2f62270497"
        ),
        RelationMusicBrainzModel(
            url = davidBowieSpotify,
            direction = Direction.FORWARD,
            targetType = MusicBrainzEntity.URL,
            type = "free streaming",
            typeId = "769085a1-c2f7-4c24-a532-2375a77693bd"
        ),
        RelationMusicBrainzModel(
            url = davidBowieDeezer,
            direction = Direction.FORWARD,
            targetType = MusicBrainzEntity.URL,
            type = "free streaming",
            typeId = "769085a1-c2f7-4c24-a532-2375a77693bd"
        ),
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
