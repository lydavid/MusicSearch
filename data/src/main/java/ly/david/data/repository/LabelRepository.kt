package ly.david.data.repository

import androidx.paging.PagingSource
import javax.inject.Inject
import javax.inject.Singleton
import ly.david.data.domain.LabelCardModel
import ly.david.data.domain.toLabelUiModel
import ly.david.data.network.MusicBrainzResource
import ly.david.data.network.RelationMusicBrainzModel
import ly.david.data.network.api.LookupApi
import ly.david.data.network.api.MusicBrainzApiService
import ly.david.data.network.toReleaseLabels
import ly.david.data.persistence.label.LabelDao
import ly.david.data.persistence.label.ReleasesLabelsDao
import ly.david.data.persistence.label.toLabelRoomModel
import ly.david.data.persistence.relation.BrowseResourceCount
import ly.david.data.persistence.relation.RelationDao
import ly.david.data.persistence.release.ReleaseDao
import ly.david.data.persistence.release.ReleaseWithReleaseCountries
import ly.david.data.persistence.release.toReleaseRoomModel

@Singleton
class LabelRepository @Inject constructor(
    private val musicBrainzApiService: MusicBrainzApiService,
    private val labelDao: LabelDao,
    private val releasesLabelsDao: ReleasesLabelsDao,
    private val releaseDao: ReleaseDao,
    private val relationDao: RelationDao,
) : ReleasesListRepository, RelationsListRepository {

    suspend fun lookupLabel(labelId: String): LabelCardModel {
        val labelRoomModel = labelDao.getLabel(labelId)
        if (labelRoomModel != null) {
            return labelRoomModel.toLabelUiModel()
        }

        val labelMusicBrainzModel = musicBrainzApiService.lookupLabel(labelId)

        labelDao.insert(labelMusicBrainzModel.toLabelRoomModel())

        return labelMusicBrainzModel.toLabelUiModel()
    }

    override suspend fun lookupRelationsFromNetwork(resourceId: String): List<RelationMusicBrainzModel>? {
        return musicBrainzApiService.lookupLabel(
            labelId = resourceId,
            include = LookupApi.INC_ALL_RELATIONS
        ).relations
    }

    override suspend fun browseReleasesAndStore(resourceId: String, nextOffset: Int): Int {
        val response = musicBrainzApiService.browseReleasesByLabel(
            labelId = resourceId,
            offset = nextOffset
        )

        if (response.offset == 0) {
            relationDao.insertBrowseResourceCount(
                browseResourceCount = BrowseResourceCount(
                    resourceId = resourceId,
                    browseResource = MusicBrainzResource.RELEASE,
                    localCount = response.releases.size,
                    remoteCount = response.count
                )
            )
        } else {
            relationDao.incrementLocalCountForResource(resourceId, MusicBrainzResource.RELEASE, response.releases.size)
        }

        val releaseMusicBrainzModels = response.releases
        releaseDao.insertAll(releaseMusicBrainzModels.map { it.toReleaseRoomModel() })
        releasesLabelsDao.insertAll(
            releaseMusicBrainzModels.flatMap { release ->
                release.labelInfoList?.toReleaseLabels(releaseId = release.id, labelId = resourceId).orEmpty()
            }
        )

        return releaseMusicBrainzModels.size
    }

    // Only difference between this and the stats one is this can return null
    override suspend fun getRemoteReleasesCountByResource(resourceId: String) =
        relationDao.getBrowseResourceCount(resourceId, MusicBrainzResource.RELEASE)?.remoteCount

    override suspend fun getLocalReleasesCountByResource(resourceId: String) =
        relationDao.getBrowseResourceCount(resourceId, MusicBrainzResource.RELEASE)?.localCount ?: 0

    override suspend fun deleteReleasesByResource(resourceId: String) {
        releasesLabelsDao.deleteReleasesByLabel(resourceId)
        relationDao.deleteBrowseResourceCountByResource(resourceId, MusicBrainzResource.RELEASE)
    }

    override fun getReleasesPagingSource(
        resourceId: String,
        query: String
    ): PagingSource<Int, ReleaseWithReleaseCountries> = when {
        query.isEmpty() -> {
            releasesLabelsDao.getReleasesByLabel(resourceId)
        }
        else -> {
            releasesLabelsDao.getReleasesByLabelFiltered(
                labelId = resourceId,
                query = "%$query%"
            )
        }
    }
}
