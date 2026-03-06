package ly.david.musicsearch.shared.domain.listen

import kotlinx.collections.immutable.ImmutableList
import ly.david.musicsearch.shared.domain.NameWithDisambiguationAndAliases
import ly.david.musicsearch.shared.domain.alias.BasicAlias
import ly.david.musicsearch.shared.domain.artist.ArtistCreditUiModel
import ly.david.musicsearch.shared.domain.parcelize.CommonParcelable
import ly.david.musicsearch.shared.domain.parcelize.Parcelize

sealed interface SubmitListenType : CommonParcelable {
    @Parcelize
    data class Track(
        override val name: String,
        override val disambiguation: String?,
        override val aliases: ImmutableList<BasicAlias>,
        val recordingId: String,
        val lengthMilliseconds: Int?,
        val artists: ImmutableList<ArtistCreditUiModel>,
        // optional because we can submit a listen from Recording screen, where the release would be ambiguous
        val releaseName: String?,
    ) : SubmitListenType, NameWithDisambiguationAndAliases {
        override fun withAliases(aliases: ImmutableList<BasicAlias>): NameWithDisambiguationAndAliases {
            return copy(aliases = aliases)
        }
    }

//    data class Album(
//
//    ): SubmitType
}
