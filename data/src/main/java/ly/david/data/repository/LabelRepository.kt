package ly.david.data.repository

import androidx.paging.PagingSource
import javax.inject.Inject
import javax.inject.Singleton
import ly.david.data.domain.LabelUiModel
import ly.david.data.domain.toLabelUiModel
import ly.david.data.network.api.MusicBrainzApiService
import ly.david.data.persistence.label.LabelDao
import ly.david.data.persistence.label.ReleaseLabel
import ly.david.data.persistence.label.ReleasesLabelsDao
import ly.david.data.persistence.label.toLabelRoomModel
import ly.david.data.persistence.release.ReleaseDao
import ly.david.data.persistence.release.ReleaseRoomModel
import ly.david.data.persistence.release.toReleaseRoomModel

@Singleton
class LabelRepository @Inject constructor(
    private val musicBrainzApiService: MusicBrainzApiService,
    private val labelDao: LabelDao,
    private val releasesLabelsDao: ReleasesLabelsDao,
    private val releaseDao: ReleaseDao,
) {
    private var label: LabelUiModel? = null

    suspend fun lookupLabel(labelId: String): LabelUiModel =
        label ?: run {

            val roomLabel = labelDao.getLabel(labelId)
            if (roomLabel != null) {
                return roomLabel.toLabelUiModel()
            }

            val musicBrainzLabel = musicBrainzApiService.lookupLabel(labelId)

            labelDao.insert(musicBrainzLabel.toLabelRoomModel())

            musicBrainzLabel.toLabelUiModel()
        }

    suspend fun browseReleasesAndStore(labelId: String, nextOffset: Int): Int {
        val response = musicBrainzApiService.browseReleasesByLabel(
            labelId = labelId,
            offset = nextOffset
        )

        if (response.offset == 0) {
            labelDao.setReleaseCount(labelId, response.count)
        }

        val musicBrainzReleases = response.releases
        releaseDao.insertAll(musicBrainzReleases.map { it.toReleaseRoomModel() })
        releasesLabelsDao.insertAll(
            musicBrainzReleases.map { release ->
                ReleaseLabel(release.id, labelId)
            }
        )

        return musicBrainzReleases.size
    }

    // Only difference between this and the stats one is this can return null
    suspend fun getTotalReleases(labelId: String) =
        labelDao.getLabel(labelId)?.releaseCount

    suspend fun getNumberOfReleasesByLabel(labelId: String) =
        releasesLabelsDao.getNumberOfReleasesByLabel(labelId)

    suspend fun deleteReleasesByLabel(labelId: String) =
        releasesLabelsDao.deleteReleasesByLabel(labelId)

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
