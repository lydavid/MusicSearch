package ly.david.musicsearch.shared.domain.alias

import ly.david.musicsearch.shared.domain.parcelize.CommonParcelable
import ly.david.musicsearch.shared.domain.parcelize.Parcelize

@Parcelize
data class BasicAlias(
    override val name: String,
    override val locale: String,
    override val isPrimary: Boolean,
    val type: AliasType? = null,
    override val begin: String = "",
    override val end: String = "",
    override val ended: Boolean = false,
) : Alias, CommonParcelable
