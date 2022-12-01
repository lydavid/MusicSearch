package ly.david.data.persistence.recording

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import ly.david.data.Recording
import ly.david.data.network.RecordingMusicBrainzModel
import ly.david.data.persistence.RoomModel
import ly.david.data.persistence.artist.credit.ArtistCreditNamesWithResource

@Entity(tableName = "recordings")
data class RecordingRoomModel(
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
) : RoomModel, Recording

fun RecordingMusicBrainzModel.toRoomModel() =
    RecordingRoomModel(
        id = id,
        name = name,
        date = date,
        disambiguation = disambiguation,
        length = length,
        video = video ?: false
    )

data class RecordingForListItem(
    @Embedded
    val recording: RecordingRoomModel,

    // This allows us to filter on this.
    @ColumnInfo("artist_credit_names")
    val artistCreditNames: String?
) : RoomModel

data class RecordingForScaffold(
    @Embedded
    val recording: RecordingRoomModel,

    @Relation(
        parentColumn = "id",
        entityColumn = "resource_id"
    )
    val artistCreditNamesWithResources: List<ArtistCreditNamesWithResource>
) : RoomModel
