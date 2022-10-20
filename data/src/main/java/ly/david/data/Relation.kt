package ly.david.data

import ly.david.data.network.MusicBrainzResource

internal interface Relation: NameWithDisambiguation {
    override val name: String
    override val disambiguation: String?
    val linkedResourceId: String
    val linkedResource: MusicBrainzResource
    val label: String
    val attributes: String?
    val additionalInfo: String?
}
