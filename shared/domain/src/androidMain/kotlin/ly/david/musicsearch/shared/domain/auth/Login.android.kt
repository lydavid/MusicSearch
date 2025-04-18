package ly.david.musicsearch.shared.domain.auth

interface Login {
    suspend operator fun invoke(tokenRequestJsonString: String)
}
