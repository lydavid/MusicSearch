package ly.david.musicsearch.data.database.dao

import app.cash.paging.PagingSource
import app.cash.sqldelight.Query
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOne
import app.cash.sqldelight.paging3.QueryPagingSource
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ly.david.musicsearch.data.database.Database
import ly.david.musicsearch.data.database.mapper.mapToReleaseListItemModel
import ly.david.musicsearch.data.musicbrainz.models.core.ReleaseMusicBrainzNetworkModel
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.NUMBER_OF_LATEST_LISTENS_TO_SHOW
import ly.david.musicsearch.shared.domain.coroutine.CoroutineDispatchers
import ly.david.musicsearch.shared.domain.details.ReleaseDetailsModel
import ly.david.musicsearch.shared.domain.getFormatsForDisplay
import ly.david.musicsearch.shared.domain.getTracksForDisplay
import ly.david.musicsearch.shared.domain.listen.ListenWithTrack
import ly.david.musicsearch.shared.domain.listitem.ReleaseListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.domain.release.FormatTrackCount
import ly.david.musicsearch.shared.domain.release.ReleaseStatus
import ly.david.musicsearch.shared.domain.release.TextRepresentationUiModel
import lydavidmusicsearchdatadatabase.Releases_by_entity
import kotlin.time.Clock
import kotlin.time.Instant

