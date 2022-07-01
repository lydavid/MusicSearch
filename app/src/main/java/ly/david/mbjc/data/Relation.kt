package ly.david.mbjc.data

import ly.david.mbjc.data.network.MusicBrainzResource

internal interface Relation {
    val linkedResourceId: String
    val linkedResource: MusicBrainzResource
    val label: String
    val name: String
    val disambiguation: String?
    val attributes: String?
    val additionalInfo: String?
}
