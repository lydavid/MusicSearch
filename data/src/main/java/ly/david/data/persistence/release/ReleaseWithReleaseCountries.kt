package ly.david.data.persistence.release

import androidx.room.Embedded
import androidx.room.Relation
import ly.david.data.persistence.RoomModel
import ly.david.data.persistence.area.ReleaseCountry

data class ReleaseWithReleaseCountries(
    @Embedded
    val release: ReleaseRoomModel,

    // TODO: how to join with Area?
    // TODO: how to additionally join with iso? after this we can remove country code from release
    @Relation(
        parentColumn = "id",
        entityColumn = "release_id",
//        associateBy = Junction(ReleaseCountry::class)
    )
    val releaseEvents: List<ReleaseCountry>
): RoomModel
