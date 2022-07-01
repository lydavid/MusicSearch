package ly.david.mbjc.data.domain

import ly.david.mbjc.data.network.MusicBrainzResource
import ly.david.mbjc.data.persistence.recording.RecordingRelationRoomModel

internal data class RecordingRelationUiModel(
    val recordingId: String,
    val linkedResourceId: String,
    val order: Int,
    val label: String,
    val name: String,
    val disambiguation: String? = null,
    val attributes: String? = null,
    val additionalInfo: String? = null,
    val resource: MusicBrainzResource,
): UiModel()

internal fun RecordingRelationRoomModel.toRecordingRelationUiModel() =
    RecordingRelationUiModel(
        recordingId, linkedResourceId, order, label, name, disambiguation, attributes, additionalInfo, resource
    )
