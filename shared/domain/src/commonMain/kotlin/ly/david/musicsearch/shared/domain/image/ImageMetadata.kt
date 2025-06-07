package ly.david.musicsearch.shared.domain.image

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import ly.david.musicsearch.shared.domain.NameWithDisambiguation
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity

data class ImageMetadata(
    val imageId: ImageId = ImageId(0L),
    val thumbnailUrl: String = "",
    val largeUrl: String = "",
    val types: ImmutableList<String> = persistentListOf(),
    val comment: String = "",
    val mbid: String? = null,
    override val name: String? = null,
    override val disambiguation: String? = null,
    val entity: MusicBrainzEntity? = null,
) : NameWithDisambiguation
