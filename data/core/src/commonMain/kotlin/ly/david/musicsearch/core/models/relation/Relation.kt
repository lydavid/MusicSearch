package ly.david.musicsearch.core.models.relation

import ly.david.musicsearch.core.models.NameWithDisambiguation
import ly.david.musicsearch.core.models.network.MusicBrainzEntity

interface Relation : NameWithDisambiguation {
    override val name: String
    override val disambiguation: String?
    val linkedEntityId: String
    val linkedEntity: MusicBrainzEntity
    val label: String
    val attributes: String?
    val additionalInfo: String?
}
