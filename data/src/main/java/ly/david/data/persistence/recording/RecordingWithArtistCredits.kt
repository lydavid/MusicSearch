package ly.david.data.persistence.recording

import androidx.room.Embedded
import androidx.room.Relation
import ly.david.data.persistence.RoomModel
import ly.david.data.persistence.artist.credit.ArtistCreditNamesWithResource

data class RecordingWithArtistCredits(
    @Embedded
    val recording: RecordingRoomModel,

    @Relation(
        parentColumn = "id",
        entityColumn = "resource_id"
    )
    val artistCreditNamesWithResources: List<ArtistCreditNamesWithResource>
) : RoomModel
