package ly.david.musicsearch.shared.domain.alias

data class BasicAlias(
    override val name: String,
    override val locale: String,
    override val isPrimary: Boolean,
) : Alias
