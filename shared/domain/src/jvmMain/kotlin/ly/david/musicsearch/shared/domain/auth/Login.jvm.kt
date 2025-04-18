package ly.david.musicsearch.shared.domain.auth

interface Login {
    operator fun invoke(authCode: String)
}
