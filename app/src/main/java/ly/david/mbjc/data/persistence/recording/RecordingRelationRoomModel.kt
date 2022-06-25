package ly.david.mbjc.data.persistence.recording

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import ly.david.mbjc.data.network.MusicBrainzResource
import ly.david.mbjc.data.network.RelationMusicBrainzModel
import ly.david.mbjc.data.network.getFormattedAttributesForDisplay
import ly.david.mbjc.data.persistence.RecordingRoomModel

// TODO: instead of this, could we just have a linking table from Recording to [Artist, Label, Place, Work, etc] ?
//  maybe try it eventually. For now, let's get something working first.
//  If we don't plan to display items like we do for Recording, let's just make this Recording specific for now.
// TODO: need a linking table back to recording or whatever else
//  relation_id: Long
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

    // TODO: rather than linking to this which is susceptible to change, link it to resource_id
    //  assuming it will be unique. If it is not unique between resources, it is for sure unique for a given resource.
    //  otherwise lookup uri wouldn't work
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
                place.name.orEmpty()
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

        // TODO: handle rest

        // TODO: handle urls, should just open that url in browser
        //  since we want to support full offline after returning to a screen, we need to save this url.
        //  Either save the url in the relation object, or store an id to the url in a urls table.
        //  Upon navigation to a "url screen", we will instead open the url in the user's browser of choice.
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
        resource = targetType
    )
}
