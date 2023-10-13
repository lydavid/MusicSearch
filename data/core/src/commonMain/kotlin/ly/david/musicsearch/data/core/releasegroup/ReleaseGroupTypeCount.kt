package ly.david.musicsearch.data.core.releasegroup

data class ReleaseGroupTypeCount(
    override val primaryType: String? = null,
    override val secondaryTypes: List<String>? = null,
    val count: Int,
) : ReleaseGroupTypes
