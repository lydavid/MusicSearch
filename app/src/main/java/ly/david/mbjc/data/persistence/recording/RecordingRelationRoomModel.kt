package ly.david.mbjc.data.persistence.recording

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
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
    val order: Int
)
