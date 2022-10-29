package ly.david.data.repository

import androidx.paging.PagingSource
import javax.inject.Inject
import javax.inject.Singleton
import ly.david.data.domain.LabelUiModel
import ly.david.data.domain.toLabelUiModel
import ly.david.data.network.MusicBrainzResource
import ly.david.data.network.api.MusicBrainzApiService
import ly.david.data.persistence.label.LabelDao
import ly.david.data.persistence.label.ReleaseLabel
import ly.david.data.persistence.label.ReleasesLabelsDao
import ly.david.data.persistence.label.toLabelRoomModel
import ly.david.data.persistence.relation.BrowseResourceOffset
import ly.david.data.persistence.relation.RelationDao
import ly.david.data.persistence.release.ReleaseDao
import ly.david.data.persistence.release.ReleaseRoomModel
import ly.david.data.persistence.release.toReleaseRoomModel

@Singleton
class LabelRepository @Inject constructor(
    private val musicBrainzApiService: MusicBrainzApiService,
    private val labelDao: LabelDao,
    private val releasesLabelsDao: ReleasesLabelsDao,
    private val releaseDao: ReleaseDao,
    private val relationDao: RelationDao,
) {
    suspend fun lookupLabel(labelId: String): LabelUiModel {
        val roomLabel = labelDao.getLabel(labelId)
        if (roomLabel != null) {
            return roomLabel.toLabelUiModel()
        }

        val musicBrainzLabel = musicBrainzApiService.lookupLabel(labelId)

        labelDao.insert(musicBrainzLabel.toLabelRoomModel())

        return musicBrainzLabel.toLabelUiModel()
    }

    suspend fun browseReleasesAndStore(labelId: String, nextOffset: Int): Int {
        val response = musicBrainzApiService.browseReleasesByLabel(
            labelId = labelId,
            offset = nextOffset
        )

        if (response.offset == 0) {
            relationDao.insertBrowseResource(
                browseResourceRoomModel = BrowseResourceOffset(
                    resourceId = labelId,
                    browseResource = MusicBrainzResource.RELEASE,
                    localCount = response.releases.size,
                    remoteCount = response.count
                )
            )
        } else {
            relationDao.incrementOffsetForResource(labelId, MusicBrainzResource.RELEASE, response.releases.size)
        }

        val releaseMusicBrainzModels = response.releases
        releaseDao.insertAll(releaseMusicBrainzModels.map { it.toReleaseRoomModel() })
        releasesLabelsDao.insertAll(
            releaseMusicBrainzModels.map { release ->
                ReleaseLabel(release.id, labelId)
            }
        )

        return releaseMusicBrainzModels.size
    }

    // Only difference between this and the stats one is this can return null
    suspend fun getRemoteReleasesByLabelCount(labelId: String) =
        relationDao.getBrowseResourceOffset(labelId, MusicBrainzResource.RELEASE)?.remoteCount

    suspend fun getLocalReleasesByLabelCount(labelId: String) =
        relationDao.getBrowseResourceOffset(labelId, MusicBrainzResource.RELEASE)?.localCount ?: 0

    suspend fun deleteReleasesByLabel(labelId: String) {
        releasesLabelsDao.deleteReleasesByLabel(labelId)
        relationDao.deleteBrowseResourceOffsetByResource(labelId, MusicBrainzResource.RELEASE)
    }

    fun getReleasesPagingSource(labelId: String, query: String): PagingSource<Int, ReleaseRoomModel> = when {
        query.isEmpty() -> {
            releasesLabelsDao.getReleasesByLabel(labelId)
        }
        else -> {
            releasesLabelsDao.getReleasesByLabelFiltered(
                labelId = labelId,
                query = "%$query%"
            )
        }
    }
}
