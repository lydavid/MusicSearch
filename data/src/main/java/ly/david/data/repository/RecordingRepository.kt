package ly.david.data.repository

import androidx.paging.PagingSource
import javax.inject.Inject
import javax.inject.Singleton
import ly.david.data.Recording
import ly.david.data.domain.RecordingUiModel
import ly.david.data.domain.toRecordingUiModel
import ly.david.data.network.MusicBrainzResource
import ly.david.data.network.api.MusicBrainzApiService
import ly.david.data.persistence.area.ReleasesCountriesDao
import ly.david.data.persistence.history.LookupHistory
import ly.david.data.persistence.history.LookupHistoryDao
import ly.david.data.persistence.recording.RecordingDao
import ly.david.data.persistence.recording.toRecordingRoomModel
import ly.david.data.persistence.relation.BrowseResourceOffset
import ly.david.data.persistence.relation.RelationDao
import ly.david.data.persistence.relation.RelationRoomModel
import ly.david.data.persistence.relation.toRelationRoomModel
import ly.david.data.persistence.release.ReleaseDao
import ly.david.data.persistence.release.ReleaseWithReleaseCountries
import ly.david.data.persistence.release.toReleaseRoomModel

@Singleton
class RecordingRepository @Inject constructor(
    private val musicBrainzApiService: MusicBrainzApiService,
    private val recordingDao: RecordingDao,
    private val relationDao: RelationDao,
    private val releaseDao: ReleaseDao,
    private val lookupHistoryDao: LookupHistoryDao,
    private val releasesCountriesDao: ReleasesCountriesDao
): ReleasesListRepository {
    private var recording: RecordingUiModel? = null

    suspend fun lookupRecording(recordingId: String): RecordingUiModel =
        recording ?: run {

            // Use cached model.
            // TODO: if we haven't stored all relations, then make call again? Only for development
            val recordingRoomModel = recordingDao.getRecording(recordingId)
            if (recordingRoomModel != null) {
                incrementOrInsertLookupHistory(recordingRoomModel)
                return recordingRoomModel.toRecordingUiModel()
            }

            val musicBrainzRecording = musicBrainzApiService.lookupRecording(recordingId)
            recordingDao.insert(musicBrainzRecording.toRecordingRoomModel())

            val recordingRelations = mutableListOf<RelationRoomModel>()
            musicBrainzRecording.relations?.forEachIndexed { index, relationMusicBrainzModel ->
                relationMusicBrainzModel.toRelationRoomModel(
                    resourceId = recordingId,
                    order = index
                )?.let { relationRoomModel ->
                    recordingRelations.add(relationRoomModel)
                }
            }
            relationDao.insertAll(recordingRelations)

            incrementOrInsertLookupHistory(musicBrainzRecording)
            musicBrainzRecording.toRecordingUiModel()
        }

    private suspend fun incrementOrInsertLookupHistory(recording: Recording) {
        lookupHistoryDao.incrementOrInsertLookupHistory(
            LookupHistory(
                title = recording.name,
                resource = MusicBrainzResource.RECORDING,
                mbid = recording.id
            )
        )
    }

    override suspend fun browseReleasesAndStore(resourceId: String, nextOffset: Int): Int {
        val response = musicBrainzApiService.browseReleasesByRecording(
            recordingId = resourceId,
            offset = nextOffset
        )

        if (response.offset == 0) {
            relationDao.insertBrowseResource(
                browseResourceRoomModel = BrowseResourceOffset(
                    resourceId = resourceId,
                    browseResource = MusicBrainzResource.RELEASE,
                    localCount = response.releases.size,
                    remoteCount = response.count
                )
            )
        } else {
            relationDao.incrementOffsetForResource(resourceId, MusicBrainzResource.RELEASE, response.releases.size)
        }

        val musicBrainzReleases = response.releases
        releaseDao.insertAll(musicBrainzReleases.map { it.toReleaseRoomModel() })

        // TODO: need linking table between recordings and releases
        //  a recording is linked to a release through a track and medium
        //  but we need to be able to browse the releases of a recording
        //  without knowing its tracks/mediums
        //  This unfortunately means we will need this linking table
        //  where MB doesn't have one here
//        releasesCountriesDao.insertAll(
//            musicBrainzReleases.map { release ->
//                ReleaseCountry(
//                    releaseId = release.id,
//                    countryId = resourceId,
//                    date = release.date
//                )
//            }
//        )

        return musicBrainzReleases.size
    }

    override suspend fun getRemoteReleasesCountByResource(resourceId: String): Int? =
        relationDao.getBrowseResourceOffset(resourceId, MusicBrainzResource.RELEASE)?.remoteCount

    override suspend fun getLocalReleasesCountByResource(resourceId: String) =
        relationDao.getBrowseResourceOffset(resourceId, MusicBrainzResource.RELEASE)?.localCount ?: 0

    override suspend fun deleteReleasesByResource(resourceId: String) {
        releasesCountriesDao.deleteReleasesFromCountry(resourceId)
        relationDao.deleteBrowseResourceOffsetByResource(resourceId, MusicBrainzResource.RELEASE)
    }

    override fun getReleasesPagingSource(resourceId: String, query: String): PagingSource<Int, ReleaseWithReleaseCountries> = when {
        query.isEmpty() -> {
            releasesCountriesDao.getReleasesFromCountry(resourceId)
        }
        else -> {
            releasesCountriesDao.getReleasesFromCountryFiltered(
                areaId = resourceId,
                query = "%$query%"
            )
        }
    }
}
