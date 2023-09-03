package ly.david.mbjc.ui.artist.releases

import androidx.paging.PagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.data.domain.listitem.ReleaseListItemModel
import ly.david.data.core.network.MusicBrainzEntity
import ly.david.data.musicbrainz.ReleaseMusicBrainzModel
import ly.david.data.musicbrainz.api.BrowseReleasesResponse
import ly.david.data.musicbrainz.api.MusicBrainzApi
import ly.david.data.room.artist.releases.ArtistRelease
import ly.david.data.room.artist.releases.ArtistReleaseDao
import ly.david.data.room.relation.RelationDao
import ly.david.data.room.release.ReleaseDao
import ly.david.data.room.release.ReleaseForListItem
import ly.david.ui.common.paging.PagedList
import ly.david.ui.common.release.ReleasesByEntityViewModel

@HiltViewModel
internal class ReleasesByArtistViewModel @Inject constructor(
    private val musicBrainzApi: MusicBrainzApi,
    private val artistReleaseDao: ArtistReleaseDao,
    private val relationDao: RelationDao,
    releaseDao: ReleaseDao,
    pagedList: PagedList<ReleaseForListItem, ReleaseListItemModel>,
) : ReleasesByEntityViewModel(
    relationDao = relationDao,
    releaseDao = releaseDao,
    pagedList = pagedList,
) {

    override suspend fun browseReleasesByEntity(entityId: String, offset: Int): BrowseReleasesResponse {
        return musicBrainzApi.browseReleasesByArtist(
            artistId = entityId,
            offset = offset
        )
    }

    override suspend fun insertAllLinkingModels(
        entityId: String,
        releaseMusicBrainzModels: List<ReleaseMusicBrainzModel>,
    ) {
        artistReleaseDao.insertAll(
            releaseMusicBrainzModels.map { release ->
                ArtistRelease(
                    artistId = entityId,
                    releaseId = release.id
                )
            }
        )
    }

    override suspend fun deleteLinkedEntitiesByEntity(entityId: String) {
        artistReleaseDao.withTransaction {
            artistReleaseDao.deleteReleasesByArtist(entityId)
            artistReleaseDao.deleteArtistReleaseLinks(entityId)
            relationDao.deleteBrowseEntityCountByEntity(entityId, MusicBrainzEntity.RELEASE)
        }
    }

    override fun getLinkedEntitiesPagingSource(
        entityId: String,
        query: String,
    ): PagingSource<Int, ReleaseForListItem> = when {
        query.isEmpty() -> {
            artistReleaseDao.getReleasesByArtist(entityId)
        }
        else -> {
            artistReleaseDao.getReleasesByArtistFiltered(
                artistId = entityId,
                query = "%$query%"
            )
        }
    }

    // TODO: ideal for selecting labels. though where would those labels be shown?
    override fun postFilter(listItemModel: ReleaseListItemModel): Boolean {
        return true
    }
}
