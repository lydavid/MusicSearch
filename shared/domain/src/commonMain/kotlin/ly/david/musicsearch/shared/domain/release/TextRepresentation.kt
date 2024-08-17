package ly.david.musicsearch.shared.domain.release

interface TextRepresentation {
    /**
     * See: https://en.wikipedia.org/wiki/ISO_15924
     */
    val script: String?

    /**
     * See: https://en.wikipedia.org/wiki/ISO_639-3
     */
    val language: String?
}
