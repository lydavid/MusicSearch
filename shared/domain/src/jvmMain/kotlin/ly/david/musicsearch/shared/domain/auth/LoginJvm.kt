package ly.david.musicsearch.shared.domain.auth

interface LoginJvm {
    operator fun invoke(authCode: String)
}
