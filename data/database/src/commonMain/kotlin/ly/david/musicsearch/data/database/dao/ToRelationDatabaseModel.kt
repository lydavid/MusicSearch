package ly.david.musicsearch.data.database.dao

import ly.david.musicsearch.core.models.artist.getDisplayNames
import ly.david.musicsearch.core.models.common.emptyToNull
import ly.david.musicsearch.core.models.common.transformThisIfNotNullOrEmpty
import ly.david.musicsearch.core.models.getLifeSpanForDisplay
import ly.david.musicsearch.core.models.relation.RelationWithOrder
import ly.david.musicsearch.data.musicbrainz.models.relation.RelatableMusicBrainzEntity
import ly.david.musicsearch.data.musicbrainz.models.relation.RelationMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.relation.getFormattedAttributesForDisplay
import ly.david.musicsearch.data.musicbrainz.models.relation.getHeader
import lydavidmusicsearchdatadatabase.Relation

/**
 * We cannot guarantee that a [Relation] will be created in the scenario that target-type points to a resource
 * but that object is null. It's possible that this is never the case, but our models are currently structured such
 * that any of them are nullable.
 */
fun RelationMusicBrainzModel.toRelationDatabaseModel(
    entityId: String,
    order: Int,
): RelationWithOrder? {
    val targetType = targetType
    requireNotNull(targetType)

    var linkedEntityId = ""
    var linkedEntityName = ""
    var linkedEntityDisambiguation: String? = null
    var additionalInfo: String? = null

    val entity = targetType.entity
    when (targetType) {
        RelatableMusicBrainzEntity.AREA -> {
            if (area == null) return null
            area?.apply {
                linkedEntityId = id
                linkedEntityName = targetCredit.emptyToNull() ?: name
                linkedEntityDisambiguation = disambiguation
            } ?: return null
        }

        RelatableMusicBrainzEntity.ARTIST -> {
            if (artist == null) return null
            artist?.apply {
                linkedEntityId = id
                linkedEntityName = targetCredit.emptyToNull() ?: name
                linkedEntityDisambiguation = disambiguation
                additionalInfo = getLifeSpanForDisplay().transformThisIfNotNullOrEmpty { "($it)" }
            }
        }

        RelatableMusicBrainzEntity.EVENT -> {
            if (event == null) return null
            event?.apply {
                linkedEntityId = id
                linkedEntityName = targetCredit.emptyToNull() ?: name
                linkedEntityDisambiguation = disambiguation
            } ?: return null
        }

        RelatableMusicBrainzEntity.GENRE -> {
            if (genre == null) return null
            genre?.apply {
                linkedEntityId = id
                linkedEntityName = targetCredit.emptyToNull() ?: name
                linkedEntityDisambiguation = disambiguation
            } ?: return null
        }

        RelatableMusicBrainzEntity.INSTRUMENT -> {
            if (instrument == null) return null
            instrument?.apply {
                linkedEntityId = id
                linkedEntityName = targetCredit.emptyToNull() ?: name
                linkedEntityDisambiguation = disambiguation
            } ?: return null
        }

        RelatableMusicBrainzEntity.LABEL -> {
            if (label == null) return null
            label?.apply {
                linkedEntityId = id
                linkedEntityName = targetCredit.emptyToNull() ?: name
                linkedEntityDisambiguation = disambiguation
            } ?: return null
        }

        RelatableMusicBrainzEntity.PLACE -> {
            if (place == null) return null
            place?.apply {
                linkedEntityId = id
                linkedEntityName = targetCredit.emptyToNull() ?: name
                linkedEntityDisambiguation = disambiguation
            } ?: return null
        }

        RelatableMusicBrainzEntity.RECORDING -> {
            if (recording == null) return null
            recording?.apply {
                linkedEntityId = id
                linkedEntityName = targetCredit.emptyToNull() ?: name
                linkedEntityDisambiguation = disambiguation
                additionalInfo = artistCredits.getDisplayNames().transformThisIfNotNullOrEmpty { "by $it" } +
                    getLifeSpanForDisplay().transformThisIfNotNullOrEmpty { "($it)" }
            } ?: return null
        }

        RelatableMusicBrainzEntity.RELEASE -> {
            if (release == null) return null
            release?.apply {
                linkedEntityId = id
                linkedEntityName = targetCredit.emptyToNull() ?: name
                linkedEntityDisambiguation = disambiguation
                additionalInfo = getLifeSpanForDisplay().transformThisIfNotNullOrEmpty { "($it)" }
            } ?: return null
        }

        RelatableMusicBrainzEntity.RELEASE_GROUP -> {
            if (releaseGroup == null) return null
            releaseGroup?.apply {
                linkedEntityId = id
                linkedEntityName = targetCredit.emptyToNull() ?: name
                linkedEntityDisambiguation = disambiguation
                additionalInfo = getLifeSpanForDisplay().transformThisIfNotNullOrEmpty { "($it)" }
            } ?: return null
        }

        RelatableMusicBrainzEntity.SERIES -> {
            if (series == null) return null
            series?.apply {
                linkedEntityId = id
                linkedEntityName = targetCredit.emptyToNull() ?: name
                linkedEntityDisambiguation = disambiguation
            } ?: return null
        }

        RelatableMusicBrainzEntity.WORK -> {
            if (work == null) return null
            work?.apply {
                linkedEntityId = id
                linkedEntityName = targetCredit.emptyToNull() ?: name
                linkedEntityDisambiguation = disambiguation
            } ?: return null
        }

        RelatableMusicBrainzEntity.URL -> {
            if (url == null) return null
            url?.apply {
                linkedEntityId = id
                linkedEntityName = resource.decodeUrl()
                linkedEntityDisambiguation = null
            } ?: return null
        }
    }

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
    )
}

internal expect fun String.decodeUrl(): String
