package ly.david.musicsearch.data.database.dao

import app.cash.paging.PagingSource
import app.cash.sqldelight.paging3.QueryPagingSource
import java.net.URLDecoder
import kotlinx.coroutines.Dispatchers
import ly.david.data.core.common.emptyToNull
import ly.david.data.core.common.transformThisIfNotNullOrEmpty
import ly.david.data.core.getDisplayNames
import ly.david.data.core.getLifeSpanForDisplay
import ly.david.data.core.network.MusicBrainzEntity
import ly.david.data.musicbrainz.RelationMusicBrainzModel
import ly.david.data.musicbrainz.getFormattedAttributesForDisplay
import ly.david.data.musicbrainz.getHeader
import ly.david.musicsearch.data.database.Database
import lydavidmusicsearchdatadatabase.Mb_relation

// TODO: can we add annotations without ksp, will it be slow?
class RelationDao(
    private val database: Database,
//    private val dispatcher: CoroutineDispatcher,
) {
    private val transacter = database.mb_relationQueries

    fun insert(relation: Mb_relation) {
        transacter.insertEntity(relation)
    }

    fun insertAll(relations: List<Mb_relation>) {
        transacter.transaction {
            relations.forEach { relation ->
                insert(relation)
            }
        }
    }

    fun getEntityRelationshipsExcludingUrls(
        entityId: String,
        query: String = "%%",
    ): PagingSource<Int, Mb_relation> {
        return QueryPagingSource(
            countQuery = transacter.countEntityRelationshipsExcludingUrls(
                entityId = entityId,
                query = query,
            ),
            transacter = transacter,
            context = Dispatchers.IO, // TODO: inject so we can swap out
        ) { limit, offset ->
            transacter.getEntityRelationshipsExcludingUrls(
                entityId = entityId,
                query = query,
                limit = limit,
                offset = offset,
            )
        }
    }

    fun deleteRelationshipsExcludingUrlsByEntity(
        entityId: String,
    ) {
        transacter.deleteRelationshipsExcludingUrlsByEntity(entityId)
    }
}

/**
 * We cannot guarantee that a [Mb_relation] will be created in the scenario that target-type points to a resource
 * but that object is null. It's possible that this is never the case, but our models are currently structured such
 * that any of them are nullable.
 */
fun RelationMusicBrainzModel.toRelationDatabaseModel(
    entityId: String,
    order: Int,
): Mb_relation? {
    var linkedEntityId = ""
    var linkedEntityName = ""
    var linkedEntityDisambiguation: String? = null
    var additionalInfo: String? = null
    val linkedTargetType = targetType
    when (linkedTargetType) {
        MusicBrainzEntity.ARTIST -> {
            if (artist == null) return null
            artist?.apply {
                linkedEntityId = id
                linkedEntityName = targetCredit.emptyToNull() ?: name
                linkedEntityDisambiguation = disambiguation
                additionalInfo = getLifeSpanForDisplay().transformThisIfNotNullOrEmpty { "($it)" }
            }
        }

        MusicBrainzEntity.RELEASE_GROUP -> {
            if (releaseGroup == null) return null
            releaseGroup?.apply {
                linkedEntityId = id
                linkedEntityName = targetCredit.emptyToNull() ?: name
                linkedEntityDisambiguation = disambiguation
                additionalInfo = getLifeSpanForDisplay().transformThisIfNotNullOrEmpty { "($it)" }
            } ?: return null
        }

        MusicBrainzEntity.RELEASE -> {
            if (release == null) return null
            release?.apply {
                linkedEntityId = id
                linkedEntityName = targetCredit.emptyToNull() ?: name
                linkedEntityDisambiguation = disambiguation
                additionalInfo = getLifeSpanForDisplay().transformThisIfNotNullOrEmpty { "($it)" }
            } ?: return null
        }

        MusicBrainzEntity.RECORDING -> {
            if (recording == null) return null
            recording?.apply {
                linkedEntityId = id
                linkedEntityName = targetCredit.emptyToNull() ?: name
                linkedEntityDisambiguation = disambiguation
                additionalInfo = artistCredits.getDisplayNames().transformThisIfNotNullOrEmpty { "by $it" } +
                    getLifeSpanForDisplay().transformThisIfNotNullOrEmpty { "($it)" }
            } ?: return null
        }

        MusicBrainzEntity.LABEL -> {
            if (label == null) return null
            label?.apply {
                linkedEntityId = id
                linkedEntityName = targetCredit.emptyToNull() ?: name
                linkedEntityDisambiguation = disambiguation
            } ?: return null
        }

        MusicBrainzEntity.AREA -> {
            if (area == null) return null
            area?.apply {
                linkedEntityId = id
                linkedEntityName = targetCredit.emptyToNull() ?: name
                linkedEntityDisambiguation = disambiguation
            } ?: return null
        }

        MusicBrainzEntity.PLACE -> {
            if (place == null) return null
            place?.apply {
                linkedEntityId = id
                linkedEntityName = targetCredit.emptyToNull() ?: name
                linkedEntityDisambiguation = disambiguation
            } ?: return null
        }

        MusicBrainzEntity.WORK -> {
            if (work == null) return null
            work?.apply {
                linkedEntityId = id
                linkedEntityName = targetCredit.emptyToNull() ?: name
                linkedEntityDisambiguation = disambiguation
            } ?: return null
        }

        MusicBrainzEntity.INSTRUMENT -> {
            if (instrument == null) return null
            instrument?.apply {
                linkedEntityId = id
                linkedEntityName = targetCredit.emptyToNull() ?: name
                linkedEntityDisambiguation = disambiguation
            } ?: return null
        }

        MusicBrainzEntity.GENRE -> {
            if (genre == null) return null
            genre?.apply {
                linkedEntityId = id
                linkedEntityName = targetCredit.emptyToNull() ?: name
                linkedEntityDisambiguation = disambiguation
            } ?: return null
        }

        MusicBrainzEntity.EVENT -> {
            if (event == null) return null
            event?.apply {
                linkedEntityId = id
                linkedEntityName = targetCredit.emptyToNull() ?: name
                linkedEntityDisambiguation = disambiguation
            } ?: return null
        }

        MusicBrainzEntity.SERIES -> {
            if (series == null) return null
            series?.apply {
                linkedEntityId = id
                linkedEntityName = targetCredit.emptyToNull() ?: name
                linkedEntityDisambiguation = disambiguation
            } ?: return null
        }

        MusicBrainzEntity.URL -> {
            if (url == null) return null
            url?.apply {
                linkedEntityId = id
                linkedEntityName = URLDecoder.decode(resource, "utf-8")
                linkedEntityDisambiguation = null
            } ?: return null
        }

        else -> return null
    }

    return Mb_relation(
        entity_id = entityId,
        linked_entity_id = linkedEntityId,
        linked_entity = linkedTargetType,
        order = order,
        label = getHeader(),
        name = linkedEntityName,
        disambiguation = linkedEntityDisambiguation,
        attributes = getFormattedAttributesForDisplay(),
        additional_info = additionalInfo
    )
}
