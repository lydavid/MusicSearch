package ly.david.ui.collections.artists

import androidx.paging.PagingSource
import ly.david.data.core.network.MusicBrainzEntity
import ly.david.data.domain.listitem.ArtistListItemModel
import ly.david.data.domain.listitem.toArtistListItemModel
import ly.david.data.musicbrainz.ArtistMusicBrainzModel
import ly.david.data.musicbrainz.api.BrowseArtistsResponse
import ly.david.data.musicbrainz.api.MusicBrainzApi
import ly.david.data.room.artist.ArtistDao
import ly.david.data.room.artist.ArtistRoomModel
import ly.david.data.room.artist.toArtistRoomModel
import ly.david.data.room.collection.CollectionEntityRoomModel
import ly.david.data.room.collection.RoomCollectionEntityDao
import ly.david.data.room.relation.RoomRelationDao
import ly.david.ui.common.artist.ArtistsPagedList
import ly.david.ui.common.paging.BrowseEntitiesByEntityViewModel
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class ArtistsByCollectionViewModel(
    private val musicBrainzApi: MusicBrainzApi,
    private val collectionEntityDao: RoomCollectionEntityDao,
    private val artistDao: ArtistDao,
    private val relationDao: RoomRelationDao,
    pagedList: ArtistsPagedList,
) : BrowseEntitiesByEntityViewModel<ArtistRoomModel, ArtistListItemModel, ArtistMusicBrainzModel, BrowseArtistsResponse>(
    byEntity = MusicBrainzEntity.ARTIST,
    relationDao = relationDao,
    pagedList = pagedList,
) {

    override suspend fun browseEntitiesByEntity(entityId: String, offset: Int): BrowseArtistsResponse {
        return musicBrainzApi.browseArtistsByCollection(
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
        query: String,
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
