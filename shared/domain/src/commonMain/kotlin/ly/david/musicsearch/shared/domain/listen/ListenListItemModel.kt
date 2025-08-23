package ly.david.musicsearch.shared.domain.listen

import ly.david.musicsearch.shared.domain.Identifiable
import ly.david.musicsearch.shared.domain.image.ImageId
import ly.david.musicsearch.shared.domain.listitem.Visitable
import kotlin.time.Instant

data class ListenListItemModel(
    override val id: String,
    val name: String,
    val formattedArtistCredits: String,
    val listenedAt: Instant,
    val recordingId: String? = null,
    val imageUrl: String? = null,
    val imageId: ImageId? = null,
    override val visited: Boolean = false,
    val release: ListenRelease = ListenRelease(),
) : Identifiable, Visitable

data class ListenRelease(
    val name: String? = null,
    val id: String? = null,
    override val visited: Boolean = false,
) : Visitable
