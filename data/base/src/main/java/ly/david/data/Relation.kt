package ly.david.data

import ly.david.data.network.MusicBrainzEntity

interface Relation : NameWithDisambiguation {
    override val name: String
    override val disambiguation: String?
    val linkedEntityId: String
    val linkedEntity: MusicBrainzEntity
    val label: String
    val attributes: String?
    val additionalInfo: String?
}
