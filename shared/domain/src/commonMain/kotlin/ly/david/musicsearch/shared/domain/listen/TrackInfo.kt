package ly.david.musicsearch.shared.domain.listen

import kotlinx.collections.immutable.ImmutableList
import ly.david.musicsearch.shared.domain.MS_IN_SECOND
import ly.david.musicsearch.shared.domain.NameWithDisambiguationAndAliases
import ly.david.musicsearch.shared.domain.alias.BasicAlias
import ly.david.musicsearch.shared.domain.artist.ArtistCreditUiModel
import ly.david.musicsearch.shared.domain.parcelize.CommonParcelable
import ly.david.musicsearch.shared.domain.parcelize.Parcelize

private const val MINIMUM_TRACK_LENGTH_SECONDS = 60L

@Parcelize
data class TrackInfo(
    override val name: String,
    override val disambiguation: String?,
    override val aliases: ImmutableList<BasicAlias>,
    val recordingId: String,
    val lengthMilliseconds: Long?,
    val artists: ImmutableList<ArtistCreditUiModel>,
) : CommonParcelable, NameWithDisambiguationAndAliases {
    override fun withAliases(aliases: ImmutableList<BasicAlias>): NameWithDisambiguationAndAliases =
        copy(aliases = aliases)

    val nonZeroLengthMilliseconds: Long
        get() = lengthMilliseconds ?: (MINIMUM_TRACK_LENGTH_SECONDS * MS_IN_SECOND)
}
