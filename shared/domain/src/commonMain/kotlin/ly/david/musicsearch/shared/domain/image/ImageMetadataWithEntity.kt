package ly.david.musicsearch.shared.domain.image

import ly.david.musicsearch.shared.domain.NameWithDisambiguation
import ly.david.musicsearch.shared.domain.musicbrainz.MusicBrainzEntity

data class ImageMetadataWithEntity(
    val imageMetadata: ImageMetadata,
    val musicBrainzEntity: MusicBrainzEntity? = null,
    override val name: String? = null,
    override val disambiguation: String? = null,
) : NameWithDisambiguation
