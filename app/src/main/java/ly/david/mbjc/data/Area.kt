package ly.david.mbjc.data

//"area": {
//    "type": null,
//    "name": "Japan",
//    "sort-name": "Japan",
//    "disambiguation": "",
//    "id": "2db42837-c832-3c27-b4a3-08198f75693c",
//    "type-id": null,
//    "iso-3166-1-codes": [
//    "JP"
//    ]
//},
internal interface Area : NameWithDisambiguation {
    val id: String
    override val name: String
    override val disambiguation: String?
    val type: String?
    val lifeSpan: LifeSpan?

    // val isoCodes: List<String>?
}
