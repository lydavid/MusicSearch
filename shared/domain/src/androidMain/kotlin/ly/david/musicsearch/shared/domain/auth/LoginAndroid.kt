package ly.david.musicsearch.shared.domain.auth

interface LoginAndroid {
    suspend operator fun invoke(tokenRequestJsonString: String)
}
