package ly.david.musicsearch.shared.domain.recording

import ly.david.musicsearch.shared.domain.NameWithDisambiguation
import ly.david.musicsearch.shared.domain.parcelize.CommonParcelable
import ly.david.musicsearch.shared.domain.parcelize.Parcelize

@Parcelize
data class RecordingFacet(
    val id: String = "",
    override val name: String = "",
    override val disambiguation: String = "",
    val formattedArtistCredits: String = "",
    val count: Int = 0,
) : CommonParcelable, NameWithDisambiguation
