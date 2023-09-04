package ly.david.data.core

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
