package ly.david.data.network

import kotlinx.serialization.Serializable

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

@Serializable
data class TextRepresentationMusicBrainzModel(
    override val script: String?,
    override val language: String?
): TextRepresentation

data class TextRepresentationRoomModel(
    override val script: String?,
    override val language: String?
): TextRepresentation

data class TextRepresentationUiModel(
    override val script: String?,
    override val language: String?
): TextRepresentation

fun TextRepresentationMusicBrainzModel.toTextRepresentationRoomModel() = TextRepresentationRoomModel(
    script = script,
    language = language,
)

fun TextRepresentationRoomModel.toTextRepresentationUiModel() = TextRepresentationUiModel(
    script = script,
    language = language,
)

fun TextRepresentationMusicBrainzModel.toTextRepresentationUiModel() = TextRepresentationUiModel(
    script = script,
    language = language,
)
