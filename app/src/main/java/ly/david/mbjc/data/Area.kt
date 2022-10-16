package ly.david.mbjc.data

internal interface Area : NameWithDisambiguation {
    val id: String
    override val name: String
    override val disambiguation: String?
    val type: String?
    val lifeSpan: LifeSpan?

    // val isoCodes: List<String>?
}

// Although this could be an enum, we currently only make use of countries.
// This way we can just display any area types and not worry about crashes if one isn't part of our enum.
internal object AreaType {
    internal const val COUNTRY = "Country"
}
