package ly.david.musicsearch.shared.domain.releasegroup

data class ReleaseGroupTypeCount(
    override val primaryType: String? = null,
    override val secondaryTypes: List<String>? = null,
    val count: Int,
) : ReleaseGroupTypes
