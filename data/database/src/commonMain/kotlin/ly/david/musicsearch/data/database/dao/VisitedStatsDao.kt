package ly.david.musicsearch.data.database.dao

import app.cash.sqldelight.Query
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOne
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ly.david.musicsearch.shared.domain.coroutine.CoroutineDispatchers
import ly.david.musicsearch.data.database.Database
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType

class VisitedStatsDao(
    database: Database,
    private val coroutineDispatchers: CoroutineDispatchers,
) {
    private val transacter = database.visited_statsQueries

    fun observeVisitedCount(
        browseEntity: MusicBrainzEntityType,
        browseMethod: BrowseMethod,
    ): Flow<Int> =
        when (browseMethod) {
            is BrowseMethod.ByEntity -> {
                if (browseMethod.entity == MusicBrainzEntityType.COLLECTION) {
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
        browseEntity: MusicBrainzEntityType,
        browseMethod: BrowseMethod.ByEntity,
    ): Query<Long> = when (browseEntity) {
        MusicBrainzEntityType.AREA -> transacter.getCountOfVisitedAreasByEntity(
            entityId = browseMethod.entityId,
        )

        MusicBrainzEntityType.ARTIST -> transacter.getCountOfVisitedArtistsByEntity(
            entityId = browseMethod.entityId,
        )

        MusicBrainzEntityType.EVENT -> transacter.getCountOfVisitedEventsByEntity(
            entityId = browseMethod.entityId,
        )

        MusicBrainzEntityType.LABEL -> transacter.getCountOfVisitedLabelsByEntity(
            entityId = browseMethod.entityId,
        )

        MusicBrainzEntityType.PLACE -> transacter.getCountOfVisitedPlacesByEntity(
            entityId = browseMethod.entityId,
        )

        MusicBrainzEntityType.RECORDING -> transacter.getCountOfVisitedRecordingsByEntity(
            entityId = browseMethod.entityId,
        )

        MusicBrainzEntityType.RELEASE -> transacter.getCountOfVisitedReleasesByEntity(
            entityId = browseMethod.entityId,
        )

        MusicBrainzEntityType.RELEASE_GROUP -> transacter.getCountOfVisitedReleaseGroupsByEntity(
            entityId = browseMethod.entityId,
        )

        MusicBrainzEntityType.WORK -> transacter.getCountOfVisitedWorksByEntity(
            entityId = browseMethod.entityId,
        )

        MusicBrainzEntityType.COLLECTION,
        MusicBrainzEntityType.GENRE,
        MusicBrainzEntityType.INSTRUMENT,
        MusicBrainzEntityType.SERIES,
        MusicBrainzEntityType.URL,
        -> error(getError(browseEntity))
    }

    private fun getCountOfAllVisitedEntities(browseEntity: MusicBrainzEntityType): Query<Long> = when (browseEntity) {
        MusicBrainzEntityType.AREA -> transacter.getCountOfAllVisitedAreas()
        MusicBrainzEntityType.ARTIST -> transacter.getCountOfAllVisitedArtists()
        MusicBrainzEntityType.EVENT -> transacter.getCountOfAllVisitedEvents()
        MusicBrainzEntityType.GENRE -> transacter.getCountOfAllVisitedGenres()
        MusicBrainzEntityType.INSTRUMENT -> transacter.getCountOfAllVisitedInstruments()
        MusicBrainzEntityType.LABEL -> transacter.getCountOfAllVisitedLabels()
        MusicBrainzEntityType.PLACE -> transacter.getCountOfAllVisitedPlaces()
        MusicBrainzEntityType.RECORDING -> transacter.getCountOfAllVisitedRecordings()
        MusicBrainzEntityType.RELEASE -> transacter.getCountOfAllVisitedReleases()
        MusicBrainzEntityType.RELEASE_GROUP -> transacter.getCountOfAllVisitedReleaseGroups()
        MusicBrainzEntityType.SERIES -> transacter.getCountOfAllVisitedSeries()
        MusicBrainzEntityType.WORK -> transacter.getCountOfAllVisitedWorks()
        MusicBrainzEntityType.COLLECTION,
        MusicBrainzEntityType.URL,
        -> error(getError(browseEntity))
    }

    private fun getError(browseEntity: MusicBrainzEntityType): String =
        "Cannot get visited count for unsupported entity: $browseEntity"
}
