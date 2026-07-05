package ly.david.musicsearch.shared.domain.preferences

sealed interface MusicBrainzInstance {
    data object Default : MusicBrainzInstance
    data class Custom(
        val url: String,
    ) : MusicBrainzInstance
}
