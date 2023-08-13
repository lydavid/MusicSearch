package ly.david.data.room.recording

import androidx.room.Embedded
import androidx.room.Relation
import ly.david.data.room.RoomModel
import ly.david.data.room.artist.UrlRelation
import ly.david.data.room.artist.credit.ArtistCreditNamesWithEntity

data class RecordingWithAllData(
    @Embedded
    val recording: RecordingRoomModel,

    @Relation(
        parentColumn = "id",
        entityColumn = "entity_id"
    )
    val artistCreditNamesWithEntities: List<ArtistCreditNamesWithEntity>,

    @Relation(
        parentColumn = "id",
        entityColumn = "entity_id"
    )
    val urls: List<UrlRelation>,
) : RoomModel
