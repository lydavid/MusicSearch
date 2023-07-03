package ly.david.data.room.artist

import androidx.room.Embedded
import androidx.room.Relation
import ly.david.data.room.image.MbidImage

data class ArtistWithAllData(

    @Embedded
    val artist: ArtistRoomModel,

    @Relation(
        parentColumn = "id",
        entityColumn = "resource_id"
    )
    val urls: List<UrlRelation>,

    @Relation(
        entity = MbidImage::class,
        parentColumn = "id",
        entityColumn = "mbid",
        projection = ["image_path"]
    )
    val coverArtPath: String?
)
