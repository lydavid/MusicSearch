package ly.david.musicsearch.data.repository.label

import app.cash.paging.PagingData
import app.cash.paging.PagingSource
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.data.database.dao.BrowseEntityCountDao
import ly.david.musicsearch.data.database.dao.CollectionEntityDao
import ly.david.musicsearch.data.database.dao.LabelDao
import ly.david.musicsearch.data.database.dao.LabelsByEntityDao
import ly.david.musicsearch.data.musicbrainz.api.BrowseApi
import ly.david.musicsearch.data.musicbrainz.api.BrowseLabelsResponse
import ly.david.musicsearch.data.musicbrainz.models.core.LabelMusicBrainzModel
import ly.david.musicsearch.data.repository.base.BrowseEntitiesByEntity
import ly.david.musicsearch.shared.domain.ListFilters
import ly.david.musicsearch.shared.domain.label.LabelsByEntityRepository
import ly.david.musicsearch.shared.domain.listitem.LabelListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity

class LabelsByEntityRepositoryImpl(
    private val labelsByEntityDao: LabelsByEntityDao,
    private val browseEntityCountDao: BrowseEntityCountDao,
    private val collectionEntityDao: CollectionEntityDao,
    private val labelDao: LabelDao,
    private val browseApi: BrowseApi,
) : LabelsByEntityRepository,
    BrowseEntitiesByEntity<LabelListItemModel, LabelMusicBrainzModel, BrowseLabelsResponse>(
        browseEntity = MusicBrainzEntity.LABEL,
        browseEntityCountDao = browseEntityCountDao,
    ) {

    override fun observeLabelsByEntity(
        entityId: String,
        entity: MusicBrainzEntity,
        listFilters: ListFilters,
    ): Flow<PagingData<LabelListItemModel>> {
        return observeEntitiesByEntity(
            entityId = entityId,
            entity = entity,
            listFilters = listFilters,
        )
    }

    override fun deleteLinkedEntitiesByEntity(
        entityId: String,
        entity: MusicBrainzEntity,
    ) {
        browseEntityCountDao.withTransaction {
            browseEntityCountDao.deleteBrowseEntityCountByEntity(
                entityId = entityId,
                browseEntity = browseEntity,
            )

            when (entity) {
                MusicBrainzEntity.COLLECTION -> {
                    collectionEntityDao.deleteAllFromCollection(entityId)
                }

                else -> {
                    labelsByEntityDao.deleteLabelsByEntity(entityId)
                }
            }
        }
    }

    override fun getLinkedEntitiesPagingSource(
        entityId: String?,
        entity: MusicBrainzEntity?,
        listFilters: ListFilters,
    ): PagingSource<Int, LabelListItemModel> {
        return when {
            entityId == null || entity == null -> {
                error("not possible")
            }

            entity == MusicBrainzEntity.COLLECTION -> {
                collectionEntityDao.getLabelsByCollection(
                    collectionId = entityId,
                    query = listFilters.query,
                )
            }

            else -> {
                labelsByEntityDao.getLabelsByEntity(
                    entityId = entityId,
                    query = listFilters.query,
                )
            }
        }
    }

    override suspend fun browseEntities(
        entityId: String,
        entity: MusicBrainzEntity,
        offset: Int,
    ): BrowseLabelsResponse {
        return browseApi.browseLabelsByEntity(
            entityId = entityId,
            entity = entity,
            offset = offset,
        )
    }

    override fun insertAllLinkingModels(
        entityId: String,
        entity: MusicBrainzEntity,
        musicBrainzModels: List<LabelMusicBrainzModel>,
    ): Int {
        labelDao.insertAll(musicBrainzModels)
        return when (entity) {
            MusicBrainzEntity.COLLECTION -> {
                collectionEntityDao.insertAll(
                    collectionId = entityId,
                    entityIds = musicBrainzModels.map { label -> label.id },
                )
            }

            else -> {
                labelsByEntityDao.insertAll(
                    entityId = entityId,
                    labelIds = musicBrainzModels.map { label -> label.id },
                )
            }
        }
    }
}
