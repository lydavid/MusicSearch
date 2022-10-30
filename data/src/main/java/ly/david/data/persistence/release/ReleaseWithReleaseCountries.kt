package ly.david.data.persistence.release

import androidx.room.DatabaseView
import androidx.room.Embedded
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
    // This question is what we need: https://stackoverflow.com/q/58681414 (ie. get a 3rd column from junction table)
    //  but the answer doesn't address it
    @Relation(
        parentColumn = "id", // release.id
        entity = LabelWithCatalog::class,
        entityColumn = "release_id", // label.id
//        associateBy = Junction(
//            value = ReleaseLabel::class,
//            parentColumn = "release_id",
//            entityColumn = "label_id"
//        )
    )
    val labels: List<LabelWithCatalog>
): RoomModel

// TODO: still need to pick the ones that matches release id
//data class LabelWithCatalog(
//    @Embedded
//    val label: LabelRoomModel,
//
//    @Relation(
//        parentColumn = "id",
//        entity = ReleaseLabel::class,
//        entityColumn = "label_id",
//        projection = ["catalog_number"]
//    )
//    val catalogNumber: String
//)

@DatabaseView("""
    SELECT l.*, rl.*
    FROM labels l
    INNER JOIN releases_labels rl ON l.id = rl.label_id
""")
data class LabelWithCatalog(
    @Embedded
    val label: LabelRoomModel,

    @Embedded
    val releaseLabel: ReleaseLabel

//    @ColumnInfo(name = "catalog_number")
//    val catalogNumber: String = ""
)
