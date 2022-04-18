package ly.david.mbjc.data.persistence

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ly.david.mbjc.data.network.MusicBrainzResource
import ly.david.mbjc.data.network.RelationMusicBrainzModel
import ly.david.mbjc.ui.navigation.Destination
import ly.david.mbjc.ui.navigation.toDestination

// TODO: we could put recording id here, but then each of these relationships will be 1-to-many from each recording/work/etc
//  this could work if we don't plan to store rels for anything else
//  but even then, that would mean the same relations rows will be repeated for every recording using the same ones
//  so we should NOT store recording_id here

// TODO: one advantage of using a model like this separate from querying all artists/places/labels/etc related to a recording
//  is that it returns the exact info needed. Well, technically we could query artists and map to a ui object with only the info we want.
//  The biggest advantage is that some artists are creditd under a different name for different recordings, this will allow us to store/get that.

// TODO: if we're using an auto id for relation, how will we know to not insert a relation that already exists?
@Entity(tableName = "relations")
internal data class RelationRoomModel(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0,

    @ColumnInfo(name = "description")
    val description: String,

    /**
     * A soon-to-be superset of [MusicBrainzResource].
     * Use [toDestination] to get corresponding destination for a MB resource.
     */
    @ColumnInfo(name = "destination")
    val destination: Destination,

    @ColumnInfo(name = "resource_id")
    val resourceId: String,
)

/**
 * We cannot guarantee that a [RelationRoomModel] will be created in the scenario that target-type points to a resource
 * but that object is null. It's possible that this is never the case, but our models are currently structured such
 * that any of them are nullable.
 */
internal fun RelationMusicBrainzModel.toRelationRoomModel(): RelationRoomModel? {

    val resourceId: String
    val description: String
    when (targetType) {
        MusicBrainzResource.ARTIST -> {
            resourceId = artist?.id ?: return null

            // TODO: some entries are still empty
            description = targetCredit ?: artist.name
        }
        else -> {
            return null
        }
    }


    return RelationRoomModel(
        description = description,
        destination = targetType.toDestination(),
        resourceId = resourceId
    )
}
