package ly.david.musicsearch.shared.domain.listen

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import ly.david.musicsearch.shared.domain.Identifiable
import ly.david.musicsearch.shared.domain.NameWithDisambiguationAndAliases
import ly.david.musicsearch.shared.domain.alias.BasicAlias
import ly.david.musicsearch.shared.domain.artist.ArtistCreditUiModel
import ly.david.musicsearch.shared.domain.artist.getDisplayNames
import ly.david.musicsearch.shared.domain.common.emptyToNull
import ly.david.musicsearch.shared.domain.image.ImageMetadata
import ly.david.musicsearch.shared.domain.listitem.Visitable
import kotlin.time.Instant

data class ListenListItemModel(
    val listenedAtMs: Long,
    val username: String,
    val recordingMessybrainzId: String,
    val recordingName: String? = null,
    val unmappedTrackName: String,
    override val disambiguation: String = "",
    val separateArtistCredits: ImmutableList<ArtistCreditUiModel> = persistentListOf(),
    val unmappedFormattedArtistCredits: String,
    val recordingId: String = "",
    val recordingDurationMs: Int? = null,
    val unmappedDurationMs: Int? = null,
    val unmappedDiscNumber: Int? = null,
    val unmappedTrackNumber: String? = null,
    val isrcs: ImmutableList<String> = persistentListOf(),
    val unmappedIsrc: String? = null,
    val imageMetadata: ImageMetadata? = null,
    override val visited: Boolean = false,
    val release: ListenRelease = ListenRelease(),
    override val aliases: ImmutableList<BasicAlias> = persistentListOf(),
) : Identifiable, Visitable, NameWithDisambiguationAndAliases {
    override val id: String = "${listenedAtMs}_${username}_$recordingMessybrainzId"
    override val name: String = recordingName ?: unmappedTrackName

    /**
     * Prefer to show [unmappedDurationMs] over [recordingDurationMs] because track length can differ from MB's recording length.
     * This is similar to preferring track length on a release over the underlying recording length.
     */
    val durationMs: Int? = unmappedDurationMs ?: recordingDurationMs

    val artistCredits: String = separateArtistCredits.getDisplayNames().emptyToNull() ?: unmappedFormattedArtistCredits

    val listenedAt: Instant = Instant.fromEpochMilliseconds(listenedAtMs)

    override fun withAliases(aliases: ImmutableList<BasicAlias>): NameWithDisambiguationAndAliases {
        return copy(aliases = aliases)
    }
}

data class ListenRelease(
    val mappedName: String? = null,
    val unmappedName: String? = null,
    val id: String? = null,
    override val visited: Boolean = false,
) : Visitable {
    val name: String? = mappedName ?: unmappedName
}
