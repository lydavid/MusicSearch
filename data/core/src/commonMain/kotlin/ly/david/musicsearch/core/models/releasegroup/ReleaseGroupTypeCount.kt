package ly.david.musicsearch.core.models.releasegroup

data class ReleaseGroupTypeCount(
    override val primaryType: String? = null,
    override val secondaryTypes: List<String>? = null,
    val count: Int,
) : ReleaseGroupTypes
