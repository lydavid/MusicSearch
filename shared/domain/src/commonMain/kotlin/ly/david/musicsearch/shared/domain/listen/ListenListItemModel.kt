package ly.david.musicsearch.shared.domain.listen

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import ly.david.musicsearch.shared.domain.Identifiable
import ly.david.musicsearch.shared.domain.NameWithDisambiguationAndAliases
import ly.david.musicsearch.shared.domain.alias.BasicAlias
import ly.david.musicsearch.shared.domain.image.ImageId
import ly.david.musicsearch.shared.domain.listitem.Visitable
import kotlin.time.Instant

data class ListenListItemModel(
    override val id: String,
    override val name: String,
    override val disambiguation: String = "",
    val formattedArtistCredits: String,
    val listenedAt: Instant,
    val recordingId: String? = null,
    val durationMs: Int? = null,
    val imageUrl: String? = null,
    val imageId: ImageId? = null,
    override val visited: Boolean = false,
    val release: ListenRelease = ListenRelease(),
    override val aliases: ImmutableList<BasicAlias> = persistentListOf(),
) : Identifiable, Visitable, NameWithDisambiguationAndAliases {
    override fun withAliases(aliases: ImmutableList<BasicAlias>): NameWithDisambiguationAndAliases {
        return copy(aliases = aliases)
    }
}

data class ListenRelease(
    val name: String? = null,
    val id: String? = null,
    override val visited: Boolean = false,
) : Visitable
