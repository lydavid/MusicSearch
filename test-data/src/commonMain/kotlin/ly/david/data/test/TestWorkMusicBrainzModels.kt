package ly.david.data.test

import ly.david.musicsearch.data.musicbrainz.models.common.AliasMusicBrainzNetworkModel
import ly.david.musicsearch.data.musicbrainz.models.core.WorkAttributeMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.WorkMusicBrainzNetworkModel

val skycladObserverWorkMusicBrainzModel = WorkMusicBrainzNetworkModel(
    id = "b04a6906-237c-3611-a5c6-b8bcd2627327",
    name = "スカイクラッドの観測者",
    type = "Song",
    typeId = "f061270a-2fd6-32f1-a641-f0f8676d14e6",
    languages = listOf("jpn"),
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

val hackingToTheGateWorkMusicBrainzModel = WorkMusicBrainzNetworkModel(
    id = "a2f313bc-aadf-4d77-b367-e4f0d8a8e21c",
    name = "Hacking to the Gate",
    type = "Song",
    typeId = "f061270a-2fd6-32f1-a641-f0f8676d14e6",
    languages = listOf("jpn"),
    iswcs = listOf(
        "T-102.018.970-0",
    ),
    attributes = listOf(
        WorkAttributeMusicBrainzModel(
            value = "16662182-001",
            type = "GEMA ID",
            typeId = "01eeee67-f514-3801-bdce-279e04872f91",
        ),
        WorkAttributeMusicBrainzModel(
            value = "13500094",
            type = "COMPASS ID",
            typeId = "5ea37343-be89-4cd0-8a37-f471738df641",
        ),
        WorkAttributeMusicBrainzModel(
            value = "702-2048-4",
            type = "JASRAC ID",
            typeId = "31048fcc-3dbb-3979-8f85-805afb933e0c",
        ),
        WorkAttributeMusicBrainzModel(
            value = "905855922",
            type = "ASCAP ID",
            typeId = "d833318c-6c6a-370e-8b16-9cb15873ba76",
        ),
    ),
)

val underPressureWorkMusicBrainzModel = WorkMusicBrainzNetworkModel(
    id = "4e6a04c3-6897-391d-8e8c-1da7a6dce1ca",
    name = "Under Pressure",
    type = "Song",
    typeId = "f061270a-2fd6-32f1-a641-f0f8676d14e6",
    languages = listOf("eng"),
    iswcs = listOf(
        "T-010.475.727-8",
        "T-011.226.466-0",
    ),
    attributes = listOf(
        WorkAttributeMusicBrainzModel(
            value = "2182263",
            type = "ACAM ID",
            typeId = "955305a2-58ec-4c64-94f7-7fb9b209416c",
        ),
        WorkAttributeMusicBrainzModel(
            value = "2406479",
            type = "ACAM ID",
            typeId = "955305a2-58ec-4c64-94f7-7fb9b209416c",
        ),
        // many omitted for now
    ),
)

val starmanWorkMusicBrainzModel = WorkMusicBrainzNetworkModel(
    id = "4491f749-d06a-348c-aa58-a288d2eafa5f",
    name = "Starman",
    type = "Song",
    typeId = "f061270a-2fd6-32f1-a641-f0f8676d14e6",
    languages = listOf("eng"),
    iswcs = listOf(
        "T-900.196.834-9",
        "T-011.559.559-9",
    ),
    attributes = listOf(
        // many omitted for now
    ),
)

val dontStopMeNowWorkMusicBrainzModel = WorkMusicBrainzNetworkModel(
    id = "8f217f03-fd55-366c-b80e-66424027dc29",
    name = "Don’t Stop Me Now",
    type = "Song",
    typeId = "f061270a-2fd6-32f1-a641-f0f8676d14e6",
    languages = listOf("eng"),
    iswcs = listOf(
        "T-010.115.271-9",
        "T-010.180.479-8",
    ),
    attributes = listOf(
        // many omitted for now
    ),
)

val cruelAngelThesisWorkMusicBrainzModel = WorkMusicBrainzNetworkModel(
    id = "343dbbe6-d9ce-3853-8d8d-230734c0424f",
    name = "残酷な天使のテーゼ",
    disambiguation = "Neon Genesis Evangelion",
    type = "Song",
    typeId = "f061270a-2fd6-32f1-a641-f0f8676d14e6",
    languages = listOf("jpn"),
    iswcs = listOf(
        "T-101.261.638-3",
    ),
    aliases = listOf(
        AliasMusicBrainzNetworkModel(
            name = "The Cruel Angel’s Thesis",
            isPrimary = true,
            locale = "en",
        ),
        AliasMusicBrainzNetworkModel(
            name = "Zankoku na tenshi no these",
            isPrimary = true,
            locale = "es",
        ),
        AliasMusicBrainzNetworkModel(
            name = "残酷な天使のテーゼ",
            isPrimary = true,
            locale = "ja",
        ),
        AliasMusicBrainzNetworkModel(
            name = "残酷天使的行动纲领",
            isPrimary = true,
            locale = "zh",
        ),
        AliasMusicBrainzNetworkModel(
            name = "잔혹한 천사의 테제",
            isPrimary = true,
            locale = "ko",
        ),
    ),
)
