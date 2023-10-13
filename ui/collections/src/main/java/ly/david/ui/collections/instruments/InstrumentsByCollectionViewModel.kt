package ly.david.ui.collections.instruments

import androidx.paging.PagingSource
import ly.david.musicsearch.data.core.network.MusicBrainzEntity
import ly.david.data.musicbrainz.InstrumentMusicBrainzModel
import ly.david.data.musicbrainz.api.BrowseInstrumentsResponse
import ly.david.data.musicbrainz.api.MusicBrainzApi
import ly.david.musicsearch.data.database.dao.BrowseEntityCountDao
import ly.david.musicsearch.data.database.dao.CollectionEntityDao
import ly.david.musicsearch.data.database.dao.InstrumentDao
import ly.david.musicsearch.data.core.listitem.InstrumentListItemModel
import ly.david.musicsearch.domain.listitem.toInstrumentListItemModel
import ly.david.ui.common.instrument.InstrumentsPagedList
import ly.david.ui.common.paging.BrowseEntitiesByEntityViewModel
import lydavidmusicsearchdatadatabase.Instrument
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class InstrumentsByCollectionViewModel(
    private val musicBrainzApi: MusicBrainzApi,
    private val collectionEntityDao: CollectionEntityDao,
    private val instrumentDao: InstrumentDao,
    private val browseEntityCountDao: BrowseEntityCountDao,
    pagedList: InstrumentsPagedList,
) : BrowseEntitiesByEntityViewModel<Instrument, InstrumentListItemModel, InstrumentMusicBrainzModel, BrowseInstrumentsResponse>(
    byEntity = MusicBrainzEntity.INSTRUMENT,
    browseEntityCountDao = browseEntityCountDao,
    pagedList = pagedList,
) {

    override suspend fun browseEntitiesByEntity(entityId: String, offset: Int): BrowseInstrumentsResponse {
        return musicBrainzApi.browseInstrumentsByCollection(
            collectionId = entityId,
            offset = offset
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
    ): PagingSource<Int, Instrument> =
        collectionEntityDao.getInstrumentsByCollection(
            collectionId = entityId,
            query = "%$query%"
        )

    override fun transformDatabaseToListItemModel(databaseModel: Instrument): InstrumentListItemModel {
        return databaseModel.toInstrumentListItemModel()
    }
}
