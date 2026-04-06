package ly.david.musicsearch.shared.domain.releasegroup

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class ReleaseGroupForRelease(
    override val id: String,
    override val name: String,
    override val disambiguation: String = "",
    override val firstReleaseDate: String,
    override val primaryType: ReleaseGroupPrimaryType? = null,
    override val secondaryTypes: ImmutableList<ReleaseGroupSecondaryType> = persistentListOf(),
) : ReleaseGroup
