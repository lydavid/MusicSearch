package ly.david.musicsearch.shared.domain.alias

data class BasicAlias(
    override val name: String,
    override val locale: String,
    override val isPrimary: Boolean,
    val type: AliasType? = null,
    override val begin: String = "",
    override val end: String = "",
    override val ended: Boolean = false,
) : Alias
