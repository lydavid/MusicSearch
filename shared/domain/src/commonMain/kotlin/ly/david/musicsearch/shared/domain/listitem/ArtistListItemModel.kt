package ly.david.musicsearch.shared.domain.listitem

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import ly.david.musicsearch.shared.domain.LifeSpanUiModel
import ly.david.musicsearch.shared.domain.NameWithDisambiguationAndAliases
import ly.david.musicsearch.shared.domain.alias.BasicAlias
import ly.david.musicsearch.shared.domain.artist.Artist
import ly.david.musicsearch.shared.domain.artist.ArtistGender
import ly.david.musicsearch.shared.domain.artist.ArtistType
import ly.david.musicsearch.shared.domain.image.ImageMetadata
import ly.david.musicsearch.shared.domain.listen.ListenInfo

data class ArtistListItemModel(
    override val id: String,
    override val name: String,
    override val sortName: String = "",
    override val disambiguation: String = "",
    val type: ArtistType? = null,
    val gender: ArtistGender? = null,
    val countryCode: String = "",
    val lifeSpan: LifeSpanUiModel = LifeSpanUiModel(),
    val imageMetadata: ImageMetadata? = null,
    override val visited: Boolean = false,
    override val collected: Boolean = false,
    override val aliases: ImmutableList<BasicAlias> = persistentListOf(),
    override val listenCount: Long? = null,
    override val lastListenedAtMs: Long? = null,
) : EntityListItemModel, Artist, NameWithDisambiguationAndAliases, ListenInfo {
    override fun withAliases(aliases: ImmutableList<BasicAlias>): ArtistListItemModel {
        return copy(aliases = aliases)
    }
}
