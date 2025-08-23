package ly.david.musicsearch.shared.domain.listen

import ly.david.musicsearch.shared.domain.Identifiable
import ly.david.musicsearch.shared.domain.image.ImageId
import kotlin.time.Instant

data class ListenListItemModel(
    override val id: String,
    val name: String,
    val formattedArtistCredits: String,
    val listenedAt: Instant,
    val recordingId: String? = null,
    val releaseName: String? = null,
    val releaseId: String? = null,
    val imageUrl: String? = null,
    val imageId: ImageId? = null,
) : Identifiable
