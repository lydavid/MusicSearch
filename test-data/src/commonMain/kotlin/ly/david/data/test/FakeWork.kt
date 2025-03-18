package ly.david.data.test

import ly.david.musicsearch.data.musicbrainz.models.core.WorkAttributeMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.WorkMusicBrainzModel

val skycladObserverWorkMusicBrainzModel = WorkMusicBrainzModel(
    id = "b04a6906-237c-3611-a5c6-b8bcd2627327",
    name = "スカイクラッドの観測者",
    type = "Song",
    typeId = "f061270a-2fd6-32f1-a641-f0f8676d14e6",
    language = "jpn",
    iswcs = listOf(
        "T-101.979.913-2",
    ),
    attributes = listOf(
        WorkAttributeMusicBrainzModel(
            value = "18230080-001",
            type = "GEMA ID",
            typeId = "01eeee67-f514-3801-bdce-279e04872f91",
        ),
        WorkAttributeMusicBrainzModel(
            value = "10530267",
            type = "COMPASS ID",
            typeId = "5ea37343-be89-4cd0-8a37-f471738df641",
        ),
        WorkAttributeMusicBrainzModel(
            value = "C-1264052606",
            type = "CASH ID",
            typeId = "9e0765a1-1505-3ca9-9147-8dcbb0aa9cec",
        ),
        WorkAttributeMusicBrainzModel(
            value = "162-6010-4",
            type = "JASRAC ID",
            typeId = "31048fcc-3dbb-3979-8f85-805afb933e0c",
        ),
    ),
)
