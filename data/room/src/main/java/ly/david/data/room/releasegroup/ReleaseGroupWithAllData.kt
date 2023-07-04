package ly.david.data.room.releasegroup

import androidx.room.Embedded
import androidx.room.Relation
import ly.david.data.room.RoomModel
import ly.david.data.room.artist.credit.ArtistCreditNamesWithResource
import ly.david.data.room.image.MbidImage

data class ReleaseGroupWithAllData(
    @Embedded
    val releaseGroup: ReleaseGroupRoomModel,

    @Relation(
        parentColumn = "id",
        entityColumn = "resource_id"
    )
    val artistCreditNamesWithResources: List<ArtistCreditNamesWithResource>,

    @Relation(
        entity = MbidImage::class,
        parentColumn = "id",
        entityColumn = "mbid",
        projection = ["large_url"]
    )
    val largeUrl: String?
) : RoomModel
