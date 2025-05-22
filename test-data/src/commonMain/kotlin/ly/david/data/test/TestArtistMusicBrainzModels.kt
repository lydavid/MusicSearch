package ly.david.data.test

import ly.david.musicsearch.data.musicbrainz.models.UrlMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.common.ArtistCreditMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.common.LifeSpanMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.AreaMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.ArtistMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.relation.Direction
import ly.david.musicsearch.data.musicbrainz.models.relation.RelationMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.relation.SerializableMusicBrainzEntity

val bandAid = ArtistMusicBrainzModel(
    id = "0ecaa896-58fa-4dca-b53d-8da7bc5f59c5",
    name = "Band Aid",
    type = "Group",
    typeId = "e431f5f6-b5d2-343d-8b36-72607fffb74b",
    disambiguation = "UK charity supergroup",
)

val carlosAlomar = ArtistMusicBrainzModel(
    id = "0719999c-d1bf-4e4b-b0a7-61a5384d0039",
    name = "Carlos Alomar",
    type = "Person",
    typeId = "b6e035f4-3ce9-331c-97df-83397230b0df",
)

val davidBowieSpotify = UrlMusicBrainzModel(
    id = "63debbe5-79e0-4047-882d-a0dc8280b4b9",
    resource = "https://open.spotify.com/artist/0oSGxfWSnnOXhD2fKuz2Gy",
)

val davidBowieDeezer = UrlMusicBrainzModel(
    id = "cb9f2aef-c926-4163-9ce1-d906283bbf6c",
    resource = "https://www.deezer.com/artist/997",
)

val davidBowieArtistMusicBrainzModel = ArtistMusicBrainzModel(
    id = "5441c29d-3602-4898-b1a1-b77fa23b8e50",
    name = "David Bowie",
    type = "Person",
    gender = "Male",
    lifeSpan = LifeSpanMusicBrainzModel(
        begin = "1947-01-08",
        end = "2016-01-10",
        ended = true,
    ),
    relations = listOf(
        RelationMusicBrainzModel(
            artist = bandAid,
            direction = Direction.FORWARD,
            targetType = SerializableMusicBrainzEntity.ARTIST,
            attributes = listOf("minor"),
            type = "collaboration",
            typeId = "75c09861-6857-4ec0-9729-84eefde7fc86",
        ),
        RelationMusicBrainzModel(
            artist = carlosAlomar,
            direction = Direction.BACKWARD,
            targetType = SerializableMusicBrainzEntity.ARTIST,
            attributes = listOf("guitar"),
            type = "instrumental supporting musician",
            typeId = "ed6a7891-ce70-4e08-9839-1f2f62270497",
        ),
        RelationMusicBrainzModel(
            url = davidBowieSpotify,
            direction = Direction.FORWARD,
            targetType = SerializableMusicBrainzEntity.URL,
            type = "free streaming",
            typeId = "769085a1-c2f7-4c24-a532-2375a77693bd",
        ),
        RelationMusicBrainzModel(
            url = davidBowieDeezer,
            direction = Direction.FORWARD,
            targetType = SerializableMusicBrainzEntity.URL,
            type = "free streaming",
            typeId = "769085a1-c2f7-4c24-a532-2375a77693bd",
        ),
    ),
)

val queenArtistMusicBrainzModel = ArtistMusicBrainzModel(
    id = "0383dadf-2a4e-4d10-a46a-e9e041da8eb3",
    name = "Queen",
    type = "Group",
    typeId = "e431f5f6-b5d2-343d-8b36-72607fffb74b",
    disambiguation = "UK rock group",
)

val adoArtistMusicBrainzModel = ArtistMusicBrainzModel(
    id = "e134b52f-2e9e-4734-9bc3-bea9648d1fa1",
    type = "Person",
    sortName = "Ado",
    typeId = "b6e035f4-3ce9-331c-97df-83397230b0df",
    disambiguation = "Japanese vocalist",
    name = "Ado",
)

val adoArtistCreditMusicBrainzModel = ArtistCreditMusicBrainzModel(
    artist = adoArtistMusicBrainzModel,
    name = "Ado",
    joinPhrase = "",
)

