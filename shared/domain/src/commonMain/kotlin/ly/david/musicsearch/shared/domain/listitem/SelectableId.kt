package ly.david.musicsearch.shared.domain.listitem

import ly.david.musicsearch.shared.domain.parcelize.CommonParcelable
import ly.david.musicsearch.shared.domain.parcelize.Parcelize

/**
 * @param id Should be the unique [EntityListItemModel.id].
 * @param recordingId For [TrackListItemModel].
 */
@Parcelize
data class SelectableId(
    val id: String,
    val recordingId: String? = null,
) : CommonParcelable
