package ly.david.mbjc.ui.collections.releasegroups

import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.data.auth.MusicBrainzAuthState
import ly.david.data.auth.getBearerToken
import ly.david.data.network.MusicBrainzResource
import ly.david.data.network.ReleaseGroupMusicBrainzModel
import ly.david.data.network.api.BrowseReleaseGroupsResponse
import ly.david.data.network.api.MusicBrainzApiService
import ly.david.data.persistence.collection.CollectionEntityDao
import ly.david.data.persistence.collection.CollectionEntityRoomModel
import ly.david.data.persistence.relation.RelationDao
import ly.david.data.persistence.releasegroup.ReleaseGroupDao
import ly.david.ui.common.releasegroup.ReleaseGroupsByEntityViewModel
import ly.david.ui.common.releasegroup.ReleaseGroupsPagedList

@HiltViewModel
internal class ReleaseGroupsByCollectionViewModel @Inject constructor(
    private val musicBrainzApiService: MusicBrainzApiService,
    private val collectionEntityDao: CollectionEntityDao,
    private val relationDao: RelationDao,
    releaseGroupDao: ReleaseGroupDao,
    releaseGroupsPagedList: ReleaseGroupsPagedList,
    private val musicBrainzAuthState: MusicBrainzAuthState,
) : ReleaseGroupsByEntityViewModel(
    relationDao = relationDao,
    releaseGroupDao = releaseGroupDao,
    releaseGroupsPagedList = releaseGroupsPagedList
) {

    override suspend fun browseReleaseGroupsByEntity(entityId: String, offset: Int): BrowseReleaseGroupsResponse {
        return musicBrainzApiService.browseReleaseGroupsByCollection(
            bearerToken = musicBrainzAuthState.getBearerToken(),
            collectionId = entityId,
            offset = offset
        )
    }

    override suspend fun insertAllLinkingModels(
        entityId: String,
        releaseGroupMusicBrainzModels: List<ReleaseGroupMusicBrainzModel>
    ) {
        collectionEntityDao.insertAll(
            releaseGroupMusicBrainzModels.map { releaseGroup ->
                CollectionEntityRoomModel(
                    id = entityId,
                    entityId = releaseGroup.id
                )
            }
        )
    }

    override suspend fun deleteLinkedResourcesByResource(resourceId: String) {
        collectionEntityDao.withTransaction {
            collectionEntityDao.deleteAllFromCollection(resourceId)
            relationDao.deleteBrowseResourceCountByResource(resourceId, MusicBrainzResource.RELEASE_GROUP)
        }
    }

    override fun getLinkedResourcesPagingSource(resourceId: String, query: String, sorted: Boolean) =
        collectionEntityDao.getReleaseGroupsByCollection(
            collectionId = resourceId,
            query = "%$query%",
            sorted = sorted
        )
}
