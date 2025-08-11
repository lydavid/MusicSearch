package ly.david.musicsearch.data.database.dao

import app.cash.paging.PagingSource
import app.cash.sqldelight.Query
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOne
import app.cash.sqldelight.paging3.QueryPagingSource
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.time.Clock
import kotlin.time.Instant
import ly.david.musicsearch.shared.domain.coroutine.CoroutineDispatchers
import ly.david.musicsearch.data.database.Database
import ly.david.musicsearch.data.database.mapper.mapToRecordingListItemModel
import ly.david.musicsearch.data.musicbrainz.models.core.RecordingMusicBrainzNetworkModel
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.details.RecordingDetailsModel
import ly.david.musicsearch.shared.domain.listitem.RecordingListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import lydavidmusicsearchdatadatabase.Recording
import lydavidmusicsearchdatadatabase.Recordings_by_entity

class RecordingDao(
    database: Database,
    private val artistCreditDao: ArtistCreditDao,
    private val collectionEntityDao: CollectionEntityDao,
    private val coroutineDispatchers: CoroutineDispatchers,
) : EntityDao {
    override val transacter = database.recordingQueries

    fun insert(recording: RecordingMusicBrainzNetworkModel) {
        recording.run {
            transacter.insertRecording(
                Recording(
                    id = id,
                    name = name,
                    disambiguation = disambiguation,
                    first_release_date = firstReleaseDate,
                    length = length,
                    video = video == true,
                    isrcs = isrcs?.toImmutableList(),
                ),
            )
            artistCreditDao.insertArtistCredits(
                entityId = recording.id,
                artistCredits = artistCredits,
            )
        }
    }

    fun insertAll(recordings: List<RecordingMusicBrainzNetworkModel>) {
        transacter.transaction {
            recordings.forEach { recording ->
                insert(recording)
            }
        }
    }

    fun getRecordingForDetails(recordingId: String): RecordingDetailsModel? {
        return transacter.getRecordingForDetails(
            recordingId = recordingId,
            mapper = ::toDetailsModel,
        ).executeAsOneOrNull()
    }

    private fun toDetailsModel(
        id: String,
        name: String,
        disambiguation: String,
        firstReleaseDate: String?,
        length: Int?,
        video: Boolean,
        isrcs: List<String>?,
        lastUpdated: Instant?,
    ) = RecordingDetailsModel(
        id = id,
        name = name,
        firstReleaseDate = firstReleaseDate,
        disambiguation = disambiguation,
        length = length,
        video = video,
        isrcs = isrcs,
        lastUpdated = lastUpdated ?: Clock.System.now(),
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
            if (browseMethod.entity == MusicBrainzEntity.COLLECTION) {
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
                if (browseMethod.entity == MusicBrainzEntity.COLLECTION) {
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
