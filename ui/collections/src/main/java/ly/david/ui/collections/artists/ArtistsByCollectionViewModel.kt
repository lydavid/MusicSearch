package ly.david.ui.collections.artists

import androidx.paging.PagingSource
import ly.david.data.musicbrainz.ArtistMusicBrainzModel
import ly.david.data.musicbrainz.api.BrowseArtistsResponse
import ly.david.data.musicbrainz.api.MusicBrainzApi
import ly.david.musicsearch.data.core.listitem.ArtistListItemModel
import ly.david.musicsearch.data.core.network.MusicBrainzEntity
import ly.david.musicsearch.data.database.dao.ArtistDao
import ly.david.musicsearch.data.database.dao.BrowseEntityCountDao
import ly.david.musicsearch.data.database.dao.CollectionEntityDao
import ly.david.ui.common.artist.ArtistsPagedList
import ly.david.ui.common.paging.BrowseEntitiesByEntityViewModel
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class ArtistsByCollectionViewModel(
    private val musicBrainzApi: MusicBrainzApi,
    private val collectionEntityDao: CollectionEntityDao,
    private val artistDao: ArtistDao,
    private val browseEntityCountDao: BrowseEntityCountDao,
    pagedList: ArtistsPagedList,
) : BrowseEntitiesByEntityViewModel<ArtistListItemModel, ArtistListItemModel, ArtistMusicBrainzModel, BrowseArtistsResponse>(
    byEntity = MusicBrainzEntity.ARTIST,
    browseEntityCountDao = browseEntityCountDao,
    pagedList = pagedList,
) {

    override suspend fun browseEntitiesByEntity(entityId: String, offset: Int): BrowseArtistsResponse {
        return musicBrainzApi.browseArtistsByCollection(
            collectionId = entityId,
            offset = offset
        )
    }

    override suspend fun insertAllLinkingModels(entityId: String, musicBrainzModels: List<ArtistMusicBrainzModel>) {
        artistDao.insertAll(musicBrainzModels)
        collectionEntityDao.insertAll(
            collectionId = entityId,
            entityIds = musicBrainzModels.map { artist -> artist.id },
        )
    }

    override suspend fun deleteLinkedEntitiesByEntity(entityId: String) {
        collectionEntityDao.withTransaction {
            collectionEntityDao.deleteAllFromCollection(entityId)
            browseEntityCountDao.deleteBrowseEntityCountByEntity(entityId, MusicBrainzEntity.ARTIST)
        }
    }

    override fun getLinkedEntitiesPagingSource(
        entityId: String,
        query: String,
    ): PagingSource<Int, ArtistListItemModel> =
        collectionEntityDao.getArtistsByCollection(
            collectionId = entityId,
            query = "%$query%"
        )

    override fun transformDatabaseToListItemModel(databaseModel: ArtistListItemModel): ArtistListItemModel {
        return databaseModel
    }
}
