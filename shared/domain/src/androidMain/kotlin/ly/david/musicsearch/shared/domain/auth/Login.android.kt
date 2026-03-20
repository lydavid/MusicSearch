package ly.david.musicsearch.shared.domain.auth

interface Login {
    /**
     * Returns whether login with [tokenRequestJsonString] was successful.
     */
    suspend operator fun invoke(tokenRequestJsonString: String): Boolean
}
