package ly.david.musicsearch.data.repository.label

import androidx.paging.PagingData
import androidx.paging.PagingSource
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.data.database.dao.AliasDao
import ly.david.musicsearch.data.database.dao.BrowseRemoteMetadataDao
import ly.david.musicsearch.data.database.dao.CollectionEntityDao
import ly.david.musicsearch.data.database.dao.LabelDao
import ly.david.musicsearch.data.musicbrainz.api.BrowseApi
import ly.david.musicsearch.data.musicbrainz.api.BrowseLabelsResponse
import ly.david.musicsearch.data.musicbrainz.models.core.LabelMusicBrainzNetworkModel
import ly.david.musicsearch.data.repository.base.BrowseEntities
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.list.ListFilters
import ly.david.musicsearch.shared.domain.label.LabelsListRepository
import ly.david.musicsearch.shared.domain.listitem.LabelListItemModel
import ly.david.musicsearch.shared.domain.musicbrainz.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType

class LabelsListRepositoryImpl(
    private val labelDao: LabelDao,
    private val browseRemoteMetadataDao: BrowseRemoteMetadataDao,
    private val collectionEntityDao: CollectionEntityDao,
    private val browseApi: BrowseApi,
    aliasDao: AliasDao,
) : LabelsListRepository,
    BrowseEntities<LabelListItemModel, LabelMusicBrainzNetworkModel, BrowseLabelsResponse>(
        browseEntity = MusicBrainzEntityType.LABEL,
        browseRemoteMetadataDao = browseRemoteMetadataDao,
        aliasDao = aliasDao,
    ) {

    override fun observeLabels(
        browseMethod: BrowseMethod,
        listFilters: ListFilters,
    ): Flow<PagingData<LabelListItemModel>> {
        return observeEntities(
            browseMethod = browseMethod,
            listFilters = listFilters,
        )
    }

    override fun getLinkedEntitiesPagingSource(
        browseMethod: BrowseMethod,
        listFilters: ListFilters,
    ): PagingSource<Int, LabelListItemModel> {
        return labelDao.getLabels(
            browseMethod = browseMethod,
            query = listFilters.query,
        )
    }

    override fun deleteEntityLinksByEntity(
        entity: MusicBrainzEntity,
    ) {
        browseRemoteMetadataDao.withTransaction {
            browseRemoteMetadataDao.deleteBrowseRemoteCountByEntity(
                entityId = entity.id,
                browseEntity = browseEntity,
            )

            when (entity.type) {
                MusicBrainzEntityType.COLLECTION -> {
                    collectionEntityDao.deleteAllFromCollection(entity.id)
                }

                else -> {
                    labelDao.deleteLabelLinksByEntity(entity.id)
                }
            }
        }
    }

    override suspend fun browseEntitiesByEntity(
        entity: MusicBrainzEntity,
        offset: Int,
    ): BrowseLabelsResponse {
        return browseApi.browseLabelsByEntity(
            entityId = entity.id,
            entity = entity.type,
            offset = offset,
        )
    }

    override fun insertAll(
        entity: MusicBrainzEntity,
        musicBrainzModels: List<LabelMusicBrainzNetworkModel>,
    ) {
        labelDao.insertAll(musicBrainzModels)
        when (entity.type) {
            MusicBrainzEntityType.COLLECTION -> {
                collectionEntityDao.addAllToCollection(
                    collectionId = entity.id,
                    entityIds = musicBrainzModels.map { label -> label.id },
                )
            }

            else -> {
                labelDao.insertLabelsByEntity(
                    entityId = entity.id,
                    labelIds = musicBrainzModels.map { label -> label.id },
                )
            }
        }
    }

    override fun getLocalLinkedEntitiesCountByEntity(
        entity: MusicBrainzEntity,
    ): Int {
        return when (entity.type) {
            MusicBrainzEntityType.COLLECTION -> {
                collectionEntityDao.getCountOfEntitiesByCollection(entity.id)
            }

            else -> {
                labelDao.getCountOfLabelsByEntity(entity.id)
            }
        }
    }
}
