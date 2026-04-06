package ly.david.musicsearch.shared.domain.releasegroup

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class ReleaseGroupTypeCount(
    override val primaryType: ReleaseGroupPrimaryType? = null,
    override val secondaryTypes: ImmutableList<ReleaseGroupSecondaryType> = persistentListOf(),
    val count: Int,
) : ReleaseGroupTypes
