package ly.david.data.test

import ly.david.musicsearch.data.musicbrainz.models.common.LifeSpanMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.AreaMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.CoordinatesMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.PlaceMusicBrainzModel

val budokanPlaceMusicBrainzModel = PlaceMusicBrainzModel(
    id = "4d43b9d8-162d-4ac5-8068-dfb009722484",
    name = "日本武道館",
    address = "〒102-8321 東京都千代田区北の丸公園2-3",
    type = "Indoor arena",
    typeId = "a77c11f6-82fa-3cc0-9041-ac60e5f6e024",
    lifeSpan = LifeSpanMusicBrainzModel(
        begin = "1964-10-03",
        ended = false,
    ),
    coordinates = CoordinatesMusicBrainzModel(
        longitude = 139.75,
        latitude = 35.69333,
    ),
    area = AreaMusicBrainzModel(
        id = "e24c0f02-9b5a-4f4f-9fe0-f8b3e67874f8",
        name = "Kitanomaru Kōen",
        sortName = "Kitanomaru Kōen",
        type = null,
        typeId = null,
    ),
)

val tokyoInternationForumHallAPlaceMusicBrainzModel = PlaceMusicBrainzModel(
    id = "5d3c4d79-4934-4029-87b3-25f907c8f729",
    name = "東京国際フォーラム・ホールA",
    address = "〒100-0005 東京都千代田区丸の内三丁目5番1号",
    type = "Venue",
    typeId = "cd92781a-a73f-30e8-a430-55d7521338db",
    lifeSpan = LifeSpanMusicBrainzModel(
        begin = "1997-01-10",
        ended = false,
    ),
    coordinates = CoordinatesMusicBrainzModel(
        longitude = 139.7642,
        latitude = 35.676925,
    ),
    area = AreaMusicBrainzModel(
        id = "41cd0808-6e0a-4ec6-ab02-14add4db58ae",
        name = "Marunouchi",
        sortName = "Marunouchi",
        type = null,
        typeId = null,
    ),
)

val tokyoInternationForumPlaceMusicBrainzModel = PlaceMusicBrainzModel(
    id = "623d61ce-d422-4d3a-b6bb-c02cd64c715d",
    name = "東京国際フォーラム",
    disambiguation = "complex; use ONLY if no more specific venue info is available!",
    address = "〒100-0005 東京都千代田区丸の内三丁目5番1号",
    type = "Other",
    typeId = "a0df5ead-0bd6-33d8-8444-855a9f3e9970",
    lifeSpan = LifeSpanMusicBrainzModel(
        begin = "1997-01-10",
        ended = false,
    ),
    coordinates = CoordinatesMusicBrainzModel(
        longitude = 139.7642,
        latitude = 35.676925,
    ),
    area = AreaMusicBrainzModel(
        id = "41cd0808-6e0a-4ec6-ab02-14add4db58ae",
        name = "Marunouchi",
        sortName = "Marunouchi",
        type = null,
        typeId = null,
    ),
)
