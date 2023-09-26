package ly.david.data.room.area

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

/**
 * Represents a [country code](https://en.wikipedia.org/wiki/List_of_ISO_3166_country_codes).
 *
 * Primarily used for [toFlagEmoji]
 *
 * Following MB's schema, an area can have multiple country codes, but we haven't found an example of one.
 * For compatibility, we will do it the same way.
 *
 * Note that there are countries without an area code (eg. "Kingdom of the Netherlands" and "Somaliland").
 * "Netherlands" itself has the code NL.
 */
@Entity(
    tableName = "iso_3166_1",
    primaryKeys = ["area_id", "code"],
    foreignKeys = [
        ForeignKey(
            // TODO: ref countries_areas instead
            entity = AreaRoomModel::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("area_id"),
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class CountryCode(
    @ColumnInfo(name = "area_id")
    val areaId: String,

    @ColumnInfo(name = "code")
    val code: String,
)
