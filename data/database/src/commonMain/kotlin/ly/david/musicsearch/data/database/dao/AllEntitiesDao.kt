package ly.david.musicsearch.data.database.dao

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOne
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.data.database.Database
import ly.david.musicsearch.shared.domain.coroutine.CoroutineDispatchers
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType

class AllEntitiesDao(
    private val coroutineDispatchers: CoroutineDispatchers,
    database: Database,
) {
    private val transactor = database.all_entitiesQueries

    fun getCountsOfAllEntities(): Flow<List<Pair<MusicBrainzEntityType, Long>>> {
        return transactor.getCountsOfAllEntities(mapper = ::mapToMusicBrainzEntityTypeToCount)
            .asFlow()
            .mapToOne(coroutineDispatchers.io)
    }
}

private fun mapToMusicBrainzEntityTypeToCount(
    areaCount: Long,
    artistCount: Long,
    eventCount: Long,
    genreCount: Long,
    instrumentCount: Long,
    labelCount: Long,
    placeCount: Long,
    recordingCount: Long,
    releaseCount: Long,
    releaseGroupCount: Long,
    seriesCount: Long,
    workCount: Long,
): List<Pair<MusicBrainzEntityType, Long>> {
    return listOf(
        MusicBrainzEntityType.AREA to areaCount,
        MusicBrainzEntityType.ARTIST to artistCount,
        MusicBrainzEntityType.EVENT to eventCount,
        MusicBrainzEntityType.GENRE to genreCount,
        MusicBrainzEntityType.INSTRUMENT to instrumentCount,
        MusicBrainzEntityType.LABEL to labelCount,
        MusicBrainzEntityType.PLACE to placeCount,
        MusicBrainzEntityType.RECORDING to recordingCount,
        MusicBrainzEntityType.RELEASE to releaseCount,
        MusicBrainzEntityType.RELEASE_GROUP to releaseGroupCount,
        MusicBrainzEntityType.SERIES to seriesCount,
        MusicBrainzEntityType.WORK to workCount,
    )
}
