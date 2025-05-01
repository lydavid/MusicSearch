package ly.david.musicsearch.shared.domain.relation

import ly.david.musicsearch.shared.domain.NameWithDisambiguation
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity

interface Relation : NameWithDisambiguation {
    override val name: String
    override val disambiguation: String?
    val linkedEntityId: String
    val linkedEntity: MusicBrainzEntity
    val label: String
    val attributes: String?
    val isForwardDirection: Boolean?
}
