package ly.david.musicsearch.shared.domain.image

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class RawImageMetadata(
    val thumbnailUrl: String,
    val largeUrl: String,
    val source: ImageSource,
    val types: ImmutableList<String> = persistentListOf(),
    val comment: String = "",
)
