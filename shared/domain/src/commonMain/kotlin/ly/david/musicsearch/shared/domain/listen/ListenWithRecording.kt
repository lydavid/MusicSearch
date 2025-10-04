package ly.david.musicsearch.shared.domain.listen

import ly.david.musicsearch.shared.domain.NameWithDisambiguation

data class ListenWithRecording(
    val id: String,
    override val name: String,
    override val disambiguation: String,
    val listenedMs: Long,
) : NameWithDisambiguation
