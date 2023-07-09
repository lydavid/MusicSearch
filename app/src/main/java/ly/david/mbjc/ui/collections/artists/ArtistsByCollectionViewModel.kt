package ly.david.mbjc.ui.collections.artists

import androidx.paging.PagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.data.musicbrainz.MusicBrainzAuthState
import ly.david.data.musicbrainz.getBearerToken
import ly.david.data.domain.listitem.ArtistListItemModel
import ly.david.data.domain.listitem.toArtistListItemModel
import ly.david.data.network.ArtistMusicBrainzModel
import ly.david.data.network.MusicBrainzEntity
import ly.david.data.network.api.BrowseArtistsResponse
import ly.david.data.network.api.MusicBrainzApiService
import ly.david.data.room.artist.ArtistDao
import ly.david.data.room.artist.ArtistRoomModel
import ly.david.data.room.artist.toArtistRoomModel
import ly.david.data.room.collection.CollectionEntityDao
import ly.david.data.room.collection.CollectionEntityRoomModel
import ly.david.data.room.relation.RelationDao
import ly.david.ui.common.paging.BrowseEntitiesByEntityViewModel
import ly.david.ui.common.paging.PagedList

@HiltViewModel
internal class ArtistsByCollectionViewModel @Inject constructor(
    private val musicBrainzApiService: MusicBrainzApiService,
    private val collectionEntityDao: CollectionEntityDao,
    private val artistDao: ArtistDao,
    private val relationDao: RelationDao,
    pagedList: PagedList<ArtistRoomModel, ArtistListItemModel>,
    private val musicBrainzAuthState: MusicBrainzAuthState,
) : BrowseEntitiesByEntityViewModel<ArtistRoomModel, ArtistListItemModel, ArtistMusicBrainzModel, BrowseArtistsResponse>(
    byEntity = MusicBrainzEntity.ARTIST,
    relationDao = relationDao,
    pagedList = pagedList
) {

    override suspend fun browseEntitiesByEntity(entityId: String, offset: Int): BrowseArtistsResponse {
        return musicBrainzApiService.browseArtistsByCollection(
            bearerToken = musicBrainzAuthState.getBearerToken(),
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

    override suspend fun deleteLinkedEntitiesByEntity(entityId: String) {
        collectionEntityDao.withTransaction {
            collectionEntityDao.deleteAllFromCollection(entityId)
            relationDao.deleteBrowseEntityCountByEntity(entityId, MusicBrainzEntity.ARTIST)
        }
    }

    override fun getLinkedEntitiesPagingSource(
        entityId: String,
        query: String
    ): PagingSource<Int, ArtistRoomModel> = when {
        query.isEmpty() -> {
            collectionEntityDao.getArtistsByCollection(entityId)
        }

        else -> {
            collectionEntityDao.getArtistsByCollectionFiltered(
                collectionId = entityId,
                query = "%$query%"
            )
        }
    }

    override fun transformRoomToListItemModel(roomModel: ArtistRoomModel): ArtistListItemModel {
        return roomModel.toArtistListItemModel()
    }
}
