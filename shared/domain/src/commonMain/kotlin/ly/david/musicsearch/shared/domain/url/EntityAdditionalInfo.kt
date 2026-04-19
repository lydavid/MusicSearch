package ly.david.musicsearch.shared.domain.url

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import ly.david.musicsearch.shared.domain.alias.BasicAlias
import ly.david.musicsearch.shared.domain.image.ImageMetadata

data class EntityAdditionalInfo(
    val imageMetadata: ImageMetadata? = null,
    val visited: Boolean = false,
    val aliases: ImmutableList<BasicAlias> = persistentListOf(),
)
