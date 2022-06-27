package ly.david.mbjc.data

import ly.david.mbjc.data.network.Coordinates

internal interface Place : NameWithDisambiguation {
    val id: String
    override val name: String
    override val disambiguation: String?
    val address: String
    val type: String?
//    val typeId: String?
    val coordinates: Coordinates?
    val lifeSpan: LifeSpan?
}