class ReleaseDao(
    database: Database,
    private val artistCreditDao: ArtistCreditDao,
    private val mediumDao: MediumDao,
    private val releaseLabelDao: ReleaseLabelDao,
    private val collectionEntityDao: CollectionEntityDao,
    private val coroutineDispatchers: CoroutineDispatchers,
) : EntityDao {
    override val transacter = database.releaseQueries

    fun upsert(oldId: String, release: ReleaseMusicBrainzNetworkModel) {
        release.run {
            if (oldId != id) {
                delete(oldId)
            }
            transacter.upsert(
                id = id,
                name = name,
                disambiguation = disambiguation,
                date = date.orEmpty(),
                barcode = barcode.orEmpty(),
                status_id = statusId.orEmpty(),
                country_code = countryCode.orEmpty(),
                packaging = packaging.orEmpty(),
                packaging_id = packagingId.orEmpty(),
                asin = asin.orEmpty(),
                quality = quality.orEmpty(),
                script = textRepresentation?.script.orEmpty(),
                language = textRepresentation?.language.orEmpty(),
            )
            artistCreditDao.insertArtistCredits(
                entityId = id,
                artistCredits = artistCredits,
            )
            mediumDao.insertAll(
                releaseId = id,
                media = media,
            )
            // TODO: insert tracks' recording? There's no aliases here, but this will be useful for linking listens to
        }
    }

    fun insert(release: ReleaseMusicBrainzNetworkModel) {
        release.run {
            transacter.insert(
                id = id,
                name = name,
                disambiguation = disambiguation,
                date = date.orEmpty(),
                barcode = barcode.orEmpty(),
                status_id = statusId.orEmpty(),
                country_code = countryCode.orEmpty(),
                packaging = packaging.orEmpty(),
                packaging_id = packagingId.orEmpty(),
                asin = asin.orEmpty(),
                quality = quality.orEmpty(),
                script = textRepresentation?.script.orEmpty(),
                language = textRepresentation?.language.orEmpty(),
            )
            artistCreditDao.insertArtistCredits(
                entityId = id,
                artistCredits = artistCredits,
            )
        }
    }

    fun insertAll(releases: List<ReleaseMusicBrainzNetworkModel>) {
        transacter.transaction {
            releases.forEach { release ->
                insert(release)
            }
        }
    }

    fun delete(releaseId: String) {
        transacter.deleteRelease(releaseId)
    }

    fun getReleaseForDetails(
        releaseId: String,
        listenBrainzUsername: String,
    ): ReleaseDetailsModel? {
        val release = transacter.getReleaseForDetails(
            releaseId = releaseId,
            mapper = ::toDetailsModel,
        ).executeAsOneOrNull()

        val formatTrackCounts = getReleaseFormatTrackCount(releaseId = releaseId)

        val latestListens = if (listenBrainzUsername.isNotEmpty()) {
            transacter.getLatestListensByRelease(
                releaseId = releaseId,
                username = listenBrainzUsername,
                limit = NUMBER_OF_LATEST_LISTENS_TO_SHOW,
                mapper = { position, number, title, listenedAtMs ->
                    ListenWithTrack(
                        mediumPosition = position ?: 1,
                        trackNumber = number,
                        trackName = title,
                        listenedMs = listenedAtMs,
                    )
                },
            ).executeAsList().toPersistentList()
        } else {
            persistentListOf()
        }

        val listenCounts = if (listenBrainzUsername.isNotEmpty()) {
            transacter.getListenCountsByRelease(
                releaseId = releaseId,
                username = listenBrainzUsername,
            ).executeAsOne()
        } else {
            null
        }

        return release?.copy(
            formattedFormats = formatTrackCounts.map { it.format }.getFormatsForDisplay(),
            formattedTracks = formatTrackCounts.map { it.trackCount }.getTracksForDisplay(),
            latestListens = latestListens,
            listenCount = listenCounts?.total_listens,
            mostListenedTrackCount = listenCounts?.most_listened_track_count ?: 0,
            completeListenCount = listenCounts?.complete_listen_count ?: 0,
        )
    }

    private fun toDetailsModel(
        id: String,
        name: String,
        disambiguation: String,
        date: String,
        barcode: String,
        asin: String,
        quality: String,
        countryCode: String,
        statusId: String,
        packaging: String,
        packagingId: String,
        script: String,
        language: String,
        releaseLength: Double?,
        hasNullLength: Boolean,
        lastUpdated: Instant?,
    ) = ReleaseDetailsModel(
        id = id,
        name = name,
        disambiguation = disambiguation,
        date = date,
        barcode = barcode,
        status = ReleaseStatus.fromId(statusId),
        countryCode = countryCode,
        packaging = packaging,
        packagingId = packagingId,
        asin = asin,
        quality = quality,
        textRepresentation = TextRepresentationUiModel(
            script = script,
            language = language,
        ),
        releaseLength = releaseLength?.toInt(),
        hasNullLength = hasNullLength,
        lastUpdated = lastUpdated ?: Clock.System.now(),
    )

    fun getReleaseFormatTrackCount(releaseId: String): List<FormatTrackCount> =
        transacter.getReleaseFormatTrackCount(
            releaseId = releaseId,
            mapper = { format, trackCount ->
                FormatTrackCount(
                    format = format,
                    trackCount = trackCount.toInt(),
                )
            },
        ).executeAsList()

    fun getReleases(
        browseMethod: BrowseMethod,
        query: String,
        username: String,
        sorted: Boolean,
    ): PagingSource<Int, ReleaseListItemModel> = when (browseMethod) {
        is BrowseMethod.All -> {
            getAllReleases(
                query = query,
                username = username,
                sorted = sorted,
            )
        }

        is BrowseMethod.ByEntity -> {
            when (browseMethod.entityType) {
                MusicBrainzEntityType.LABEL -> {
                    getReleasesByLabel(
                        labelId = browseMethod.entityId,
                        query = query,
                        username = username,
                        sorted = sorted,
                    )
                }

                MusicBrainzEntityType.COLLECTION -> {
                    getReleasesByCollection(
                        collectionId = browseMethod.entityId,
                        query = query,
                        username = username,
                        sorted = sorted,
                    )
                }

                else -> {
                    getReleasesByEntity(
                        entityId = browseMethod.entityId,
                        query = query,
                        username = username,
                        sorted = sorted,
                    )
                }
            }
        }
    }

    // region releases by label
    fun insertReleasesByLabel(
        labelId: String,
        releases: List<ReleaseMusicBrainzNetworkModel>,
    ) {
        transacter.transaction {
            releases.forEach { release ->
                insert(release)
                transacter.insertOrIgnoreReleasesByEntity(
                    Releases_by_entity(
                        entity_id = labelId,
                        release_id = release.id,
                    ),
                )
                release.labelInfoList?.forEach { labelInfo ->
                    releaseLabelDao.insertOrIgnore(
                        releaseId = release.id,
                        labelId = labelId,
                        catalogNumber = labelInfo.catalogNumber.orEmpty(),
                    )
                }
            }
        }
    }

    fun getCountOfReleasesByLabel(labelId: String): Int =
        transacter.getNumberOfReleasesByLabel(
            labelId = labelId,
            query = "%%",
        )
            .executeAsOne()
            .toInt()

    private fun getReleasesByLabel(
        labelId: String,
        query: String,
        username: String,
        sorted: Boolean,
    ): PagingSource<Int, ReleaseListItemModel> = QueryPagingSource(
        countQuery = transacter.getNumberOfReleasesByLabel(
            labelId = labelId,
            query = "%$query%",
        ),
        transacter = transacter,
        context = coroutineDispatchers.io,
        queryProvider = { limit, offset ->
            transacter.getReleasesByLabel(
                labelId = labelId,
                query = "%$query%",
                username = username,
                sorted = sorted,
                limit = limit,
                offset = offset,
                mapper = ::mapToReleaseListItemModel,
            )
        },
    )

    fun deleteReleasesByLabel(labelId: String) {
        withTransaction {
            deleteReleaseLinksByEntity(labelId)
            transacter.deleteReleasesByLabelLinks(labelId = labelId)
        }
    }
    // endregion

    fun insertReleasesByEntity(
        entityId: String,
        releases: List<ReleaseMusicBrainzNetworkModel>,
    ) {
        transacter.transaction {
            releases.forEach { release ->
                transacter.insertOrIgnoreReleasesByEntity(
                    Releases_by_entity(
                        entity_id = entityId,
                        release_id = release.id,
                    ),
                )
            }
        }
    }

    fun deleteReleaseLinksByEntity(entityId: String) {
        transacter.deleteReleaseLinksByEntity(entityId)
    }

    private fun getCountOfReleasesByEntityQuery(
        entityId: String,
        query: String,
    ) = transacter.getNumberOfReleasesByEntity(
        entityId = entityId,
        query = "%$query%",
    )

    fun getCountOfReleasesByEntity(entityId: String): Int =
        getCountOfReleasesByEntityQuery(
            entityId = entityId,
            query = "",
        )
            .executeAsOne()
            .toInt()

    private fun getReleasesByEntity(
        entityId: String,
        query: String,
        username: String,
        sorted: Boolean,
    ): PagingSource<Int, ReleaseListItemModel> = QueryPagingSource(
        countQuery = getCountOfReleasesByEntityQuery(
            entityId = entityId,
            query = query,
        ),
        transacter = transacter,
        context = coroutineDispatchers.io,
        queryProvider = { limit, offset ->
            transacter.getReleasesByEntity(
                entityId = entityId,
                query = "%$query%",
                username = username,
                sorted = sorted,
                limit = limit,
                offset = offset,
                mapper = ::mapToReleaseListItemModel,
            )
        },
    )

    // releases by collection

    private fun getReleasesByCollection(
        collectionId: String,
        query: String,
        username: String,
        sorted: Boolean,
    ): PagingSource<Int, ReleaseListItemModel> = QueryPagingSource(
        countQuery = transacter.getNumberOfReleasesByCollection(
            collectionId = collectionId,
            query = "%$query%",
        ),
        transacter = transacter,
        context = coroutineDispatchers.io,
        queryProvider = { limit, offset ->
            transacter.getReleasesByCollection(
                collectionId = collectionId,
                query = "%$query%",
                username = username,
                sorted = sorted,
                limit = limit,
                offset = offset,
                mapper = ::mapToReleaseListItemModel,
            )
        },
    )

    fun observeCountOfReleases(
        browseMethod: BrowseMethod,
    ): Flow<Int> =
        when (browseMethod) {
            is BrowseMethod.ByEntity -> {
                if (browseMethod.entityType == MusicBrainzEntityType.COLLECTION) {
                    collectionEntityDao.getCountOfEntitiesByCollectionQuery(
                        collectionId = browseMethod.entityId,
                    )
                } else {
                    getCountOfReleasesByEntityQuery(
                        entityId = browseMethod.entityId,
                        query = "",
                    )
                }
            }

            else -> {
                getCountOfAllReleases(query = "")
            }
        }
            .asFlow()
            .mapToOne(coroutineDispatchers.io)
            .map { it.toInt() }

    private fun getCountOfAllReleases(
        query: String,
    ): Query<Long> = transacter.getCountOfAllReleases(
        query = "%$query%",
    )

    private fun getAllReleases(
        query: String,
        username: String,
        sorted: Boolean,
    ): PagingSource<Int, ReleaseListItemModel> = QueryPagingSource(
        countQuery = getCountOfAllReleases(
            query = query,
        ),
        transacter = transacter,
        context = coroutineDispatchers.io,
        queryProvider = { limit, offset ->
            transacter.getAllReleases(
                query = "%$query%",
                sorted = sorted,
                username = username,
                limit = limit,
                offset = offset,
                mapper = ::mapToReleaseListItemModel,
            )
        },
    )
}
