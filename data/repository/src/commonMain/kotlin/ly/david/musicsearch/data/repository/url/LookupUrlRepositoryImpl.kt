package ly.david.musicsearch.data.repository.url

import kotlinx.collections.immutable.persistentListOf
import ly.david.musicsearch.data.database.dao.UrlDao
import ly.david.musicsearch.data.musicbrainz.api.LookupApi
import ly.david.musicsearch.data.musicbrainz.models.relation.RelationMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.relation.SerializableMusicBrainzEntity
import ly.david.musicsearch.shared.domain.listitem.RelationListItemModel
import ly.david.musicsearch.shared.domain.url.EntityAdditionalInfo
import ly.david.musicsearch.shared.domain.url.LookupUrlRepository

internal class LookupUrlRepositoryImpl(
    private val lookupApi: LookupApi,
    private val urlDao: UrlDao,
) : LookupUrlRepository {
    override suspend fun getEntitiesLinkedToUrl(
        url: String,
        searchLocalOnly: Boolean,
    ): List<RelationListItemModel> {
        return lookupApi
            .lookupUrl(url = url)
            .relations?.mapNotNull { relation ->
                relation.toListItemModel(
                    getAdditionalInfo = urlDao::getAdditionalInfoForEntity,
                )
            }.orEmpty()
    }
}

@Suppress(
    "CyclomaticComplexMethod",
    "LongMethod",
    "ReturnCount",
)
private fun RelationMusicBrainzModel.toListItemModel(
    getAdditionalInfo: (entityId: String) -> EntityAdditionalInfo?,
): RelationListItemModel? {
    val targetType = targetType
    requireNotNull(targetType)

    val linkedEntityId: String
    val linkedEntityName: String
    val linkedEntityDisambiguation: String?

    when (targetType) {
        SerializableMusicBrainzEntity.AREA -> {
            area?.apply {
                linkedEntityId = id
                linkedEntityName = name
                linkedEntityDisambiguation = disambiguation
            } ?: return null
        }

        SerializableMusicBrainzEntity.ARTIST -> {
            artist?.apply {
                linkedEntityId = id
                linkedEntityName = name
                linkedEntityDisambiguation = disambiguation
            } ?: return null
        }

        SerializableMusicBrainzEntity.EVENT -> {
            event?.apply {
                linkedEntityId = id
                linkedEntityName = name
                linkedEntityDisambiguation = disambiguation
            } ?: return null
        }

        SerializableMusicBrainzEntity.GENRE -> {
            genre?.apply {
                linkedEntityId = id
                linkedEntityName = name
                linkedEntityDisambiguation = disambiguation
            } ?: return null
        }

        SerializableMusicBrainzEntity.INSTRUMENT -> {
            instrument?.apply {
                linkedEntityId = id
                linkedEntityName = name
                linkedEntityDisambiguation = disambiguation
            } ?: return null
        }

        SerializableMusicBrainzEntity.LABEL -> {
            label?.apply {
                linkedEntityId = id
                linkedEntityName = name
                linkedEntityDisambiguation = disambiguation
            } ?: return null
        }

        SerializableMusicBrainzEntity.PLACE -> {
            place?.apply {
                linkedEntityId = id
                linkedEntityName = name
                linkedEntityDisambiguation = disambiguation
            } ?: return null
        }

        SerializableMusicBrainzEntity.RECORDING -> {
            recording?.apply {
                linkedEntityId = id
                linkedEntityName = name
                linkedEntityDisambiguation = disambiguation
            } ?: return null
        }

        SerializableMusicBrainzEntity.RELEASE -> {
            release?.apply {
                linkedEntityId = id
                linkedEntityName = name
                linkedEntityDisambiguation = disambiguation
            } ?: return null
        }

        SerializableMusicBrainzEntity.RELEASE_GROUP -> {
            releaseGroup?.apply {
                linkedEntityId = id
                linkedEntityName = name
                linkedEntityDisambiguation = disambiguation
            } ?: return null
        }

        SerializableMusicBrainzEntity.SERIES -> {
            series?.apply {
                linkedEntityId = id
                linkedEntityName = name
                linkedEntityDisambiguation = disambiguation
            } ?: return null
        }

        SerializableMusicBrainzEntity.WORK -> {
            work?.apply {
                linkedEntityId = id
                linkedEntityName = name
                linkedEntityDisambiguation = disambiguation
            } ?: return null
        }

        SerializableMusicBrainzEntity.URL -> {
            return null
        }
    }

    val additionalInfo = getAdditionalInfo(linkedEntityId)

    return RelationListItemModel(
        id = linkedEntityId, // only for lazy paging, so just make it unique
        linkedEntityId = linkedEntityId,
        linkedEntity = targetType.entity,
        type = "", // not useful to display
        name = linkedEntityName,
        disambiguation = linkedEntityDisambiguation,
        visited = additionalInfo?.visited == true,
        imageMetadata = additionalInfo?.imageMetadata,
        aliases = additionalInfo?.aliases ?: persistentListOf(),
    )
}
