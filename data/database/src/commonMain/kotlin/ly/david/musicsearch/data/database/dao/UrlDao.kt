package ly.david.musicsearch.data.database.dao

import ly.david.musicsearch.data.database.Database
import ly.david.musicsearch.data.database.mapper.combineToAliases
import ly.david.musicsearch.data.database.mapper.mapToImageMetadata
import ly.david.musicsearch.shared.domain.image.ImageSource
import ly.david.musicsearch.shared.domain.listitem.RelationListItemModel
import ly.david.musicsearch.shared.domain.network.toMusicBrainzEntityType
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

    fun getEntityInfoForUrl(url: String): List<RelationListItemModel> {
        return transacter.getEntityInfoForUrl(
            url = url,
            mapper = ::mapToRelationListItemModel,
        )
            .executeAsList()
    }

    private fun mapToRelationListItemModel(
        entityId: String,
        name: String?,
        disambiguation: String?,
        entityType: String,
        aliasNames: String?,
        aliasLocales: String?,
        thumbnailUrl: String?,
        imageId: Long?,
        source: ImageSource?,
        visited: Boolean,
    ): RelationListItemModel {
        return RelationListItemModel(
            id = entityId,
            linkedEntityId = entityId,
            linkedEntity = entityType.toMusicBrainzEntityType() ?: error("Invalid entity type: $entityType"),
            type = "",
            name = name.orEmpty(),
            disambiguation = disambiguation.orEmpty(),
            imageMetadata = mapToImageMetadata(
                id = imageId,
                thumbnailUrl = thumbnailUrl,
                source = source,
            ),
            visited = visited,
            aliases = combineToAliases(aliasNames, aliasLocales),
        )
    }
}
