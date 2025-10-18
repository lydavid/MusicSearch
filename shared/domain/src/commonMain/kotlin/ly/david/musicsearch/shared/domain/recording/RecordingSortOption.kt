package ly.david.musicsearch.shared.domain.recording

import ly.david.musicsearch.shared.domain.list.SortableOption

enum class RecordingSortOption : SortableOption {
    None,
    NameAscending,
    NameDescending,
    DateAscending,
    DateDescending,
    ListensAscending,
    ListensDescending,
}
