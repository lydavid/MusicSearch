package ly.david.musicsearch.data.musicbrainz.models.common

import kotlinx.serialization.Serializable
import ly.david.musicsearch.shared.domain.LifeSpan

@Serializable
data class LifeSpanMusicBrainzModel(
    override val begin: String? = null,
    override val end: String? = null,
    override val ended: Boolean? = null,
) : LifeSpan
