package ly.david.musicsearch.shared.domain.preferences

sealed interface ListenBrainzInstance {
    data object Default : ListenBrainzInstance
    data class Custom(
        val url: String,
    ) : ListenBrainzInstance
}
