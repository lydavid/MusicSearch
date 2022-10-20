package ly.david.data.persistence.area

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import ly.david.data.Area
import ly.david.data.LifeSpan
import ly.david.data.common.toFlagEmoji
import ly.david.data.network.AreaMusicBrainzModel
import ly.david.data.persistence.RoomModel

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
data class Iso3166_1(
    @ColumnInfo(name = "area_id")
    val areaId: String,

    @ColumnInfo(name = "code")
    val code: String,
)

@Entity(tableName = "areas")
data class AreaRoomModel(
    @PrimaryKey
    @ColumnInfo(name = "id")
    override val id: String,

    @ColumnInfo(name = "name")
    override val name: String,

    @ColumnInfo(name = "disambiguation")
    override val disambiguation: String?,

    @ColumnInfo(name = "type")
    override val type: String?,

    @Embedded
    override val lifeSpan: LifeSpan?,

    @ColumnInfo(name = "release_count", defaultValue = "null")
    val releaseCount: Int? = null,
) : RoomModel, Area

fun AreaMusicBrainzModel.toAreaRoomModel() =
    AreaRoomModel(
        id = id,
        name = name,
        disambiguation = disambiguation,
        type = type,
        lifeSpan = lifeSpan
    )
