package ly.david.musicsearch.shared.domain.preferences

sealed interface WikidataInstance {
    data object Default : WikidataInstance
    data class Custom(
        val url: String,
    ) : WikidataInstance
}
