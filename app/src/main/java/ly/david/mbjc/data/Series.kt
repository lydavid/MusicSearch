package ly.david.mbjc.data

internal interface Series : NameWithDisambiguation {
    val id: String
    override val name: String
    override val disambiguation: String?
    val type: String?
//    val typeId: String?
}
