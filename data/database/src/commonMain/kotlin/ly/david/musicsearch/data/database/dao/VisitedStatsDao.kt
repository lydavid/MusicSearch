package ly.david.musicsearch.data.database.dao

import app.cash.sqldelight.Query
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOne
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ly.david.musicsearch.shared.domain.coroutine.CoroutineDispatchers
import ly.david.musicsearch.data.database.Database
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity

class VisitedStatsDao(
    database: Database,
    private val coroutineDispatchers: CoroutineDispatchers,
) {
    private val transacter = database.visited_statsQueries

    fun observeVisitedCount(
        browseEntity: MusicBrainzEntity,
        browseMethod: BrowseMethod,
    ): Flow<Int> =
        when (browseMethod) {
            is BrowseMethod.ByEntity -> {
                if (browseMethod.entity == MusicBrainzEntity.COLLECTION) {
                    transacter.getCountOfVisitedEntitiesByCollection(
                        collectionId = browseMethod.entityId,
                    )
                } else {
                    getCountOfVisitedEntitiesByEntity(browseEntity, browseMethod)
                }
            }

            is BrowseMethod.All -> {
                getCountOfAllVisitedEntities(browseEntity)
            }
        }
            .asFlow()
            .mapToOne(coroutineDispatchers.io)
            .map { it.toInt() }

    private fun getCountOfVisitedEntitiesByEntity(
        browseEntity: MusicBrainzEntity,
        browseMethod: BrowseMethod.ByEntity,
    ): Query<Long> = when (browseEntity) {
        MusicBrainzEntity.AREA -> transacter.getCountOfVisitedAreasByEntity(
            entityId = browseMethod.entityId,
        )

        MusicBrainzEntity.ARTIST -> transacter.getCountOfVisitedArtistsByEntity(
            entityId = browseMethod.entityId,
        )

        MusicBrainzEntity.EVENT -> transacter.getCountOfVisitedEventsByEntity(
            entityId = browseMethod.entityId,
        )

        MusicBrainzEntity.LABEL -> transacter.getCountOfVisitedLabelsByEntity(
            entityId = browseMethod.entityId,
        )

        MusicBrainzEntity.PLACE -> transacter.getCountOfVisitedPlacesByEntity(
            entityId = browseMethod.entityId,
        )

        MusicBrainzEntity.RECORDING -> transacter.getCountOfVisitedRecordingsByEntity(
            entityId = browseMethod.entityId,
        )

        MusicBrainzEntity.RELEASE -> transacter.getCountOfVisitedReleasesByEntity(
            entityId = browseMethod.entityId,
        )

        MusicBrainzEntity.RELEASE_GROUP -> transacter.getCountOfVisitedReleaseGroupsByEntity(
            entityId = browseMethod.entityId,
        )

        MusicBrainzEntity.WORK -> transacter.getCountOfVisitedWorksByEntity(
            entityId = browseMethod.entityId,
        )

        MusicBrainzEntity.COLLECTION,
        MusicBrainzEntity.GENRE,
        MusicBrainzEntity.INSTRUMENT,
        MusicBrainzEntity.SERIES,
        MusicBrainzEntity.URL,
        -> error(getError(browseEntity))
    }

    private fun getCountOfAllVisitedEntities(browseEntity: MusicBrainzEntity): Query<Long> = when (browseEntity) {
        MusicBrainzEntity.AREA -> transacter.getCountOfAllVisitedAreas()
        MusicBrainzEntity.ARTIST -> transacter.getCountOfAllVisitedArtists()
        MusicBrainzEntity.EVENT -> transacter.getCountOfAllVisitedEvents()
        MusicBrainzEntity.GENRE -> transacter.getCountOfAllVisitedGenres()
        MusicBrainzEntity.INSTRUMENT -> transacter.getCountOfAllVisitedInstruments()
        MusicBrainzEntity.LABEL -> transacter.getCountOfAllVisitedLabels()
        MusicBrainzEntity.PLACE -> transacter.getCountOfAllVisitedPlaces()
        MusicBrainzEntity.RECORDING -> transacter.getCountOfAllVisitedRecordings()
        MusicBrainzEntity.RELEASE -> transacter.getCountOfAllVisitedReleases()
        MusicBrainzEntity.RELEASE_GROUP -> transacter.getCountOfAllVisitedReleaseGroups()
        MusicBrainzEntity.SERIES -> transacter.getCountOfAllVisitedSeries()
        MusicBrainzEntity.WORK -> transacter.getCountOfAllVisitedWorks()
        MusicBrainzEntity.COLLECTION,
        MusicBrainzEntity.URL,
        -> error(getError(browseEntity))
    }

    private fun getError(browseEntity: MusicBrainzEntity): String =
        "Cannot get visited count for unsupported entity: $browseEntity"
}
