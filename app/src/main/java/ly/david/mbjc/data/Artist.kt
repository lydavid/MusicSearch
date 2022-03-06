package ly.david.mbjc.data

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

// TODO: split
@Entity(
    tableName = "artists",
    indices = [
        Index(value = ["id"], unique = true)
    ],
//    foreignKeys = []
)
data class Artist(

    @PrimaryKey
    @ColumnInfo(name = "id")
    @Json(name = "id")
    val id: String,

    @ColumnInfo(name = "name")
    @Json(name = "name")
    override val name: String = "",
    @ColumnInfo(name = "sort-name")
    @Json(name = "sort-name")
    val sortName: String = "",
    @ColumnInfo(name = "disambiguation")
    @Json(name = "disambiguation")
    override val disambiguation: String? = null,

    @ColumnInfo(name = "type")
    @Json(name = "type")
    val type: String? = null,
    @ColumnInfo(name = "type-id")
    @Json(name = "type-id")
    val typeId: String? = null,

    @ColumnInfo(name = "gender")
    @Json(name = "gender")
    val gender: String? = null,
    @ColumnInfo(name = "gender-id")
    @Json(name = "gender-id")
    val genderId: String? = null,

    @ColumnInfo(name = "country")
    @Json(name = "country")
    val country: String? = null,

    // Allow nested fields to be part of this Room table. Good for data that doesn't require its own table.
    @Embedded
    @Json(name = "life-span") val lifeSpan: LifeSpan? = null,

    // for search responses only
    @ColumnInfo(name = "score")
    @Json(name = "score")
    val score: Int? = null,
): NameWithDisambiguation

data class LifeSpan(
    @ColumnInfo(name = "begin")
    @Json(name = "begin")
    val begin: String? = null,
    @ColumnInfo(name = "ended")
    @Json(name = "ended")
    val ended: Boolean? = null
)
