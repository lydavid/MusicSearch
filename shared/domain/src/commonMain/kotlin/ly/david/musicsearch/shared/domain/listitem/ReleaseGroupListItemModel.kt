package ly.david.musicsearch.shared.domain.listitem

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
    override val secondaryTypes: List<String> = listOf(),
    val formattedArtistCredits: String? = null,
    val imageUrl: String? = null,
    val imageId: ImageId? = null,
    override val visited: Boolean = false,
    override val collected: Boolean = false,
    override val aliases: List<BasicAlias> = listOf(),
) : EntityListItemModel, ReleaseGroup, NameWithDisambiguationAndAliases {
    override fun withAliases(aliases: List<BasicAlias>): ReleaseGroupListItemModel {
        return copy(aliases = aliases)
    }
}
