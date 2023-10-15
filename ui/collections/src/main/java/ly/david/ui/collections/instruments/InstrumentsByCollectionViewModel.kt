package ly.david.ui.collections.instruments

import androidx.paging.PagingSource
import ly.david.data.musicbrainz.InstrumentMusicBrainzModel
import ly.david.data.musicbrainz.api.BrowseInstrumentsResponse
import ly.david.data.musicbrainz.api.MusicBrainzApi
import ly.david.musicsearch.core.models.listitem.InstrumentListItemModel
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.data.database.dao.BrowseEntityCountDao
import ly.david.musicsearch.data.database.dao.CollectionEntityDao
import ly.david.musicsearch.data.database.dao.InstrumentDao
import ly.david.ui.common.instrument.InstrumentsPagedList
import ly.david.ui.common.paging.BrowseEntitiesByEntityViewModel
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class InstrumentsByCollectionViewModel(
    private val musicBrainzApi: MusicBrainzApi,
    private val collectionEntityDao: CollectionEntityDao,
    private val instrumentDao: InstrumentDao,
    private val browseEntityCountDao: BrowseEntityCountDao,
    pagedList: InstrumentsPagedList,
) : BrowseEntitiesByEntityViewModel<InstrumentListItemModel, InstrumentMusicBrainzModel, BrowseInstrumentsResponse>(
    byEntity = MusicBrainzEntity.INSTRUMENT,
    browseEntityCountDao = browseEntityCountDao,
    pagedList = pagedList,
) {

    override suspend fun browseEntitiesByEntity(entityId: String, offset: Int): BrowseInstrumentsResponse {
        return musicBrainzApi.browseInstrumentsByCollection(
            collectionId = entityId,
            offset = offset,
        )
    }

    override suspend fun insertAllLinkingModels(entityId: String, musicBrainzModels: List<InstrumentMusicBrainzModel>) {
        instrumentDao.insertAll(musicBrainzModels)
        collectionEntityDao.insertAll(
            entityId,
            musicBrainzModels.map { instrument -> instrument.id },
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
    ): PagingSource<Int, InstrumentListItemModel> =
        collectionEntityDao.getInstrumentsByCollection(
            collectionId = entityId,
            query = "%$query%",
        )
}
