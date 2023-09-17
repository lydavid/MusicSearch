package ly.david.ui.collections.instruments

import androidx.paging.PagingSource
import ly.david.data.core.network.MusicBrainzEntity
import ly.david.data.domain.listitem.InstrumentListItemModel
import ly.david.data.domain.listitem.toInstrumentListItemModel
import ly.david.data.musicbrainz.InstrumentMusicBrainzModel
import ly.david.data.musicbrainz.api.BrowseInstrumentsResponse
import ly.david.data.musicbrainz.api.MusicBrainzApi
import ly.david.data.room.collection.RoomCollectionEntityDao
import ly.david.data.room.collection.CollectionEntityRoomModel
import ly.david.data.room.instrument.InstrumentDao
import ly.david.data.room.instrument.InstrumentRoomModel
import ly.david.data.room.instrument.toInstrumentRoomModel
import ly.david.data.room.relation.RoomRelationDao
import ly.david.ui.common.instrument.InstrumentsPagedList
import ly.david.ui.common.paging.BrowseEntitiesByEntityViewModel
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class InstrumentsByCollectionViewModel(
    private val musicBrainzApi: MusicBrainzApi,
    private val collectionEntityDao: RoomCollectionEntityDao,
    private val instrumentDao: InstrumentDao,
    private val relationDao: RoomRelationDao,
    pagedList: InstrumentsPagedList,
) : BrowseEntitiesByEntityViewModel<InstrumentRoomModel, InstrumentListItemModel, InstrumentMusicBrainzModel, BrowseInstrumentsResponse>(
    byEntity = MusicBrainzEntity.INSTRUMENT,
    relationDao = relationDao,
    pagedList = pagedList,
) {

    override suspend fun browseEntitiesByEntity(entityId: String, offset: Int): BrowseInstrumentsResponse {
        return musicBrainzApi.browseInstrumentsByCollection(
            collectionId = entityId,
            offset = offset
        )
    }

    override suspend fun insertAllLinkingModels(entityId: String, musicBrainzModels: List<InstrumentMusicBrainzModel>) {
        instrumentDao.insertAll(musicBrainzModels.map { it.toInstrumentRoomModel() })
        collectionEntityDao.insertAll(
            musicBrainzModels.map { instrument ->
                CollectionEntityRoomModel(
                    id = entityId,
                    entityId = instrument.id
                )
            }
        )
    }

    override suspend fun deleteLinkedEntitiesByEntity(entityId: String) {
        collectionEntityDao.withTransaction {
            collectionEntityDao.deleteAllFromCollection(entityId)
            relationDao.deleteBrowseEntityCountByEntity(entityId, MusicBrainzEntity.AREA)
        }
    }

    override fun getLinkedEntitiesPagingSource(
        entityId: String,
        query: String,
    ): PagingSource<Int, InstrumentRoomModel> = when {
        query.isEmpty() -> {
            collectionEntityDao.getInstrumentsByCollection(entityId)
        }

        else -> {
            collectionEntityDao.getInstrumentsByCollectionFiltered(
                collectionId = entityId,
                query = "%$query%"
            )
        }
    }

    override fun transformRoomToListItemModel(roomModel: InstrumentRoomModel): InstrumentListItemModel {
        return roomModel.toInstrumentListItemModel()
    }
}
