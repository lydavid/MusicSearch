package ly.david.musicsearch.data.database.dao

import ly.david.musicsearch.data.database.Database
import ly.david.musicsearch.data.database.mapper.combineToAliases
import ly.david.musicsearch.data.database.mapper.mapToImageMetadata
import ly.david.musicsearch.shared.domain.url.EntityAdditionalInfo

class UrlDao(
    database: Database,
) : EntityDao {
    override val transacter = database.urlQueries

    fun getAdditionalInfoForEntity(entityId: String): EntityAdditionalInfo? {
        return transacter.getAdditionalInfoForEntity(
            entityId = entityId,
            mapper = { aliasNames, aliasLocales, thumbnailUrl, imageId, source, visited ->
                EntityAdditionalInfo(
                    imageMetadata = mapToImageMetadata(
                        id = imageId,
                        thumbnailUrl = thumbnailUrl,
                        source = source,
                    ),
                    visited = visited,
                    aliases = combineToAliases(aliasNames, aliasLocales),
                )
            },
        ).executeAsOneOrNull()
    }
}
