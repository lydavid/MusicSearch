package ly.david.mbjc.data

internal interface Work: NameWithDisambiguation {
    val id: String
    override val name: String
    override val disambiguation: String?

    val type: String?
//    val typeId: String?
    val language: String?
//    val languages: List<String>?
}
