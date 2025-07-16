package ly.david.musicsearch.shared.domain.releasegroup

data class ReleaseGroupForRelease(
    override val id: String,
    override val name: String,
    override val disambiguation: String = "",
    override val firstReleaseDate: String,
    override val primaryType: String = "",
    override val secondaryTypes: List<String> = listOf(),
) : ReleaseGroup
