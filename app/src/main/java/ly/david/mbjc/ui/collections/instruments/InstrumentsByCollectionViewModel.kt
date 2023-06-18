package ly.david.mbjc.ui.collections.instruments

import androidx.paging.PagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.data.musicbrainz.MusicBrainzAuthState
import ly.david.data.musicbrainz.getBearerToken
import ly.david.data.domain.listitem.InstrumentListItemModel
import ly.david.data.domain.listitem.toInstrumentListItemModel
import ly.david.data.network.InstrumentMusicBrainzModel
import ly.david.data.network.MusicBrainzResource
import ly.david.data.network.api.BrowseInstrumentsResponse
import ly.david.data.network.api.MusicBrainzApiService
import ly.david.data.room.collection.CollectionEntityDao
import ly.david.data.room.collection.CollectionEntityRoomModel
import ly.david.data.room.instrument.InstrumentDao
import ly.david.data.room.instrument.InstrumentRoomModel
import ly.david.data.room.instrument.toInstrumentRoomModel
import ly.david.data.room.relation.RelationDao
import ly.david.ui.common.paging.BrowseEntitiesByEntityViewModel
import ly.david.ui.common.paging.PagedList

@HiltViewModel
internal class InstrumentsByCollectionViewModel @Inject constructor(
    private val musicBrainzApiService: MusicBrainzApiService,
    private val collectionEntityDao: CollectionEntityDao,
    private val instrumentDao: InstrumentDao,
    private val relationDao: RelationDao,
    pagedList: PagedList<InstrumentRoomModel, InstrumentListItemModel>,
    private val musicBrainzAuthState: MusicBrainzAuthState,
) : BrowseEntitiesByEntityViewModel<InstrumentRoomModel, InstrumentListItemModel, InstrumentMusicBrainzModel, BrowseInstrumentsResponse>(
    byEntity = MusicBrainzResource.INSTRUMENT,
    relationDao = relationDao,
    pagedList = pagedList
) {

    override suspend fun browseEntitiesByEntity(entityId: String, offset: Int): BrowseInstrumentsResponse {
        return musicBrainzApiService.browseInstrumentsByCollection(
            bearerToken = musicBrainzAuthState.getBearerToken(),
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

    override suspend fun deleteLinkedResourcesByResource(resourceId: String) {
        collectionEntityDao.withTransaction {
            collectionEntityDao.deleteAllFromCollection(resourceId)
            relationDao.deleteBrowseResourceCountByResource(resourceId, MusicBrainzResource.AREA)
        }
    }

    override fun getLinkedResourcesPagingSource(
        resourceId: String,
        query: String
    ): PagingSource<Int, InstrumentRoomModel> = when {
        query.isEmpty() -> {
            collectionEntityDao.getInstrumentsByCollection(resourceId)
        }

        else -> {
            collectionEntityDao.getInstrumentsByCollectionFiltered(
                collectionId = resourceId,
                query = "%$query%"
            )
        }
    }

    override fun transformRoomToListItemModel(roomModel: InstrumentRoomModel): InstrumentListItemModel {
        return roomModel.toInstrumentListItemModel()
    }
}
