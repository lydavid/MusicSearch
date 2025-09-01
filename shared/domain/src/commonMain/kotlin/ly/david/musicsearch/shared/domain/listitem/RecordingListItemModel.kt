package ly.david.musicsearch.shared.domain.listitem

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import ly.david.musicsearch.shared.domain.NameWithDisambiguationAndAliases
import ly.david.musicsearch.shared.domain.alias.BasicAlias
import ly.david.musicsearch.shared.domain.recording.Recording

data class RecordingListItemModel(
    override val id: String,
    override val name: String,
    override val firstReleaseDate: String = "",
    override val disambiguation: String = "",
    override val length: Int? = null,
    override val video: Boolean = false,
    val formattedArtistCredits: String? = null,
    override val visited: Boolean = false,
    override val collected: Boolean = false,
    override val aliases: ImmutableList<BasicAlias> = persistentListOf(),
) : EntityListItemModel, Recording, NameWithDisambiguationAndAliases {
    override fun withAliases(aliases: ImmutableList<BasicAlias>): NameWithDisambiguationAndAliases {
        return copy(aliases = aliases)
    }
}
