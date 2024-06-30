package ly.david.musicsearch.core.models.area

import ly.david.musicsearch.core.models.LifeSpan
import ly.david.musicsearch.core.models.NameWithDisambiguation

interface Area : NameWithDisambiguation {
    val id: String
    override val name: String
    val sortName: String
    override val disambiguation: String?
    val type: String?
    val lifeSpan: LifeSpan?

    // val isoCodes: List<String>?
}

object AreaType {
    const val COUNTRY = "Country"
}

data class ReleaseEvent(
    val id: String,
    val name: String,
    val date: String?,
    val countryCode: String?,
)
