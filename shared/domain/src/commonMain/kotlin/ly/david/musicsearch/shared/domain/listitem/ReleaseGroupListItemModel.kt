package ly.david.musicsearch.shared.domain.listitem

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import ly.david.musicsearch.shared.domain.NameWithDisambiguationAndAliases
import ly.david.musicsearch.shared.domain.alias.BasicAlias
import ly.david.musicsearch.shared.domain.image.ImageId
import ly.david.musicsearch.shared.domain.releasegroup.ReleaseGroup

data class ReleaseGroupListItemModel(
    override val id: String,
    override val name: String,
    override val disambiguation: String = "",
    override val firstReleaseDate: String = "",
    override val primaryType: String = "",
    override val secondaryTypes: ImmutableList<String> = persistentListOf(),
    val formattedArtistCredits: String? = null,
    val imageUrl: String? = null,
    val imageId: ImageId? = null,
    override val visited: Boolean = false,
    override val collected: Boolean = false,
    override val aliases: ImmutableList<BasicAlias> = persistentListOf(),
) : EntityListItemModel, ReleaseGroup, NameWithDisambiguationAndAliases {
    override fun withAliases(aliases: ImmutableList<BasicAlias>): ReleaseGroupListItemModel {
        return copy(aliases = aliases)
    }
}
