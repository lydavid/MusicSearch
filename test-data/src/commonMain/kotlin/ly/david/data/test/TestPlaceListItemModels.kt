package ly.david.data.test

import ly.david.musicsearch.shared.domain.LifeSpanUiModel
import ly.david.musicsearch.shared.domain.listitem.PlaceListItemModel
import ly.david.musicsearch.shared.domain.place.CoordinatesUiModel

val budokanPlaceListItemModel = PlaceListItemModel(
    id = "4d43b9d8-162d-4ac5-8068-dfb009722484",
    name = "日本武道館",
    address = "〒102-8321 東京都千代田区北の丸公園2-3",
    type = "Indoor arena",
    lifeSpan = LifeSpanUiModel(
        begin = "1964-10-03",
        ended = false,
    ),
    coordinates = CoordinatesUiModel(
        longitude = 139.75,
        latitude = 35.69333,
    ),
)

val tokyoInternationForumHallAPlaceListItemModel = PlaceListItemModel(
    id = "5d3c4d79-4934-4029-87b3-25f907c8f729",
    name = "東京国際フォーラム・ホールA",
    address = "〒100-0005 東京都千代田区丸の内三丁目5番1号",
    type = "Venue",
    lifeSpan = LifeSpanUiModel(
        begin = "1997-01-10",
        ended = false,
    ),
    coordinates = CoordinatesUiModel(
        longitude = 139.7642,
        latitude = 35.676925,
    ),
)

val tokyoInternationForumPlaceListItemModel = PlaceListItemModel(
    id = "623d61ce-d422-4d3a-b6bb-c02cd64c715d",
    name = "東京国際フォーラム",
    disambiguation = "complex; use ONLY if no more specific venue info is available!",
    address = "〒100-0005 東京都千代田区丸の内三丁目5番1号",
    type = "Other",
    lifeSpan = LifeSpanUiModel(
        begin = "1997-01-10",
        ended = false,
    ),
    coordinates = CoordinatesUiModel(
        longitude = 139.7642,
        latitude = 35.676925,
    ),
)
