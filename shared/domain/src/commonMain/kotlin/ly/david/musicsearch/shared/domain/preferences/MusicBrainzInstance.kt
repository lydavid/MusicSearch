package ly.david.musicsearch.shared.domain.preferences

sealed interface MusicBrainzInstance {
    data object Default : MusicBrainzInstance
    data class Custom(
        val url: String,
    ) : MusicBrainzInstance
}

sealed interface ListenBrainzInstance {
    data object Default : ListenBrainzInstance
    data class Custom(
        val url: String,
    ) : ListenBrainzInstance
}

sealed interface WikimediaInstance {
    data object Default : WikimediaInstance
    data class Custom(
        val url: String,
    ) : WikimediaInstance
}
