package ly.david.musicsearch.data.database.dao

import app.cash.paging.PagingSource
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOne
import app.cash.sqldelight.paging3.QueryPagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ly.david.musicsearch.core.coroutines.CoroutineDispatchers
import ly.david.musicsearch.data.database.Database
import ly.david.musicsearch.data.database.mapper.mapToLabelListItemModel
import ly.david.musicsearch.data.musicbrainz.models.core.LabelInfo
import ly.david.musicsearch.data.musicbrainz.models.core.LabelMusicBrainzModel
import ly.david.musicsearch.shared.domain.LifeSpanUiModel
import ly.david.musicsearch.shared.domain.label.LabelDetailsModel
import ly.david.musicsearch.shared.domain.listitem.LabelListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import lydavidmusicsearchdatadatabase.Label
import lydavidmusicsearchdatadatabase.Labels_by_entity

class LabelDao(
    database: Database,
    private val coroutineDispatchers: CoroutineDispatchers,
    private val releaseLabelDao: ReleaseLabelDao,
) : EntityDao {
    override val transacter = database.labelQueries

    fun insert(label: LabelMusicBrainzModel) {
        label.run {
            transacter.insertLabel(
                Label(
                    id = id,
                    name = name,
                    disambiguation = disambiguation,
                    type = type,
                    type_id = typeId,
                    label_code = labelCode,
                    ipis = ipis,
                    isnis = isnis,
                    begin = label.lifeSpan?.begin,
                    end = label.lifeSpan?.end,
                    ended = label.lifeSpan?.ended,
                ),
            )
        }
    }

    fun insertAll(labels: List<LabelMusicBrainzModel>?) {
        transacter.transaction {
            labels?.forEach { label ->
                insert(label)
            }
        }
    }

    fun getLabelForDetails(labelId: String): LabelDetailsModel? {
        return transacter.getLabelForDetails(
            id = labelId,
            mapper = ::toDetailsModel,
        ).executeAsOneOrNull()
    }

    private fun toDetailsModel(
        id: String,
        name: String,
        disambiguation: String?,
        type: String?,
        labelCode: Int?,
        ipis: List<String>?,
        isnis: List<String>?,
        begin: String?,
        end: String?,
        ended: Boolean?,
    ) = LabelDetailsModel(
        id = id,
        name = name,
        disambiguation = disambiguation,
        type = type,
        labelCode = labelCode,
        ipis = ipis,
        isnis = isnis,
        lifeSpan = LifeSpanUiModel(
            begin = begin,
            end = end,
            ended = ended,
        ),
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
    ): Int {
        return transacter.transactionWithResult {
            labelIds.forEach { labelId ->
                insertLabelByEntity(
                    entityId = entityId,
                    labelId = labelId,
                )
            }
            labelIds.size
        }
    }

    fun deleteLabelsByEntity(entityId: String) {
        transacter.deleteLabelsByEntity(entityId)
    }

    fun observeCountOfLabelsByEntity(entityId: String): Flow<Int> =
        transacter.getNumberOfLabelsByEntity(
            entityId = entityId,
            query = "%%",
        )
            .asFlow()
            .mapToOne(coroutineDispatchers.io)
            .map { it.toInt() }

    fun getCountOfLabelsByEntity(entityId: String): Int =
        transacter.getNumberOfLabelsByEntity(
            entityId = entityId,
            query = "%%",
        )
            .executeAsOne()
            .toInt()

    fun getLabels(
        entityId: String?,
        entity: MusicBrainzEntity?,
        query: String,
    ): PagingSource<Int, LabelListItemModel> = when {
        entityId == null || entity == null -> {
            getAllLabels(
                query = query,
            )
        }

        entity == MusicBrainzEntity.COLLECTION -> {
            getLabelsByCollection(
                collectionId = entityId,
                query = query,
            )
        }

        else -> {
            getLabelsByEntity(
                entityId = entityId,
                query = query,
            )
        }
    }

    private fun getAllLabels(
        query: String,
    ): PagingSource<Int, LabelListItemModel> = QueryPagingSource(
        countQuery = transacter.getCountOfAllLabels(
            query = "%$query%",
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
        countQuery = transacter.getNumberOfLabelsByEntity(
            entityId = entityId,
            query = "%$query%",
        ),
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
