package ly.david.data.persistence.releasegroup

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Relation
import ly.david.data.persistence.RoomModel
import ly.david.data.persistence.artist.credit.ArtistCreditNamesWithResource

data class ReleaseGroupWithAllData(
    @Embedded
    val releaseGroup: ReleaseGroupRoomModel,

    @Relation(
        parentColumn = "id",
        entityColumn = "resource_id"
    )
    val artistCreditNamesWithResources: List<ArtistCreditNamesWithResource>
) : RoomModel

data class ReleaseGroupForListItem(
    @Embedded
    val releaseGroup: ReleaseGroupRoomModel,

    @ColumnInfo("artist_credit_names")
    val artistCreditNames: String?,
) : RoomModel
