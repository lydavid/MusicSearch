package ly.david.musicsearch.shared.domain.releasegroup

data class ReleaseGroupForRelease(
    override val id: String,
    override val name: String,
    override val firstReleaseDate: String,
    override val disambiguation: String = "",
    override val primaryType: String? = null,
    override val secondaryTypes: List<String>? = null,
) : ReleaseGroup
