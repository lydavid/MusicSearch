package ly.david.data.room.releasegroup

import androidx.room.Embedded
import androidx.room.Relation
import ly.david.data.room.RoomModel
import ly.david.data.room.artist.UrlRelation
import ly.david.data.room.artist.credit.ArtistCreditNamesWithEntity
import ly.david.data.room.image.MbidImage

data class ReleaseGroupWithAllData(
    @Embedded
    val releaseGroup: ReleaseGroupRoomModel,

    @Relation(
        parentColumn = "id",
        entityColumn = "entity_id"
    )
    val artistCreditNamesWithEntities: List<ArtistCreditNamesWithEntity>,

    @Relation(
        entity = MbidImage::class,
        parentColumn = "id",
        entityColumn = "mbid",
        projection = ["large_url"]
    )
    val largeUrl: String?,

    @Relation(
        parentColumn = "id",
        entityColumn = "entity_id"
    )
    val urls: List<UrlRelation>,
) : RoomModel
