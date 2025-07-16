package ly.david.musicsearch.data.database.dao

import app.cash.sqldelight.Query
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOne
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ly.david.musicsearch.core.coroutines.CoroutineDispatchers
import ly.david.musicsearch.data.database.Database
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity

class CollectedStatsDao(
    database: Database,
    private val coroutineDispatchers: CoroutineDispatchers,
) {
    private val transacter = database.collected_statsQueries

    fun observeCollectedCount(
        browseEntity: MusicBrainzEntity,
        browseMethod: BrowseMethod,
    ): Flow<Int> =
        when (browseMethod) {
            is BrowseMethod.ByEntity -> {
                if (browseMethod.entity == MusicBrainzEntity.COLLECTION) {
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
        browseEntity: MusicBrainzEntity,
        browseMethod: BrowseMethod.ByEntity,
    ): Query<Long> = when (browseEntity) {
        MusicBrainzEntity.AREA -> transacter.getCountOfCollectedAreasByEntity(
            entityId = browseMethod.entityId,
        )

        MusicBrainzEntity.ARTIST -> transacter.getCountOfCollectedArtistsByEntity(
            entityId = browseMethod.entityId,
        )

        MusicBrainzEntity.EVENT -> transacter.getCountOfCollectedEventsByEntity(
            entityId = browseMethod.entityId,
        )

        MusicBrainzEntity.LABEL -> transacter.getCountOfCollectedLabelsByEntity(
            entityId = browseMethod.entityId,
        )

        MusicBrainzEntity.PLACE -> transacter.getCountOfCollectedPlacesByEntity(
            entityId = browseMethod.entityId,
        )

        MusicBrainzEntity.RECORDING -> transacter.getCountOfCollectedRecordingsByEntity(
            entityId = browseMethod.entityId,
        )

        MusicBrainzEntity.RELEASE -> transacter.getCountOfCollectedReleasesByEntity(
            entityId = browseMethod.entityId,
        )

        MusicBrainzEntity.RELEASE_GROUP -> transacter.getCountOfCollectedReleaseGroupsByEntity(
            entityId = browseMethod.entityId,
        )

        MusicBrainzEntity.WORK -> transacter.getCountOfCollectedWorksByEntity(
            entityId = browseMethod.entityId,
        )

        MusicBrainzEntity.COLLECTION,
        MusicBrainzEntity.GENRE,
        MusicBrainzEntity.INSTRUMENT,
        MusicBrainzEntity.SERIES,
        MusicBrainzEntity.URL,
        -> error(getError(browseEntity))
    }

    private fun getCountOfAllCollectedEntities(browseEntity: MusicBrainzEntity): Query<Long> = when (browseEntity) {
        MusicBrainzEntity.AREA -> transacter.getCountOfAllCollectedAreas()
        MusicBrainzEntity.ARTIST -> transacter.getCountOfAllCollectedArtists()
        MusicBrainzEntity.EVENT -> transacter.getCountOfAllCollectedEvents()
        MusicBrainzEntity.GENRE -> transacter.getCountOfAllCollectedGenres()
        MusicBrainzEntity.INSTRUMENT -> transacter.getCountOfAllCollectedInstruments()
        MusicBrainzEntity.LABEL -> transacter.getCountOfAllCollectedLabels()
        MusicBrainzEntity.PLACE -> transacter.getCountOfAllCollectedPlaces()
        MusicBrainzEntity.RECORDING -> transacter.getCountOfAllCollectedRecordings()
        MusicBrainzEntity.RELEASE -> transacter.getCountOfAllCollectedReleases()
        MusicBrainzEntity.RELEASE_GROUP -> transacter.getCountOfAllCollectedReleaseGroups()
        MusicBrainzEntity.SERIES -> transacter.getCountOfAllCollectedSeries()
        MusicBrainzEntity.WORK -> transacter.getCountOfAllCollectedWorks()
        MusicBrainzEntity.COLLECTION,
        MusicBrainzEntity.URL,
        -> error(getError(browseEntity))
    }

    private fun getError(browseEntity: MusicBrainzEntity): String =
        "Cannot get collected count for unsupported entity: $browseEntity"
}
