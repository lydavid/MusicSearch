package ly.david.musicsearch.shared.domain.recording

import ly.david.musicsearch.shared.domain.listitem.RecordingListItemModel

sealed interface RecordingsMatchingIsrcResult {
    data class Success(val recordings: List<RecordingListItemModel>) : RecordingsMatchingIsrcResult
    data object Loading : RecordingsMatchingIsrcResult
    data object NoUnmappedIsrc : RecordingsMatchingIsrcResult
    data object NoMatchingRecordings : RecordingsMatchingIsrcResult
}
