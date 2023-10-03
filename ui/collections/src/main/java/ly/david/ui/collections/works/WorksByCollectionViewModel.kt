package ly.david.ui.collections.works

import androidx.paging.PagingSource
import ly.david.data.core.network.MusicBrainzEntity
import ly.david.data.musicbrainz.WorkMusicBrainzModel
import ly.david.data.musicbrainz.api.BrowseWorksResponse
import ly.david.data.musicbrainz.api.MusicBrainzApi
import ly.david.musicsearch.data.database.dao.BrowseEntityCountDao
import ly.david.musicsearch.data.database.dao.CollectionEntityDao
import ly.david.musicsearch.data.database.dao.WorkDao
import ly.david.musicsearch.domain.listitem.WorkListItemModel
import ly.david.musicsearch.domain.listitem.toWorkListItemModel
import ly.david.ui.common.paging.BrowseEntitiesByEntityViewModel
import ly.david.ui.common.work.WorksPagedList
import lydavidmusicsearchdatadatabase.Work
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class WorksByCollectionViewModel(
    private val musicBrainzApi: MusicBrainzApi,
    private val collectionEntityDao: CollectionEntityDao,
    private val workDao: WorkDao,
    private val browseEntityCountDao: BrowseEntityCountDao,
    pagedList: WorksPagedList,
) : BrowseEntitiesByEntityViewModel<Work, WorkListItemModel, WorkMusicBrainzModel, BrowseWorksResponse>(
    byEntity = MusicBrainzEntity.WORK,
    browseEntityCountDao = browseEntityCountDao,
    pagedList = pagedList
) {

    override suspend fun browseEntitiesByEntity(entityId: String, offset: Int): BrowseWorksResponse {
        return musicBrainzApi.browseWorksByCollection(
            collectionId = entityId,
            offset = offset
        )
    }

    override suspend fun insertAllLinkingModels(entityId: String, musicBrainzModels: List<WorkMusicBrainzModel>) {
        collectionEntityDao.withTransaction {
            workDao.insertAll(musicBrainzModels)
            collectionEntityDao.insertAll(
                collectionId = entityId,
                entityIds = musicBrainzModels.map { work -> work.id },
            )
        }
    }

    override suspend fun deleteLinkedEntitiesByEntity(entityId: String) {
        collectionEntityDao.withTransaction {
            collectionEntityDao.deleteAllFromCollection(entityId)
            browseEntityCountDao.deleteBrowseEntityCountByEntity(entityId, MusicBrainzEntity.AREA)
        }
    }

    override fun getLinkedEntitiesPagingSource(
        entityId: String,
        query: String,
    ): PagingSource<Int, Work> =
        collectionEntityDao.getWorksByCollection(
            collectionId = entityId,
            query = "%$query%",
        )

    override fun transformDatabaseToListItemModel(databaseModel: Work): WorkListItemModel {
        return databaseModel.toWorkListItemModel()
    }
}
