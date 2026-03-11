package ly.david.musicsearch.shared.domain.listen

data class ListenSubmission(
    val listenedAtS: Long,
    val artistName: String,
    val trackName: String,
    val releaseName: String? = null,
    val releaseMbid: String? = null,
    val artistMbids: List<String>? = null,
    val recordingMbid: String? = null,
    val durationMs: Long? = null,
)
