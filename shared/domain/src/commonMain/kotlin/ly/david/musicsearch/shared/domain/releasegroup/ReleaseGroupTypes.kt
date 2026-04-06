package ly.david.musicsearch.shared.domain.releasegroup

import kotlinx.collections.immutable.ImmutableList

interface ReleaseGroupTypes {
    val primaryType: ReleaseGroupPrimaryType?
    val secondaryTypes: ImmutableList<ReleaseGroupSecondaryType>
}
