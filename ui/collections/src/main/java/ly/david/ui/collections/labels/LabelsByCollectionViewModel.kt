package ly.david.ui.collections.labels

import androidx.paging.PagingSource
import ly.david.data.core.network.MusicBrainzEntity
import ly.david.data.domain.listitem.LabelListItemModel
import ly.david.data.domain.listitem.toLabelListItemModel
import ly.david.data.musicbrainz.LabelMusicBrainzModel
import ly.david.data.musicbrainz.api.BrowseLabelsResponse
import ly.david.data.musicbrainz.api.MusicBrainzApi
import ly.david.musicsearch.data.database.dao.BrowseEntityCountDao
import ly.david.musicsearch.data.database.dao.CollectionEntityDao
import ly.david.musicsearch.data.database.dao.LabelDao
import ly.david.ui.common.label.LabelsPagedList
import ly.david.ui.common.paging.BrowseEntitiesByEntityViewModel
import lydavidmusicsearchdatadatabase.Label
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class LabelsByCollectionViewModel(
    private val musicBrainzApi: MusicBrainzApi,
    private val collectionEntityDao: CollectionEntityDao,
    private val labelDao: LabelDao,
    private val browseEntityCountDao: BrowseEntityCountDao,
    pagedList: LabelsPagedList,
) : BrowseEntitiesByEntityViewModel<Label, LabelListItemModel, LabelMusicBrainzModel, BrowseLabelsResponse>(
    byEntity = MusicBrainzEntity.LABEL,
    browseEntityCountDao = browseEntityCountDao,
    pagedList = pagedList,
) {

    override suspend fun browseEntitiesByEntity(entityId: String, offset: Int): BrowseLabelsResponse {
        return musicBrainzApi.browseLabelsByCollection(
            collectionId = entityId,
            offset = offset,
        )
    }

    override suspend fun insertAllLinkingModels(entityId: String, musicBrainzModels: List<LabelMusicBrainzModel>) {
        labelDao.insertAll(musicBrainzModels)
        collectionEntityDao.insertAll(
            entityId,
            musicBrainzModels.map { label -> label.id },
        )
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
    ): PagingSource<Int, Label> =
        collectionEntityDao.getLabelsByCollection(
            collectionId = entityId,
            query = "%$query%",
        )

    override fun transformDatabaseToListItemModel(databaseModel: Label): LabelListItemModel {
        return databaseModel.toLabelListItemModel()
    }
}
