package ly.david.musicsearch.data.repository.instrument

import androidx.paging.PagingData
import androidx.paging.PagingSource
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.data.database.dao.AliasDao
import ly.david.musicsearch.data.database.dao.BrowseRemoteMetadataDao
import ly.david.musicsearch.data.database.dao.CollectionEntityDao
import ly.david.musicsearch.data.database.dao.InstrumentDao
import ly.david.musicsearch.data.musicbrainz.api.BrowseApi
import ly.david.musicsearch.data.musicbrainz.api.BrowseInstrumentsResponse
import ly.david.musicsearch.data.musicbrainz.models.core.InstrumentMusicBrainzNetworkModel
import ly.david.musicsearch.data.repository.base.BrowseEntities
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.list.ListFilters
import ly.david.musicsearch.shared.domain.instrument.InstrumentsListRepository
import ly.david.musicsearch.shared.domain.listitem.InstrumentListItemModel
import ly.david.musicsearch.shared.domain.musicbrainz.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType

class InstrumentsListRepositoryImpl(
    private val browseRemoteMetadataDao: BrowseRemoteMetadataDao,
    private val collectionEntityDao: CollectionEntityDao,
    private val instrumentDao: InstrumentDao,
    private val browseApi: BrowseApi,
    aliasDao: AliasDao,
) : InstrumentsListRepository,
    BrowseEntities<InstrumentListItemModel, InstrumentMusicBrainzNetworkModel, BrowseInstrumentsResponse>(
        browseEntity = MusicBrainzEntityType.INSTRUMENT,
        browseRemoteMetadataDao = browseRemoteMetadataDao,
        aliasDao = aliasDao,
    ) {

    override fun observeInstruments(
        browseMethod: BrowseMethod,
        listFilters: ListFilters,
    ): Flow<PagingData<InstrumentListItemModel>> {
        return observeEntities(
            browseMethod = browseMethod,
            listFilters = listFilters,
        )
    }

    override fun getLinkedEntitiesPagingSource(
        browseMethod: BrowseMethod,
        listFilters: ListFilters,
    ): PagingSource<Int, InstrumentListItemModel> {
        return instrumentDao.getInstruments(
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

                else -> error(browseEntitiesNotSupported(entity.type))
            }
        }
    }

    override suspend fun browseEntitiesByEntity(
        entity: MusicBrainzEntity,
        offset: Int,
    ): BrowseInstrumentsResponse {
        return when (entity.type) {
            MusicBrainzEntityType.COLLECTION -> {
                browseApi.browseInstrumentsByCollection(
                    collectionId = entity.id,
                    offset = offset,
                )
            }

            else -> error(browseEntitiesNotSupported(entity.type))
        }
    }

    override fun insertAll(
        entity: MusicBrainzEntity,
        musicBrainzModels: List<InstrumentMusicBrainzNetworkModel>,
    ) {
        instrumentDao.upsertAll(musicBrainzModels)
        when (entity.type) {
            MusicBrainzEntityType.COLLECTION -> {
                collectionEntityDao.addAllToCollection(
                    collectionId = entity.id,
                    entityIds = musicBrainzModels.map { instrument -> instrument.id },
                )
            }

            else -> {
                error(browseEntitiesNotSupported(entity.type))
            }
        }
    }

    override fun getLocalLinkedEntitiesCountByEntity(
        entity: MusicBrainzEntity,
    ): Int {
        return collectionEntityDao.getCountOfEntitiesByCollection(entity.id)
    }
}
