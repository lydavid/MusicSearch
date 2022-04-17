package ly.david.mbjc.data.persistence

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ly.david.mbjc.data.Recording
import ly.david.mbjc.data.network.MusicBrainzResource
import ly.david.mbjc.data.network.RecordingMusicBrainzModel

@Entity(tableName = "recordings")
internal data class RecordingRoomModel(
    @PrimaryKey
    @ColumnInfo(name = "id")
    override val id: String,
    @ColumnInfo(name = "title")
    override val name: String,
    @ColumnInfo(name = "first_release_date")
    override val date: String? = null,
    @ColumnInfo(name = "disambiguation")
    override val disambiguation: String = "",
    @ColumnInfo(name = "length")
    override val length: Int? = null,
    @ColumnInfo(name = "video")
    override val video: Boolean = false,

    // TODO: need linking tables 
//    @ColumnInfo(name = "artist_credit") val artistCredits: List<ArtistCreditMusicBrainzModel>? = null,
//    @ColumnInfo(name = "relations") val relations: List<Relation>? = null
) : RoomModel(), Recording

// TODO: because relation model can contain any of [artist, label, work, ...], we might only want to store a
//  formatted description of it, its id, and its resource, so that we can deeplink to its page.
internal fun RecordingMusicBrainzModel.toRecordingRoomModel() =
    RecordingRoomModel(
        id = id,
        name = name,
        date = date,
        disambiguation = disambiguation,
        length = length,
        video = video
    )

@Entity(tableName = "relations")
internal data class RelationRoomModel(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0,

    @ColumnInfo(name = "description")
    val description: String,
    @ColumnInfo(name = "resource")
    val resource: MusicBrainzResource, // TODO: Destination (superset [soon TM])?
    @ColumnInfo(name = "resource_id")
    val resourceId: String,
)
