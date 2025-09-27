package ly.david.musicsearch.shared.domain.musicbrainz

import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.domain.parcelize.CommonParcelable
import ly.david.musicsearch.shared.domain.parcelize.Parcelize

@Parcelize
data class MusicBrainzEntity(
    val id: String,
    val type: MusicBrainzEntityType,
) : CommonParcelable
