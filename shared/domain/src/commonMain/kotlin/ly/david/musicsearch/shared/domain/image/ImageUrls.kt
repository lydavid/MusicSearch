package ly.david.musicsearch.shared.domain.image

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class ImageUrls(
    val databaseId: Long = 0L,
    val thumbnailUrl: String = "",
    val largeUrl: String = "",
    val types: ImmutableList<String> = persistentListOf(),
    val comment: String = "",
)
