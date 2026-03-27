package ly.david.musicsearch.shared.domain.listen

import kotlinx.collections.immutable.ImmutableList
import ly.david.musicsearch.shared.domain.artist.ArtistCreditUiModel
import ly.david.musicsearch.shared.domain.parcelize.CommonParcelable
import ly.david.musicsearch.shared.domain.parcelize.Parcelize

sealed interface SubmitListenType : CommonParcelable {
    @Parcelize
    data class Track(
        val info: TrackInfo,
        // optional because we can submit a listen from Recording screen, where the release would be ambiguous
        val releaseName: String?,
        val releaseId: String?,
    ) : SubmitListenType

    @Parcelize
    data class Album(
        val recordingIds: ImmutableList<String>,
        val releaseName: String,
        val releaseId: String,
        val releaseArtists: ImmutableList<ArtistCreditUiModel>,
    ) : SubmitListenType
}
