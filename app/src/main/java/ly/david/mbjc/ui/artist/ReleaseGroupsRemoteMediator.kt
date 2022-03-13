package ly.david.mbjc.ui.artist

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import java.io.IOException
import kotlinx.coroutines.delay
import ly.david.mbjc.data.network.BROWSE_LIMIT
import ly.david.mbjc.data.network.DELAY_PAGED_API_CALLS_MS
import ly.david.mbjc.data.network.MusicBrainzApiService
import ly.david.mbjc.data.persistence.ArtistDao
import ly.david.mbjc.data.persistence.ReleaseGroupArtistDao
import ly.david.mbjc.data.persistence.ReleaseGroupDao
import ly.david.mbjc.data.persistence.RoomArtist
import ly.david.mbjc.data.persistence.RoomReleaseGroup
import ly.david.mbjc.data.persistence.RoomReleaseGroupArtistCredit
import ly.david.mbjc.data.persistence.toRoomReleaseGroup
import retrofit2.HttpException

@OptIn(ExperimentalPagingApi::class)
class ReleaseGroupsRemoteMediator(
    private val musicBrainzApiService: MusicBrainzApiService,
    private val releaseGroupDao: ReleaseGroupDao,
    private val releaseGroupArtistDao: ReleaseGroupArtistDao,
    private val artistDao: ArtistDao,
    private val artistId: String,
    private val getRoomArtist: suspend () -> RoomArtist?

    // TODO: if query is passed in, do something different?

) : RemoteMediator<Int, RoomReleaseGroup>() {

    override suspend fun initialize(): InitializeAction {
        return if (getRoomArtist()?.releaseGroupsCount == null) {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        } else {
            InitializeAction.SKIP_INITIAL_REFRESH
        }
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, RoomReleaseGroup>
    ): MediatorResult {
        val nextOffset: Int = when (loadType) {
            LoadType.REFRESH -> 0
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> {
                // TODO: see if we can extract all these calls, so that we can use MusicBrainzData, and reuse this for releases
                val numReleaseGroupsInDatabase = releaseGroupDao.getNumberOfReleaseGroupsByArtist(artistId)
                val totalReleaseGroups = getRoomArtist()?.releaseGroupsCount

                // It should not be possible for the number of release groups in the database to exceed the total
                // from Music Brainz's database. But if it does, it could cause an infinite loop here.
                if (numReleaseGroupsInDatabase == totalReleaseGroups) {
                    return MediatorResult.Success(endOfPaginationReached = true)
                }

                delay(DELAY_PAGED_API_CALLS_MS)
                numReleaseGroupsInDatabase
            }
        }

        return try {
            val response = musicBrainzApiService.browseReleaseGroupsByArtist(
                artistId = artistId,
                limit = BROWSE_LIMIT,
                offset = nextOffset
            )

            // Only need to update it the first time we ever browse this artist's release groups.
            if (response.releaseGroupOffset == 0) {
                artistDao.setTotalReleaseGroups(artistId, response.releaseGroupCount)
            }

            val musicBrainzReleaseGroups = response.releaseGroups
            releaseGroupDao.insertAll(musicBrainzReleaseGroups.map { it.toRoomReleaseGroup() })
            releaseGroupArtistDao.insertAll(
                musicBrainzReleaseGroups.flatMap { releaseGroup ->
                    releaseGroup.artistCredits?.mapIndexed { index, artistCredit ->
                        RoomReleaseGroupArtistCredit(
                            releaseGroupId = releaseGroup.id,
                            artistId = artistCredit.artist.id,
                            name = artistCredit.name,
                            joinPhrase = artistCredit.joinPhrase,
                            order = index
                        )
                    }.orEmpty()
                }
            )

            MediatorResult.Success(endOfPaginationReached = musicBrainzReleaseGroups.size < BROWSE_LIMIT)
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }
}
