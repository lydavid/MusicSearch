package ly.david.musicsearch.shared.domain.image

sealed interface ArtistImageSource {
    data object Wikimedia : ArtistImageSource

    sealed interface Spotify : ArtistImageSource {
        data object Default : Spotify
        data class Custom(
            val clientId: String = "",
            val clientSecret: String = "",
        ) : Spotify
    }
}
