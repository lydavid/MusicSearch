package ly.david.musicsearch.shared.domain.listen

data class ListenWithTrack(
    val mediumPosition: Int,
    val trackNumber: String,
    val trackName: String,
    val listenedMs: Long,
)
