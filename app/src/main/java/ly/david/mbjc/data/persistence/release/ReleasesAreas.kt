package ly.david.mbjc.data.persistence.release

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

// TODO:  * A release may be released in the same country multiple times?
//  that would be the only reason to make this a separate table from releases_areas
/**
 * This records a single release event for a release.
 */
@Entity(
    tableName = "releases_countries",
    primaryKeys = ["release_id", "country_id"],
    foreignKeys = [
        ForeignKey(
            entity = ReleaseRoomModel::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("release_id"),
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ]
)
internal data class ReleasesCountries(
    @ColumnInfo(name = "release_id")
    val releaseId: String,

    @ColumnInfo(name = "country_id")
    val countryId: String,
)

// TODO: do we need this table?
//@Entity(
//    tableName = "releases_areas",
//    primaryKeys = ["release_id", "area_id"],
//    foreignKeys = [
//        ForeignKey(
//            entity = ReleaseRoomModel::class,
//            parentColumns = arrayOf("id"),
//            childColumns = arrayOf("release_id"),
//            onUpdate = ForeignKey.CASCADE,
//            onDelete = ForeignKey.CASCADE
//        )
//    ]
//)
//internal data class ReleasesAreas(
//    @ColumnInfo(name = "release_id")
//    val releaseId: String,
//
//    @ColumnInfo(name = "area_id")
//    val areaId: String,
//)

//@Dao
//internal abstract class ReleasesAreasDao : BaseDao<ReleasesAreas> {
//
//}