val kissArtistMusicBrainzModel = ArtistMusicBrainzModel(
    id = "e1f1e33e-2e4c-4d43-b91b-7064068d3283",
    type = "Group",
    sortName = "KISS",
    typeId = "e431f5f6-b5d2-343d-8b36-72607fffb74b",
    disambiguation = "US rock band",
    name = "KISS",
    lifeSpan = LifeSpanMusicBrainzModel(
        begin = "1973-01",
        end = "2023-12-02",
        ended = true,
    ),
    isnis = listOf("0000000123153016"),
    countryCode = "US",
)

val variousArtistsArtistMusicBrainzModel = ArtistMusicBrainzModel(
    id = "89ad4ac3-39f7-470e-963a-56509c546377",
    name = "Various Artists",
    sortName = "Various Artists",
    disambiguation = "add compilations to this artist",
    type = "Other",
    typeId = "ac897045-5043-3294-969b-187360e45d86",
    lifeSpan = LifeSpanMusicBrainzModel(
        begin = null,
        end = null,
        ended = false,
    ),
)

val roseliaArtistMusicBrainzModel = ArtistMusicBrainzModel(
    id = "adea3c3d-a84d-4f9e-ac0b-1ef71a8947a5",
    name = "Roselia",
    sortName = "Roselia",
    type = "Group",
    typeId = "e431f5f6-b5d2-343d-8b36-72607fffb74b",
    disambiguation = "BanG Dream!",
    lifeSpan = LifeSpanMusicBrainzModel(
        begin = "2016-09-15",
    ),
)

val itouKanakoArtistMusicBrainzModel = ArtistMusicBrainzModel(
    id = "eee65fbc-ead0-4c01-b385-a6f563c418d3",
    name = "いとうかなこ",
    disambiguation = "singer",
    sortName = "Ito, Kanako",
    type = "Person",
    typeId = "b6e035f4-3ce9-331c-97df-83397230b0df",
    lifeSpan = LifeSpanMusicBrainzModel(
        begin = "1973-03-28",
    ),
)

val tchaikovskyArtistMusicBrainzModel = ArtistMusicBrainzModel(
    id = "9ddd7abc-9e1b-471d-8031-583bc6bc8be9",
    name = "Пётр Ильич Чайковский",
    disambiguation = "Russian romantic composer",
    sortName = "Tchaikovsky, Pyotr Ilyich",
    type = "Person",
    typeId = "b6e035f4-3ce9-331c-97df-83397230b0df",
    lifeSpan = LifeSpanMusicBrainzModel(
        begin = "1840-05-07",
        end = "1893-11-06",
        ended = true,
    ),
    isnis = listOf("0000000121449518"),
    ipis = listOf("00084070584", "00222236411"),
    countryCode = "RU",
)

val berlinerPhilharmonikerArtistMusicBrainzModel = ArtistMusicBrainzModel(
    id = "dea28aa9-1086-4ffa-8739-0ccc759de1ce",
    name = "Berliner Philharmoniker",
    disambiguation = "",
    sortName = "Berliner Philharmoniker",
    type = "Orchestra",
    typeId = "a0b36c92-3eb1-3839-a4f9-4799823f54a5",
    lifeSpan = LifeSpanMusicBrainzModel(
        begin = "1882",
        ended = false,
    ),
    isnis = listOf("0000000120191498", "0000000121144247"),
    countryCode = null,
    area = AreaMusicBrainzModel(
        id = "c9ac1239-e832-41bc-9930-e252a1fd1105",
        name = "Berlin",
        countrySubDivisionCodes = listOf("DE-BE"),
    ),
)

val aimerArtistMusicBrainzModel = ArtistMusicBrainzModel(
    id = "9388cee2-7d57-4598-905f-106019b267d3",
    name = "Aimer",
    disambiguation = "Japanese pop singer",
    sortName = "Aimer",
    type = "Person",
    typeId = "b6e035f4-3ce9-331c-97df-83397230b0df",
    ipis = listOf("00656032457"),
    isnis = listOf("0000000407348085"),
)
