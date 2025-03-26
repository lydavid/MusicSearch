package ly.david.musicsearch.data.database.dao

import app.cash.paging.PagingSource
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOne
import app.cash.sqldelight.paging3.QueryPagingSource
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ly.david.musicsearch.core.coroutines.CoroutineDispatchers
import ly.david.musicsearch.data.database.Database
import ly.david.musicsearch.data.database.mapper.mapToRecordingListItemModel
import ly.david.musicsearch.data.musicbrainz.models.core.RecordingMusicBrainzModel
import ly.david.musicsearch.shared.domain.listitem.RecordingListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.recording.RecordingDetailsModel
import lydavidmusicsearchdatadatabase.Recording
import lydavidmusicsearchdatadatabase.Recordings_by_entity

class RecordingDao(
    database: Database,
    private val coroutineDispatchers: CoroutineDispatchers,
    private val artistCreditDao: ArtistCreditDao,
) : EntityDao {
    override val transacter = database.recordingQueries

    fun insert(recording: RecordingMusicBrainzModel) {
        recording.run {
            transacter.insertRecording(
                Recording(
                    id = id,
                    name = name,
                    disambiguation = disambiguation,
                    first_release_date = firstReleaseDate,
                    length = length,
                    video = video ?: false,
                    isrcs = isrcs?.toImmutableList(),
                ),
            )
            artistCreditDao.insertArtistCredits(
                entityId = recording.id,
                artistCredits = artistCredits,
            )
        }
    }

    fun insertAll(recordings: List<RecordingMusicBrainzModel>) {
        transacter.transaction {
            recordings.forEach { recording ->
                insert(recording)
            }
        }
    }

    fun getRecordingForDetails(recordingId: String): RecordingDetailsModel? {
        return transacter.getRecordingForDetails(
            recordingId,
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
    ) = RecordingDetailsModel(
        id = id,
        name = name,
        firstReleaseDate = firstReleaseDate,
        disambiguation = disambiguation,
        length = length,
        video = video,
        isrcs = isrcs,
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
    ): Int {
        return transacter.transactionWithResult {
            recordingIds.forEach { recordingId ->
                insertRecordingByEntity(
                    recordingId = recordingId,
                    entityId = entityId,
                )
            }
            recordingIds.size
        }
    }

    fun deleteRecordingsByEntity(entityId: String) {
        transacter.deleteRecordingsByEntity(entityId)
    }

    fun observeCountOfRecordingsByEntity(entityId: String): Flow<Int> =
        transacter.getNumberOfRecordingsByEntity(
            entityId = entityId,
            query = "%%",
        )
            .asFlow()
            .mapToOne(coroutineDispatchers.io)
            .map { it.toInt() }

    fun getCountOfRecordingsByEntity(entityId: String): Int =
        transacter.getNumberOfRecordingsByEntity(
            entityId = entityId,
            query = "%%",
        )
            .executeAsOne()
            .toInt()

    fun getRecordings(
        entityId: String?,
        entity: MusicBrainzEntity?,
        query: String,
    ): PagingSource<Int, RecordingListItemModel> = when {
        entityId == null || entity == null -> {
            getAllRecordings(query = query)
        }

        entity == MusicBrainzEntity.COLLECTION -> {
            getRecordingsByCollection(
                collectionId = entityId,
                query = query,
            )
        }

        else -> {
            getRecordingsByEntity(
                entityId = entityId,
                query = query,
            )
        }
    }

    private fun getAllRecordings(
        query: String,
    ): PagingSource<Int, RecordingListItemModel> = QueryPagingSource(
        countQuery = transacter.getCountOfAllRecordings(
            query = "%$query%",
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
        countQuery = transacter.getNumberOfRecordingsByEntity(
            entityId = entityId,
            query = "%$query%",
        ),
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
