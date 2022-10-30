package ly.david.data.persistence.release

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import ly.david.data.persistence.RoomModel
import ly.david.data.persistence.area.ReleaseCountry
import ly.david.data.persistence.label.LabelRoomModel
import ly.david.data.persistence.label.ReleaseLabel

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

/**
 * Don't use this when paging releases.
 */
data class ReleaseWithAllData(
    @Embedded
    val release: ReleaseRoomModel,

    @Relation(
        parentColumn = "id",
        entityColumn = "release_id",
    )
    val releaseEvents: List<ReleaseCountry>,

    // TODO: can we have a view that combines label and catalog num?
    @Relation(
        parentColumn = "id", // release.id
        entity = LabelRoomModel::class,
        entityColumn = "id", // label.id
        associateBy = Junction(
            value = ReleaseLabel::class,
            parentColumn = "release_id",
            entityColumn = "label_id"
        )
    )
    val labels: List<LabelRoomModel>
): RoomModel
