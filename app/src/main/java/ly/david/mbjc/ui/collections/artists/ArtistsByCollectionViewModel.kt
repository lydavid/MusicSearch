package ly.david.mbjc.ui.collections.artists

import androidx.paging.PagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.data.domain.ArtistListItemModel
import ly.david.data.domain.toArtistListItemModel
import ly.david.data.network.ArtistMusicBrainzModel
import ly.david.data.network.MusicBrainzResource
import ly.david.data.network.api.BrowseArtistsResponse
import ly.david.data.network.api.MusicBrainzApiService
import ly.david.data.persistence.artist.ArtistDao
import ly.david.data.persistence.artist.ArtistRoomModel
import ly.david.data.persistence.artist.toArtistRoomModel
import ly.david.data.persistence.collection.CollectionEntityDao
import ly.david.data.persistence.collection.CollectionEntityRoomModel
import ly.david.data.persistence.relation.RelationDao
import ly.david.mbjc.ui.common.paging.BrowseEntitiesByEntityViewModel
import ly.david.mbjc.ui.common.paging.PagedList

@HiltViewModel
internal class ArtistsByCollectionViewModel @Inject constructor(
    private val musicBrainzApiService: MusicBrainzApiService,
    private val collectionEntityDao: CollectionEntityDao,
    private val artistDao: ArtistDao,
    private val relationDao: RelationDao,
    pagedList: PagedList<ArtistRoomModel, ArtistListItemModel>,
) : BrowseEntitiesByEntityViewModel
<ArtistRoomModel, ArtistListItemModel, ArtistMusicBrainzModel, BrowseArtistsResponse>(
    byEntity = MusicBrainzResource.ARTIST,
    relationDao = relationDao,
    pagedList = pagedList
) {

    override suspend fun browseEntitiesByEntity(entityId: String, offset: Int): BrowseArtistsResponse {
        return musicBrainzApiService.browseArtistsByCollection(
            collectionId = entityId,
            offset = offset
        )
    }

    override suspend fun insertAllLinkingModels(entityId: String, musicBrainzModels: List<ArtistMusicBrainzModel>) {
        artistDao.insertAll(musicBrainzModels.map { it.toArtistRoomModel() })
        collectionEntityDao.insertAll(
            musicBrainzModels.map { artist ->
                CollectionEntityRoomModel(
                    id = entityId,
                    entityId = artist.id
                )
            }
        )
    }

    override suspend fun deleteLinkedResourcesByResource(resourceId: String) {
        collectionEntityDao.withTransaction {
            collectionEntityDao.deleteCollectionEntityLinks(resourceId)
            relationDao.deleteBrowseResourceCountByResource(resourceId, MusicBrainzResource.ARTIST)
        }
    }

    override fun getLinkedResourcesPagingSource(
        resourceId: String,
        query: String
    ): PagingSource<Int, ArtistRoomModel> = when {
        query.isEmpty() -> {
            collectionEntityDao.getArtistsByCollection(resourceId)
        }
        else -> {
            collectionEntityDao.getArtistsByCollectionFiltered(
                collectionId = resourceId,
                query = "%$query%"
            )
        }
    }

    override fun transformRoomToListItemModel(roomModel: ArtistRoomModel): ArtistListItemModel {
        return roomModel.toArtistListItemModel()
    }
}
