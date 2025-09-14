package ly.david.musicsearch.data.database.dao

import app.cash.paging.PagingSource
import app.cash.sqldelight.Query
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOne
import app.cash.sqldelight.paging3.QueryPagingSource
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ly.david.musicsearch.data.database.Database
import ly.david.musicsearch.data.database.mapper.mapToLabelListItemModel
import ly.david.musicsearch.data.musicbrainz.models.core.LabelInfo
import ly.david.musicsearch.data.musicbrainz.models.core.LabelMusicBrainzNetworkModel
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.LifeSpanUiModel
import ly.david.musicsearch.shared.domain.coroutine.CoroutineDispatchers
import ly.david.musicsearch.shared.domain.details.LabelDetailsModel
import ly.david.musicsearch.shared.domain.listitem.LabelListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import lydavidmusicsearchdatadatabase.Label
import lydavidmusicsearchdatadatabase.Labels_by_entity
import kotlin.time.Clock
import kotlin.time.Instant

class LabelDao(
    database: Database,
    private val releaseLabelDao: ReleaseLabelDao,
    private val collectionEntityDao: CollectionEntityDao,
    private val coroutineDispatchers: CoroutineDispatchers,
) : EntityDao {
    override val transacter = database.labelQueries

    fun upsert(
        oldId: String,
        label: LabelMusicBrainzNetworkModel,
    ) {
        label.run {
            if (oldId != id) {
                delete(oldId)
            }
            transacter.upsert(
                id = id,
                name = name,
                disambiguation = disambiguation.orEmpty(),
                type = type.orEmpty(),
                type_id = typeId.orEmpty(),
                label_code = labelCode,
                ipis = ipis.orEmpty(),
                isnis = isnis.orEmpty(),
                begin = label.lifeSpan?.begin.orEmpty(),
                end = label.lifeSpan?.end.orEmpty(),
                ended = label.lifeSpan?.ended == true,
            )
        }
    }

    fun insertAll(labels: List<LabelMusicBrainzNetworkModel>?) {
        transacter.transaction {
            labels?.forEach { label ->
                label.run {
                    transacter.insertIgnore(
                        label = Label(
                            id = id,
                            name = name,
                            disambiguation = disambiguation.orEmpty(),
                            type = type.orEmpty(),
                            type_id = typeId.orEmpty(),
                            label_code = labelCode,
                            ipis = ipis.orEmpty(),
                            isnis = isnis.orEmpty(),
                            begin = label.lifeSpan?.begin.orEmpty(),
                            end = label.lifeSpan?.end.orEmpty(),
                            ended = label.lifeSpan?.ended == true,
                        ),
                    )
                }
            }
        }
    }

    fun getLabelForDetails(labelId: String): LabelDetailsModel? {
        return transacter.getLabelForDetails(
            labelId = labelId,
            mapper = ::toDetailsModel,
        ).executeAsOneOrNull()
    }

    private fun toDetailsModel(
        id: String,
        name: String,
        disambiguation: String,
        type: String,
        labelCode: Int?,
        ipis: List<String>,
        isnis: List<String>,
        begin: String,
        end: String,
        ended: Boolean,
        lastUpdated: Instant?,
    ) = LabelDetailsModel(
        id = id,
        name = name,
        disambiguation = disambiguation,
        type = type,
        labelCode = labelCode,
        ipis = ipis.toPersistentList(),
        isnis = isnis.toPersistentList(),
        lifeSpan = LifeSpanUiModel(
            begin = begin,
            end = end,
            ended = ended,
        ),
        lastUpdated = lastUpdated ?: Clock.System.now(),
    )

    fun delete(id: String) {
        transacter.deleteLabel(id)
    }

    private fun insertLabelByEntity(
        entityId: String,
        labelId: String,
    ) {
        transacter.insertOrIgnoreLabelByEntity(
            Labels_by_entity(
                entity_id = entityId,
                label_id = labelId,
            ),
        )
    }

    fun insertLabelsByEntity(
        entityId: String,
        labelIds: List<String>,
    ) {
        transacter.transaction {
            labelIds.forEach { labelId ->
                insertLabelByEntity(
                    entityId = entityId,
                    labelId = labelId,
                )
            }
        }
    }

    fun deleteLabelLinksByEntity(entityId: String) {
        transacter.deleteLabelLinksByEntity(entityId)
    }

    fun getCountOfLabelsByEntity(entityId: String): Int =
        getCountOfLabelsByEntityQuery(
            entityId = entityId,
            query = "",
        )
            .executeAsOne()
            .toInt()

    fun getLabels(
        browseMethod: BrowseMethod,
        query: String,
    ): PagingSource<Int, LabelListItemModel> = when (browseMethod) {
        is BrowseMethod.All -> {
            getAllLabels(
                query = query,
            )
        }

        is BrowseMethod.ByEntity -> {
            if (browseMethod.entity == MusicBrainzEntityType.COLLECTION) {
                getLabelsByCollection(
                    collectionId = browseMethod.entityId,
                    query = query,
                )
            } else {
                getLabelsByEntity(
                    entityId = browseMethod.entityId,
                    query = query,
                )
            }
        }
    }

    fun observeCountOfLabels(browseMethod: BrowseMethod): Flow<Int> =
        when (browseMethod) {
            is BrowseMethod.ByEntity -> {
                if (browseMethod.entity == MusicBrainzEntityType.COLLECTION) {
                    collectionEntityDao.getCountOfEntitiesByCollectionQuery(
                        collectionId = browseMethod.entityId,
                    )
                } else {
                    getCountOfLabelsByEntityQuery(
                        entityId = browseMethod.entityId,
                        query = "",
                    )
                }
            }

            else -> {
                getCountOfAllLabels(query = "")
            }
        }
            .asFlow()
            .mapToOne(coroutineDispatchers.io)
            .map { it.toInt() }

    private fun getCountOfAllLabels(
        query: String,
    ): Query<Long> = transacter.getCountOfAllLabels(
        query = "%$query%",
    )

    private fun getAllLabels(
        query: String,
    ): PagingSource<Int, LabelListItemModel> = QueryPagingSource(
        countQuery = getCountOfAllLabels(
            query = query,
        ),
        transacter = transacter,
        context = coroutineDispatchers.io,
        queryProvider = { limit, offset ->
            transacter.getAllLabels(
                query = "%$query%",
                limit = limit,
                offset = offset,
                mapper = ::mapToLabelListItemModel,
            )
        },
    )

    private fun getLabelsByCollection(
        collectionId: String,
        query: String,
    ): PagingSource<Int, LabelListItemModel> = QueryPagingSource(
        countQuery = transacter.getNumberOfLabelsByCollection(
            collectionId = collectionId,
            query = "%$query%",
        ),
        transacter = transacter,
        context = coroutineDispatchers.io,
        queryProvider = { limit, offset ->
            transacter.getLabelsByCollection(
                collectionId = collectionId,
                query = "%$query%",
                limit = limit,
                offset = offset,
                mapper = ::mapToLabelListItemModel,
            )
        },
    )

    private fun getLabelsByEntity(
        entityId: String,
        query: String,
    ): PagingSource<Int, LabelListItemModel> = QueryPagingSource(
        countQuery = getCountOfLabelsByEntityQuery(entityId, query),
        transacter = transacter,
        context = coroutineDispatchers.io,
        queryProvider = { limit, offset ->
            transacter.getLabelsByEntity(
                entityId = entityId,
                query = "%$query%",
                limit = limit,
                offset = offset,
                mapper = ::mapToLabelListItemModel,
            )
        },
    )

    private fun getCountOfLabelsByEntityQuery(
        entityId: String,
        query: String,
    ) = transacter.getNumberOfLabelsByEntity(
        entityId = entityId,
        query = "%$query%",
    )

    // region labels by release
    fun insertLabelsByRelease(
        releaseId: String,
        labelInfoList: List<LabelInfo>?,
    ) {
        transacter.transaction {
            labelInfoList?.forEach { labelInfo ->
                val labelId = labelInfo.label?.id ?: return@forEach
                insertLabelByEntity(
                    entityId = releaseId,
                    labelId = labelId,
                )
                releaseLabelDao.insertOrIgnore(
                    releaseId = releaseId,
                    labelId = labelId,
                    catalogNumber = labelInfo.catalogNumber.orEmpty(),
                )
            }
        }
    }

    fun getLabelsByRelease(
        releaseId: String,
    ): List<LabelListItemModel> = transacter.getLabelsByRelease(
        releaseId = releaseId,
        mapper = ::mapToLabelListItemModel,
    ).executeAsList()

    fun deleteReleaseLabelLinks(releaseId: String) {
        releaseLabelDao.deleteReleaseLabelLinks(releaseId)
    }
    // endregion
}
