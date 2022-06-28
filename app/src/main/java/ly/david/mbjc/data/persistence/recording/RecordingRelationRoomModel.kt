package ly.david.mbjc.data.persistence.recording

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import ly.david.mbjc.data.getDisplayNames
import ly.david.mbjc.data.getLifeSpanForDisplay
import ly.david.mbjc.data.network.MusicBrainzResource
import ly.david.mbjc.data.network.RelationMusicBrainzModel
import ly.david.mbjc.data.network.getFormattedAttributesForDisplay
import ly.david.mbjc.data.persistence.RecordingRoomModel
import ly.david.mbjc.ui.common.transformThisIfNotNullOrEmpty

// TODO: can generalize recording_id to

// TODO: instead of this, could we just have a linking table from Recording to [Artist, Label, Place, Work, etc] ?
//  maybe try it eventually. For now, let's get something working first.
//  If we don't plan to display items like we do for Recording, let's just make this Recording specific for now.
@Entity(
    tableName = "recordings_relations",
    primaryKeys = ["recording_id", "linked_resource_id", "order"],
    foreignKeys = [
        ForeignKey(
            entity = RecordingRoomModel::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("recording_id"),
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ]
)
internal data class RecordingRelationRoomModel(
    @ColumnInfo(name = "recording_id")
    val recordingId: String,

    // TODO: can we make it nullable so that we don't pass url id?
    @ColumnInfo(name = "linked_resource_id")
    val linkedResourceId: String,

    // TODO: an artist can appear multiple times similar to artist credits
    //  for now, we'll use order which is the order we insert it. But we probably won't display it in this order.
    //  This is not necessarily the order it's displayed on MB website.
    @ColumnInfo(name = "order")
    val order: Int,

    /**
     * [RelationMusicBrainzModel.type].
     */
    @ColumnInfo(name = "label")
    val label: String,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "disambiguation")
    val disambiguation: String? = null,

    /**
     * Combined [RelationMusicBrainzModel.attributes].
     */
    @ColumnInfo(name = "attributes")
    val attributes: String? = null,

    /**
     * Includes things like:
     * - by Artists
     * - in Area
     * - (in 1970-01)
     * - (order: 8)
     */
    @ColumnInfo(name = "additional_info")
    val additionalInfo: String? = null,

    @ColumnInfo(name = "resource")
    val resource: MusicBrainzResource,
)

/**
 * We cannot guarantee that a [RecordingRelationRoomModel] will be created in the scenario that target-type points to a resource
 * but that object is null. It's possible that this is never the case, but our models are currently structured such
 * that any of them are nullable.
 */
internal fun RelationMusicBrainzModel.toRecordingRelationRoomModel(
    recordingId: String,
    order: Int,
): RecordingRelationRoomModel? {

    val resourceId: String
    val name: String
    val disambiguation: String?
    var additionalInfo: String? = null
    when (targetType) {
        MusicBrainzResource.ARTIST -> {
            if (artist == null) return null
            resourceId = artist.id
            name = if (targetCredit.isNullOrEmpty()) {
                artist.name
            } else {
                targetCredit
            }
            disambiguation = artist.disambiguation
            additionalInfo = getLifeSpanForDisplay().transformThisIfNotNullOrEmpty { "($it)" }
        }
        MusicBrainzResource.RECORDING -> {
            if (recording == null) return null
            resourceId = recording.id
            name = if (targetCredit.isNullOrEmpty()) {
                recording.name
            } else {
                targetCredit
            }
            disambiguation = recording.disambiguation
            additionalInfo = recording.artistCredits.getDisplayNames().transformThisIfNotNullOrEmpty { "by $it" } +
                getLifeSpanForDisplay().transformThisIfNotNullOrEmpty { "($it)" }
        }
        MusicBrainzResource.LABEL -> {
            if (label == null) return null
            resourceId = label.id
            name = if (targetCredit.isNullOrEmpty()) {
                label.name.orEmpty()
            } else {
                targetCredit
            }
            disambiguation = label.disambiguation
        }
        MusicBrainzResource.PLACE -> {
            if (place == null) return null
            resourceId = place.id
            name = if (targetCredit.isNullOrEmpty()) {
                place.name
            } else {
                targetCredit
            }
            disambiguation = place.disambiguation
        }
        MusicBrainzResource.WORK -> {
            if (work == null) return null
            resourceId = work.id
            name = if (targetCredit.isNullOrEmpty()) {
                work.name
            } else {
                targetCredit
            }
            disambiguation = work.disambiguation
        }
        // TODO: handle urls, should just open that url in browser
        //  since we want to support full offline after returning to a screen, we need to save this url.
        //  Either save the url in the relation object, or store an id to the url in a urls table.
        //  Upon navigation to a "url screen", we will instead open the url in the user's browser of choice.
        MusicBrainzResource.URL -> {
            if (url == null) return null
            resourceId = url.id
            name = url.resource
            disambiguation = null
        }

        // TODO: handle rest


        else -> {
            return null
        }
    }

    return RecordingRelationRoomModel(
        recordingId = recordingId,
        linkedResourceId = resourceId,
        order = order,
        label = type,
        name = name,
        disambiguation = disambiguation,
        attributes = getFormattedAttributesForDisplay(),
        additionalInfo = additionalInfo,
        resource = targetType
    )
}
