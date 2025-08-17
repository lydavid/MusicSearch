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

class CollectedStatsDao(
    database: Database,
    private val coroutineDispatchers: CoroutineDispatchers,
) {
    private val transacter = database.collected_statsQueries

    fun observeCollectedCount(
        browseEntity: MusicBrainzEntityType,
        browseMethod: BrowseMethod,
    ): Flow<Int> =
        when (browseMethod) {
            is BrowseMethod.ByEntity -> {
                if (browseMethod.entity == MusicBrainzEntityType.COLLECTION) {
                    transacter.getCountOfCollectedEntitiesByCollection(
                        collectionId = browseMethod.entityId,
                    )
                } else {
                    getCountOfCollectedEntitiesByEntity(browseEntity, browseMethod)
                }
            }

            is BrowseMethod.All -> {
                getCountOfAllCollectedEntities(browseEntity)
            }
        }
            .asFlow()
            .mapToOne(coroutineDispatchers.io)
            .map { it.toInt() }

    private fun getCountOfCollectedEntitiesByEntity(
        browseEntity: MusicBrainzEntityType,
        browseMethod: BrowseMethod.ByEntity,
    ): Query<Long> = when (browseEntity) {
        MusicBrainzEntityType.AREA -> transacter.getCountOfCollectedAreasByEntity(
            entityId = browseMethod.entityId,
        )

        MusicBrainzEntityType.ARTIST -> transacter.getCountOfCollectedArtistsByEntity(
            entityId = browseMethod.entityId,
        )

        MusicBrainzEntityType.EVENT -> transacter.getCountOfCollectedEventsByEntity(
            entityId = browseMethod.entityId,
        )

        MusicBrainzEntityType.LABEL -> transacter.getCountOfCollectedLabelsByEntity(
            entityId = browseMethod.entityId,
        )

        MusicBrainzEntityType.PLACE -> transacter.getCountOfCollectedPlacesByEntity(
            entityId = browseMethod.entityId,
        )

        MusicBrainzEntityType.RECORDING -> transacter.getCountOfCollectedRecordingsByEntity(
            entityId = browseMethod.entityId,
        )

        MusicBrainzEntityType.RELEASE -> transacter.getCountOfCollectedReleasesByEntity(
            entityId = browseMethod.entityId,
        )

        MusicBrainzEntityType.RELEASE_GROUP -> transacter.getCountOfCollectedReleaseGroupsByEntity(
            entityId = browseMethod.entityId,
        )

        MusicBrainzEntityType.WORK -> transacter.getCountOfCollectedWorksByEntity(
            entityId = browseMethod.entityId,
        )

        MusicBrainzEntityType.COLLECTION,
        MusicBrainzEntityType.GENRE,
        MusicBrainzEntityType.INSTRUMENT,
        MusicBrainzEntityType.SERIES,
        MusicBrainzEntityType.URL,
        -> error(getError(browseEntity))
    }

    private fun getCountOfAllCollectedEntities(browseEntity: MusicBrainzEntityType): Query<Long> = when (browseEntity) {
        MusicBrainzEntityType.AREA -> transacter.getCountOfAllCollectedAreas()
        MusicBrainzEntityType.ARTIST -> transacter.getCountOfAllCollectedArtists()
        MusicBrainzEntityType.EVENT -> transacter.getCountOfAllCollectedEvents()
        MusicBrainzEntityType.GENRE -> transacter.getCountOfAllCollectedGenres()
        MusicBrainzEntityType.INSTRUMENT -> transacter.getCountOfAllCollectedInstruments()
        MusicBrainzEntityType.LABEL -> transacter.getCountOfAllCollectedLabels()
        MusicBrainzEntityType.PLACE -> transacter.getCountOfAllCollectedPlaces()
        MusicBrainzEntityType.RECORDING -> transacter.getCountOfAllCollectedRecordings()
        MusicBrainzEntityType.RELEASE -> transacter.getCountOfAllCollectedReleases()
        MusicBrainzEntityType.RELEASE_GROUP -> transacter.getCountOfAllCollectedReleaseGroups()
        MusicBrainzEntityType.SERIES -> transacter.getCountOfAllCollectedSeries()
        MusicBrainzEntityType.WORK -> transacter.getCountOfAllCollectedWorks()
        MusicBrainzEntityType.COLLECTION,
        MusicBrainzEntityType.URL,
        -> error(getError(browseEntity))
    }

    private fun getError(browseEntity: MusicBrainzEntityType): String =
        "Cannot get collected count for unsupported entity: $browseEntity"
}
