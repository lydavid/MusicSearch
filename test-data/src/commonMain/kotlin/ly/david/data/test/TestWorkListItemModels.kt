package ly.david.data.test

import kotlinx.collections.immutable.persistentListOf
import ly.david.musicsearch.shared.domain.alias.BasicAlias
import ly.david.musicsearch.shared.domain.listitem.WorkListItemModel

val skycladObserverWorkListItemModel = WorkListItemModel(
    id = "b04a6906-237c-3611-a5c6-b8bcd2627327",
    name = "スカイクラッドの観測者",
    type = "Song",
    languages = persistentListOf(
        "jpn",
    ),
    iswcs = persistentListOf(
        "T-101.979.913-2",
    ),
)

val hackingToTheGateWorkListItemModel = WorkListItemModel(
    id = "a2f313bc-aadf-4d77-b367-e4f0d8a8e21c",
    name = "Hacking to the Gate",
    type = "Song",
    languages = persistentListOf(
        "jpn",
    ),
    iswcs = persistentListOf(
        "T-102.018.970-0",
    ),
)

val underPressureWorkListItemModel = WorkListItemModel(
    id = "4e6a04c3-6897-391d-8e8c-1da7a6dce1ca",
    name = "Under Pressure",
    type = "Song",
    languages = persistentListOf(
        "eng",
    ),
    iswcs = persistentListOf(
        "T-010.475.727-8",
        "T-011.226.466-0",
    ),
)

val starmanWorkListItemModel = WorkListItemModel(
    id = "4491f749-d06a-348c-aa58-a288d2eafa5f",
    name = "Starman",
    type = "Song",
    languages = persistentListOf(
        "eng",
    ),
    iswcs = persistentListOf(
        "T-011.559.559-9",
        "T-900.196.834-9",
    ),
    attributes = persistentListOf(
        // many omitted for now
    ),
)

val dontStopMeNowWorkListItemModel = WorkListItemModel(
    id = "8f217f03-fd55-366c-b80e-66424027dc29",
    name = "Don’t Stop Me Now",
    type = "Song",
    languages = persistentListOf(
        "eng",
    ),
    iswcs = persistentListOf(
        "T-010.115.271-9",
        "T-010.180.479-8",
    ),
    attributes = persistentListOf(
        // many omitted for now
    ),
)

val cruelAngelThesisWorkListItemModel = WorkListItemModel(
    id = "343dbbe6-d9ce-3853-8d8d-230734c0424f",
    name = "残酷な天使のテーゼ",
    disambiguation = "Neon Genesis Evangelion",
    type = "Song",
    languages = persistentListOf("jpn"),
    iswcs = persistentListOf(
        "T-101.261.638-3",
    ),
    aliases = persistentListOf(
        BasicAlias(
            name = "The Cruel Angel’s Thesis",
            isPrimary = true,
            locale = "en",
        ),
        BasicAlias(
            name = "Zankoku na tenshi no these",
            isPrimary = true,
            locale = "es",
        ),
        BasicAlias(
            name = "残酷な天使のテーゼ",
            isPrimary = true,
            locale = "ja",
        ),
        BasicAlias(
            name = "残酷天使的行动纲领",
            isPrimary = true,
            locale = "zh",
        ),
        BasicAlias(
            name = "잔혹한 천사의 테제",
            isPrimary = true,
            locale = "ko",
        ),
    ),
)
