package ly.david.data.core.area

import ly.david.data.core.LifeSpan
import ly.david.data.core.NameWithDisambiguation

interface Area : NameWithDisambiguation {
    val id: String
    override val name: String
    val sortName: String
    override val disambiguation: String?
    val type: String?
    val lifeSpan: LifeSpan?

    // val isoCodes: List<String>?
}

// Although this could be an enum, we currently only make use of countries.
// This way we can just display any area types and not worry about crashes if one isn't part of our enum.
object AreaType {
    const val COUNTRY = "Country"
    const val WORLDWIDE = "[Worldwide]"
}

fun Area.showReleases(): Boolean =
    type == AreaType.COUNTRY || name == AreaType.WORLDWIDE

data class ReleaseEvent(
    val id: String,
    val name: String,
    val date: String?,
    val countryCode: String?,
)
