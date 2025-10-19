package ly.david.musicsearch.shared.domain.recording

import ly.david.musicsearch.shared.domain.list.SortableOption

enum class RecordingSortOption : SortableOption {
    InsertedAscending,
    InsertedDescending,
    NameAscending,
    NameDescending,
    DateAscending,
    DateDescending,
    ListensAscending,
    ListensDescending,
}
