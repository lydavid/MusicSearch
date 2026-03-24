package ly.david.musicsearch.shared.domain.listitem

/**
 * @param id Should be the unique [EntityListItemModel.id].
 * @param recordingId For [TrackListItemModel].
 */
data class SelectableId(
    val id: String,
    val recordingId: String? = null,
)
