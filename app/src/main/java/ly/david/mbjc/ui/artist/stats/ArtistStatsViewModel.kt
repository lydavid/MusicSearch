package ly.david.mbjc.ui.artist.stats

import androidx.lifecycle.ViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import ly.david.data.core.network.MusicBrainzEntity
import ly.david.data.domain.browse.GetBrowseEntityCountFlowUseCase
import ly.david.data.domain.relation.GetCountOfEachRelationshipTypeUseCase
import ly.david.data.room.releasegroup.RoomReleaseGroupTypeCount
import ly.david.musicsearch.data.database.dao.ArtistReleaseDao
import ly.david.musicsearch.data.database.dao.ArtistReleaseGroupDao
import ly.david.ui.stats.ReleaseGroupStats
import ly.david.ui.stats.ReleaseStats
import ly.david.ui.stats.Stats
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class ArtistStatsViewModel(
    private val getCountOfEachRelationshipTypeUseCase: GetCountOfEachRelationshipTypeUseCase,
    private val getBrowseEntityCountFlowUseCase: GetBrowseEntityCountFlowUseCase,
    private val artistReleaseGroupDao: ArtistReleaseGroupDao,
    private val artistReleaseDao: ArtistReleaseDao,
) : ViewModel() {

    private fun releaseStats(entityId: String): Flow<ReleaseStats> =
        combine(
            getBrowseEntityCountFlowUseCase(entityId, MusicBrainzEntity.RELEASE),
            artistReleaseDao.getNumberOfReleasesByArtist(entityId),
        ) { browseReleaseCount, localReleases ->
            ReleaseStats(
                totalRemote = browseReleaseCount?.remoteCount,
                totalLocal = localReleases,
            )
        }

    private fun releaseGroupStats(entityId: String): Flow<ReleaseGroupStats> =
        combine(
            getBrowseEntityCountFlowUseCase(entityId, MusicBrainzEntity.RELEASE_GROUP),
            artistReleaseGroupDao.getNumberOfReleaseGroupsByArtist(entityId),
            artistReleaseGroupDao.getCountOfEachAlbumType(entityId)
        ) { browseReleaseGroupCount, localReleaseGroups, releaseGroupTypeCount ->
            ReleaseGroupStats(
                totalRemote = browseReleaseGroupCount?.remoteCount,
                totalLocal = localReleaseGroups,
                releaseGroupTypeCounts = releaseGroupTypeCount.map {
                    RoomReleaseGroupTypeCount(
                        primaryType = it.primaryType,
                        secondaryTypes = it.secondaryTypes,
                        count = it.count,
                    )
                }.toImmutableList(),
            )
        }

    fun getStats(entityId: String): Flow<Stats> =
        combine(
            getCountOfEachRelationshipTypeUseCase(entityId),
            releaseStats(entityId),
            releaseGroupStats(entityId),
        ) { relationTypeCounts, releaseStats, releaseGroupStats ->
            Stats(
                totalRelations = relationTypeCounts.sumOf { it.count },
                relationTypeCounts = relationTypeCounts.toImmutableList(),
                releaseStats = releaseStats,
                releaseGroupStats = releaseGroupStats,
            )
        }
            .distinctUntilChanged()
}
