package ly.david.data.test

import kotlinx.collections.immutable.persistentListOf
import ly.david.musicsearch.shared.domain.alias.BasicAlias
import ly.david.musicsearch.shared.domain.listitem.RecordingListItemModel

val underPressureRecordingListItemModel = RecordingListItemModel(
    id = "32c7e292-14f1-4080-bddf-ef852e0a4c59",
    name = "Under Pressure",
    firstReleaseDate = "1981-10",
    length = 241000,
    formattedArtistCredits = "Queen & David Bowie",
)

val skycladObserverRecordingListItemModel = RecordingListItemModel(
    id = "6a8fc477-9b12-4001-9387-f5d936b05503",
    name = "スカイクラッドの観測者",
    firstReleaseDate = "2009-10-28",
    length = 275186,
    formattedArtistCredits = "いとうかなこ",
    aliases = persistentListOf(
        BasicAlias(
            name = "Skyclad Observer",
            locale = "en",
            isPrimary = true,
        ),
        BasicAlias(
            name = "スカイクラッドの観測者",
            locale = "ja",
            isPrimary = true,
        ),
    ),
)

val skycladObserverCoverRecordingListItemModel = RecordingListItemModel(
    id = "108a3d66-d1ef-424d-a7cb-2f53a702ce45",
    name = "スカイクラッドの観測者",
    firstReleaseDate = "2023-09-06",
    length = 273826,
    formattedArtistCredits = "Roselia×いとうかなこ",
)
