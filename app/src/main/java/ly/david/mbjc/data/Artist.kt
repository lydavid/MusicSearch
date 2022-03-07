package ly.david.mbjc.data

import androidx.room.ColumnInfo
import com.squareup.moshi.Json

interface Artist : NameWithDisambiguation {
    val id: String

    override val name: String
    val sortName: String
    override val disambiguation: String?

    val type: String?
    val gender: String?
    val country: String?

    val lifeSpan: LifeSpan?
}

data class LifeSpan(
    @ColumnInfo(name = "begin")
    @Json(name = "begin")
    val begin: String? = null,
    @ColumnInfo(name = "ended")
    @Json(name = "ended")
    val ended: Boolean? = null
)
