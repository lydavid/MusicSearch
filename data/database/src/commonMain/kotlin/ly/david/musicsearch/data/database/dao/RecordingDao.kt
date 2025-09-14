package ly.david.musicsearch.data.database.dao

import app.cash.paging.PagingSource
import app.cash.sqldelight.Query
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOne
import app.cash.sqldelight.paging3.QueryPagingSource
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ly.david.musicsearch.data.database.Database
import ly.david.musicsearch.data.database.mapper.mapToRecordingListItemModel
import ly.david.musicsearch.data.musicbrainz.models.core.RecordingMusicBrainzNetworkModel
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.NUMBER_OF_LATEST_LISTENS_TO_SHOW
import ly.david.musicsearch.shared.domain.coroutine.CoroutineDispatchers
import ly.david.musicsearch.shared.domain.details.RecordingDetailsModel
import ly.david.musicsearch.shared.domain.listitem.RecordingListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import lydavidmusicsearchdatadatabase.Recordings_by_entity
import kotlin.time.Clock
import kotlin.time.Instant

class RecordingDao(
    database: Database,
    private val artistCreditDao: ArtistCreditDao,
    private val collectionEntityDao: CollectionEntityDao,
    private val coroutineDispatchers: CoroutineDispatchers,
) : EntityDao {
    override val transacter = database.recordingQueries

    fun upsert(
        oldRecordingId: String,
        recording: RecordingMusicBrainzNetworkModel,
    ) {
        recording.run {
            if (oldRecordingId != id) {
                // TODO: propagate update to tracks and listens?
                // We don't need to update other lists, let them handle it themselves,
                // though by deleting the old recording that they linked to, they will be incomplete,
                // forcing them to fetch from remote. It's possible their paging gets messed up because of this.
                // It's possible to fix by refreshing those lists.
                // A similar thing happens when refreshing a recording that has changed its id.
                delete(oldRecordingId)
            }
            transacter.upsert(
                id = id,
                name = name,
                disambiguation = disambiguation,
                firstReleaseDate = firstReleaseDate.orEmpty(),
                length = length,
                video = video == true,
                isrcs = isrcs?.sorted().orEmpty(),
            )
            artistCreditDao.insertArtistCredits(
                entityId = recording.id,
                artistCredits = artistCredits,
            )
        }
    }

    // we can do this because lookup, search, and browse all returns the same amount of info that goes into this table
    fun upsertAll(recordings: List<RecordingMusicBrainzNetworkModel>) {
        transacter.transaction {
            recordings.forEach { recording ->
                upsert(oldRecordingId = recording.id, recording = recording)
            }
        }
    }

    fun getRecordingForDetails(
        recordingId: String,
        listenBrainzUsername: String,
    ): RecordingDetailsModel? {
        return transacter.transactionWithResult {
            val recording = transacter.getRecordingForDetails(
                recordingId = recordingId,
                username = listenBrainzUsername,
                mapper = ::toDetailsModel,
            ).executeAsOneOrNull()

            recording?.copy(
                latestListensTimestampsMs = transacter.getLatestListensByRecording(
                    recordingId = recordingId,
                    username = listenBrainzUsername,
                    limit = NUMBER_OF_LATEST_LISTENS_TO_SHOW,
                ).executeAsList().mapNotNull { it.listened_at_ms }.toPersistentList(),
            )
        }
    }

    private fun toDetailsModel(
        id: String,
        name: String,
        disambiguation: String,
        firstReleaseDate: String,
        length: Int?,
        video: Boolean,
        isrcs: List<String>,
        lastUpdated: Instant?,
        listenCount: Long,
    ) = RecordingDetailsModel(
        id = id,
        name = name,
        firstReleaseDate = firstReleaseDate,
        disambiguation = disambiguation,
        length = length,
        video = video,
        isrcs = isrcs.toPersistentList(),
        lastUpdated = lastUpdated ?: Clock.System.now(),
        listenCount = listenCount,
    )

    fun delete(id: String) {
        transacter.deleteRecording(id)
    }

    private fun insertRecordingByEntity(
        entityId: String,
        recordingId: String,
    ) {
        transacter.insertOrIgnoreRecordingByEntity(
            Recordings_by_entity(
                entity_id = entityId,
                recording_id = recordingId,
            ),
        )
    }

    fun insertRecordingsByEntity(
        entityId: String,
        recordingIds: List<String>,
    ) {
        return transacter.transaction {
            recordingIds.forEach { recordingId ->
                insertRecordingByEntity(
                    recordingId = recordingId,
                    entityId = entityId,
                )
            }
        }
    }

    fun deleteRecordingLinksByEntity(entityId: String) {
        transacter.deleteRecordingLinksByEntity(entityId)
    }

    fun getCountOfRecordingsByEntity(entityId: String): Int =
        getCountOfRecordingsByEntityQuery(
            entityId = entityId,
            query = "",
        )
            .executeAsOne()
            .toInt()

    fun getRecordings(
        browseMethod: BrowseMethod,
        query: String,
    ): PagingSource<Int, RecordingListItemModel> = when (browseMethod) {
        is BrowseMethod.All -> {
            getAllRecordings(
                query = query,
            )
        }

        is BrowseMethod.ByEntity -> {
            if (browseMethod.entity == MusicBrainzEntityType.COLLECTION) {
                getRecordingsByCollection(
                    collectionId = browseMethod.entityId,
                    query = query,
                )
            } else {
                getRecordingsByEntity(
                    entityId = browseMethod.entityId,
                    query = query,
                )
            }
        }
    }

    fun observeCountOfRecordings(browseMethod: BrowseMethod): Flow<Int> =
        when (browseMethod) {
            is BrowseMethod.ByEntity -> {
                if (browseMethod.entity == MusicBrainzEntityType.COLLECTION) {
                    collectionEntityDao.getCountOfEntitiesByCollectionQuery(
                        collectionId = browseMethod.entityId,
                    )
                } else {
                    getCountOfRecordingsByEntityQuery(
                        entityId = browseMethod.entityId,
                        query = "",
                    )
                }
            }

            else -> {
                getCountOfAllRecordings(query = "")
            }
        }
            .asFlow()
            .mapToOne(coroutineDispatchers.io)
            .map { it.toInt() }

    private fun getCountOfAllRecordings(
        query: String,
    ): Query<Long> = transacter.getCountOfAllRecordings(
        query = "%$query%",
    )

    private fun getAllRecordings(
        query: String,
    ): PagingSource<Int, RecordingListItemModel> = QueryPagingSource(
        countQuery = getCountOfAllRecordings(
            query = query,
        ),
        transacter = transacter,
        context = coroutineDispatchers.io,
        queryProvider = { limit, offset ->
            transacter.getAllRecordings(
                query = "%$query%",
                limit = limit,
                offset = offset,
                mapper = ::mapToRecordingListItemModel,
            )
        },
    )

    private fun getRecordingsByEntity(
        entityId: String,
        query: String,
    ): PagingSource<Int, RecordingListItemModel> = QueryPagingSource(
        countQuery = getCountOfRecordingsByEntityQuery(entityId, query),
        transacter = transacter,
        context = coroutineDispatchers.io,
        queryProvider = { limit, offset ->
            transacter.getRecordingsByEntity(
                entityId = entityId,
                query = "%$query%",
                limit = limit,
                offset = offset,
                mapper = ::mapToRecordingListItemModel,
            )
        },
    )

    private fun getCountOfRecordingsByEntityQuery(
        entityId: String,
        query: String,
    ) = transacter.getNumberOfRecordingsByEntity(
        entityId = entityId,
        query = "%$query%",
    )

    private fun getRecordingsByCollection(
        collectionId: String,
        query: String,
    ): PagingSource<Int, RecordingListItemModel> = QueryPagingSource(
        countQuery = transacter.getNumberOfRecordingsByCollection(
            collectionId = collectionId,
            query = "%$query%",
        ),
        transacter = transacter,
        context = coroutineDispatchers.io,
        queryProvider = { limit, offset ->
            transacter.getRecordingsByCollection(
                collectionId = collectionId,
                query = "%$query%",
                limit = limit,
                offset = offset,
                mapper = ::mapToRecordingListItemModel,
            )
        },
    )
}
