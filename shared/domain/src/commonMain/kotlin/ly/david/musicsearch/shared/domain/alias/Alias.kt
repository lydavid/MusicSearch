package ly.david.musicsearch.shared.domain.alias

import ly.david.musicsearch.shared.domain.LifeSpan

interface Alias : LifeSpan {
    val name: String
    val isPrimary: Boolean?
    val locale: String?
    override val begin: String?
    override val end: String?
    override val ended: Boolean?
}
