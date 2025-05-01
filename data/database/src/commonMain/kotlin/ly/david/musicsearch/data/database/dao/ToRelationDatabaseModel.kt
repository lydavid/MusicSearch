package ly.david.musicsearch.data.database.dao

import ly.david.musicsearch.data.musicbrainz.models.relation.Direction
import ly.david.musicsearch.data.musicbrainz.models.relation.RelationMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.relation.SerializableMusicBrainzEntity
import ly.david.musicsearch.data.musicbrainz.models.relation.getFormattedAttributesForDisplay
import ly.david.musicsearch.data.musicbrainz.models.relation.getHeader
import ly.david.musicsearch.shared.domain.LifeSpanUiModel
import ly.david.musicsearch.shared.domain.artist.getDisplayNames
import ly.david.musicsearch.shared.domain.common.emptyToNull
import ly.david.musicsearch.shared.domain.common.transformThisIfNotNullOrEmpty
import ly.david.musicsearch.shared.domain.relation.RelationWithOrder

fun RelationMusicBrainzModel.toRelationDatabaseModel(
    entityId: String,
    index: Int,
    numberOfRelationships: Int,
): RelationWithOrder? {
    val targetType = targetType
    requireNotNull(targetType)

    var linkedEntityId = ""
    var linkedEntityName = ""
    var linkedEntityDisambiguation: String? = null
    var additionalInfo: String? = null

    val entity = targetType.entity
    when (targetType) {
        SerializableMusicBrainzEntity.AREA -> {
            area?.apply {
                linkedEntityId = id
                linkedEntityName = targetCredit.emptyToNull() ?: name
                linkedEntityDisambiguation = disambiguation
            } ?: return null
        }

        SerializableMusicBrainzEntity.ARTIST -> {
            artist?.apply {
                linkedEntityId = id
                linkedEntityName = targetCredit.emptyToNull() ?: name
                linkedEntityDisambiguation = disambiguation
            } ?: return null
        }

        SerializableMusicBrainzEntity.EVENT -> {
            event?.apply {
                linkedEntityId = id
                linkedEntityName = targetCredit.emptyToNull() ?: name
                linkedEntityDisambiguation = disambiguation
            } ?: return null
        }

        SerializableMusicBrainzEntity.GENRE -> {
            genre?.apply {
                linkedEntityId = id
                linkedEntityName = targetCredit.emptyToNull() ?: name
                linkedEntityDisambiguation = disambiguation
            } ?: return null
        }

        SerializableMusicBrainzEntity.INSTRUMENT -> {
            instrument?.apply {
                linkedEntityId = id
                linkedEntityName = targetCredit.emptyToNull() ?: name
                linkedEntityDisambiguation = disambiguation
            } ?: return null
        }

        SerializableMusicBrainzEntity.LABEL -> {
            label?.apply {
                linkedEntityId = id
                linkedEntityName = targetCredit.emptyToNull() ?: name
                linkedEntityDisambiguation = disambiguation
            } ?: return null
        }

        SerializableMusicBrainzEntity.PLACE -> {
            place?.apply {
                linkedEntityId = id
                linkedEntityName = targetCredit.emptyToNull() ?: name
                linkedEntityDisambiguation = disambiguation
            } ?: return null
        }

        SerializableMusicBrainzEntity.RECORDING -> {
            recording?.apply {
                linkedEntityId = id
                linkedEntityName = targetCredit.emptyToNull() ?: name
                linkedEntityDisambiguation = disambiguation
                additionalInfo = artistCredits.getDisplayNames().transformThisIfNotNullOrEmpty { "by $it" }
            } ?: return null
        }

        SerializableMusicBrainzEntity.RELEASE -> {
            release?.apply {
                linkedEntityId = id
                linkedEntityName = targetCredit.emptyToNull() ?: name
                linkedEntityDisambiguation = disambiguation
            } ?: return null
        }

        SerializableMusicBrainzEntity.RELEASE_GROUP -> {
            releaseGroup?.apply {
                linkedEntityId = id
                linkedEntityName = targetCredit.emptyToNull() ?: name
                linkedEntityDisambiguation = disambiguation
            } ?: return null
        }

        SerializableMusicBrainzEntity.SERIES -> {
            series?.apply {
                linkedEntityId = id
                linkedEntityName = targetCredit.emptyToNull() ?: name
                linkedEntityDisambiguation = disambiguation
            } ?: return null
        }

        SerializableMusicBrainzEntity.WORK -> {
            work?.apply {
                linkedEntityId = id
                linkedEntityName = targetCredit.emptyToNull() ?: name
                linkedEntityDisambiguation = disambiguation
            } ?: return null
        }

        SerializableMusicBrainzEntity.URL -> {
            url?.apply {
                linkedEntityId = id
                linkedEntityName = resource.decodeUrl()
                linkedEntityDisambiguation = null
            } ?: return null
        }
    }

    val order: Int = orderingKey.takeIf { it != null } ?: (numberOfRelationships + index)

    return RelationWithOrder(
        id = entityId,
        linkedEntityId = linkedEntityId,
        linkedEntity = entity,
        order = order,
        label = getHeader(),
        name = linkedEntityName,
        disambiguation = linkedEntityDisambiguation,
        attributes = getFormattedAttributesForDisplay(),
        additionalInfo = additionalInfo,
        isForwardDirection = direction == Direction.FORWARD,
        lifeSpan = LifeSpanUiModel(
            begin = begin,
            end = end,
            ended = ended,
        ),
    )
}

internal expect fun String.decodeUrl(): String
