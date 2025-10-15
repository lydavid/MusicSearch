package ly.david.musicsearch.shared.domain.listitem

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import ly.david.musicsearch.shared.domain.NameWithDisambiguationAndAliases
import ly.david.musicsearch.shared.domain.alias.BasicAlias
import ly.david.musicsearch.shared.domain.image.ImageId
import ly.david.musicsearch.shared.domain.release.Release
import ly.david.musicsearch.shared.domain.release.ReleaseStatus
import ly.david.musicsearch.shared.domain.release.TextRepresentationUiModel

data class ReleaseListItemModel(
    override val id: String,
    override val name: String,
    override val disambiguation: String = "",
    override val date: String = "",
    override val barcode: String = "",
    val status: ReleaseStatus? = null,
    override val countryCode: String = "",
    override val packaging: String = "",
    override val packagingId: String = "",
    override val asin: String = "",
    override val quality: String = "",

    val catalogNumbers: String? = null,

    val textRepresentation: TextRepresentationUiModel? = TextRepresentationUiModel(),
    val imageUrl: String? = null,
    val imageId: ImageId? = null,

    val formattedFormats: String? = null,
    val formattedTracks: String? = null,
    val formattedArtistCredits: String? = null,

    val releaseCountryCount: Int = 0,
    override val visited: Boolean = false,
    override val collected: Boolean = false,
    override val aliases: ImmutableList<BasicAlias> = persistentListOf(),
    val listenState: ListenState = ListenState.Hide,
) : EntityListItemModel, Release, NameWithDisambiguationAndAliases {
    override fun withAliases(aliases: ImmutableList<BasicAlias>): NameWithDisambiguationAndAliases {
        return copy(aliases = aliases)
    }

    sealed interface ListenState {
        data object Hide : ListenState
        data object Unknown : ListenState
        data class Known(
            val listenCount: Long = 0,
            val completeListenCount: Long = 0,
        ) : ListenState
    }
}
