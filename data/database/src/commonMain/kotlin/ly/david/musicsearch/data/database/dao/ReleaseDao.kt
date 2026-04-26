package ly.david.musicsearch.data.database.dao

import androidx.paging.PagingSource
import app.cash.sqldelight.Query
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
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
import ly.david.musicsearch.shared.domain.release.ReleaseStatusCount
import ly.david.musicsearch.shared.domain.release.TextRepresentationUiModel
import ly.david.musicsearch.shared.domain.sort.ReleaseSortOption
import lydavidmusicsearchdatadatabase.Releases_by_entity
import kotlin.time.Clock
import kotlin.time.Instant

class ReleaseDao(
    database: Database,
    private val artistCreditDao: ArtistCreditDao,
    private val mediumDao: MediumDao,
    private val releaseLabelDao: ReleaseLabelDao,
    private val coroutineDispatchers: CoroutineDispatchers,
) : EntityDao {
    override val transacter = database.releaseQueries

    private fun String?.idToReleaseStatus(): ReleaseStatus {
        return when (this) {
            "4e304316-386d-3409-af2e-78857eec5cfe" -> ReleaseStatus.OFFICIAL
            "518ffc83-5cde-34df-8627-81bff5093d92" -> ReleaseStatus.PROMOTION
            "1156806e-d06a-38bd-83f0-cf2284a808b9" -> ReleaseStatus.BOOTLEG
            "41121bb9-3413-3818-8a9a-9742318349aa" -> ReleaseStatus.PSEUDO_RELEASE
            "6a3772de-4605-4132-993d-aa315cd19b4b" -> ReleaseStatus.WITHDRAWN
            "cddc06b1-1afc-4bbb-83e6-0a902ffb4aba" -> ReleaseStatus.EXPUNGED
            "55005350-bc3f-441a-b0c7-27c565eae341" -> ReleaseStatus.CANCELLED
            else -> ReleaseStatus.UNKNOWN
        }
    }

    fun upsert(
        oldId: String,
        release: ReleaseMusicBrainzNetworkModel,
    ) {
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
                status = statusId.idToReleaseStatus(),
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
                status = statusId.idToReleaseStatus(),
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
        status: ReleaseStatus,
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
        status = status,
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
        sortOption: ReleaseSortOption,
        showReleaseStatuses: Set<ReleaseStatus>,
    ): PagingSource<Int, ReleaseListItemModel> = when (browseMethod) {
        is BrowseMethod.All -> {
            getAllReleases(
                query = query,
                username = username,
                sortOption = sortOption,
                showReleaseStatuses = showReleaseStatuses,
            )
        }

        is BrowseMethod.ByEntity -> {
            when (browseMethod.entityType) {
                MusicBrainzEntityType.LABEL -> {
                    getReleasesByLabel(
                        labelId = browseMethod.entityId,
                        query = query,
                        username = username,
                        sortOption = sortOption,
                        showReleaseStatuses = showReleaseStatuses,
                    )
                }

                MusicBrainzEntityType.COLLECTION -> {
                    getReleasesByCollection(
                        collectionId = browseMethod.entityId,
                        query = query,
                        username = username,
                        sortOption = sortOption,
                        showReleaseStatuses = showReleaseStatuses,
                    )
                }

                else -> {
                    getReleasesByEntity(
                        entityId = browseMethod.entityId,
                        query = query,
                        username = username,
                        sortOption = sortOption,
                        showReleaseStatuses = showReleaseStatuses,
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

    fun getCountOfReleasesByLabelQuery(
        labelId: String,
        query: String,
        showReleaseStatuses: Set<ReleaseStatus>,
    ) = transacter.getNumberOfReleasesByLabel(
        entityId = labelId,
        query = "%$query%",
        statuses = showReleaseStatuses,
    )

    private fun getReleasesByLabel(
        labelId: String,
        query: String,
        username: String,
        sortOption: ReleaseSortOption,
        showReleaseStatuses: Set<ReleaseStatus>,
    ): PagingSource<Int, ReleaseListItemModel> = QueryPagingSource(
        countQuery = getCountOfReleasesByLabelQuery(
            labelId = labelId,
            query = query,
            showReleaseStatuses = showReleaseStatuses,
        ),
        transacter = transacter,
        context = coroutineDispatchers.io,
        queryProvider = { limit, offset ->
            transacter.getReleasesByLabel(
                entityId = labelId,
                query = "%$query%",
                username = username,
                statuses = showReleaseStatuses,
                sortBy = sortOption.order.toLong(),
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

    fun getCountOfReleasesByEntityQuery(
        entityId: String,
        query: String,
        showReleaseStatuses: Set<ReleaseStatus>,
    ) = transacter.getNumberOfReleasesByEntity(
        entityId = entityId,
        query = "%$query%",
        statuses = showReleaseStatuses,
    )

    private fun getReleasesByEntity(
        entityId: String,
        query: String,
        username: String,
        sortOption: ReleaseSortOption,
        showReleaseStatuses: Set<ReleaseStatus>,
    ): PagingSource<Int, ReleaseListItemModel> = QueryPagingSource(
        countQuery = getCountOfReleasesByEntityQuery(
            entityId = entityId,
            query = query,
            showReleaseStatuses = showReleaseStatuses,
        ),
        transacter = transacter,
        context = coroutineDispatchers.io,
        queryProvider = { limit, offset ->
            transacter.getReleasesByEntity(
                entityId = entityId,
                query = "%$query%",
                username = username,
                statuses = showReleaseStatuses,
                sortBy = sortOption.order.toLong(),
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
        sortOption: ReleaseSortOption,
        showReleaseStatuses: Set<ReleaseStatus>,
    ): PagingSource<Int, ReleaseListItemModel> = QueryPagingSource(
        countQuery = getCountOfReleasesByCollectionQuery(
            collectionId = collectionId,
            query = query,
            showReleaseStatuses = showReleaseStatuses,
        ),
        transacter = transacter,
        context = coroutineDispatchers.io,
        queryProvider = { limit, offset ->
            transacter.getReleasesByCollection(
                collectionId = collectionId,
                query = "%$query%",
                username = username,
                statuses = showReleaseStatuses,
                sortBy = sortOption.order.toLong(),
                limit = limit,
                offset = offset,
                mapper = ::mapToReleaseListItemModel,
            )
        },
    )

    fun getCountOfReleasesByCollectionQuery(
        collectionId: String,
        query: String,
        showReleaseStatuses: Set<ReleaseStatus>,
    ): Query<Long> = transacter.getNumberOfReleasesByCollection(
        collectionId = collectionId,
        query = "%$query%",
        statuses = showReleaseStatuses,
    )

    fun observeCountOfReleases(
        browseMethod: BrowseMethod,
        query: String,
        showReleaseStatuses: Set<ReleaseStatus>,
    ): Flow<Int> =
        when (browseMethod) {
            is BrowseMethod.ByEntity -> {
                if (browseMethod.entityType == MusicBrainzEntityType.COLLECTION) {
                    getCountOfReleasesByCollectionQuery(
                        collectionId = browseMethod.entityId,
                        query = query,
                        showReleaseStatuses = showReleaseStatuses,
                    )
                } else {
                    getCountOfReleasesByEntityQuery(
                        entityId = browseMethod.entityId,
                        query = query,
                        showReleaseStatuses = showReleaseStatuses,
                    )
                }
            }

            BrowseMethod.All -> {
                getCountOfAllReleasesQuery(
                    query = query,
                    showReleaseStatuses = showReleaseStatuses,
                )
            }
        }
            .asFlow()
            .mapToOne(coroutineDispatchers.io)
            .map { it.toInt() }

    private fun getCountOfAllReleasesQuery(
        query: String,
        showReleaseStatuses: Set<ReleaseStatus>,
    ): Query<Long> = transacter.getCountOfAllReleases(
        query = "%$query%",
        statuses = showReleaseStatuses,
    )

    private fun getAllReleases(
        query: String,
        username: String,
        sortOption: ReleaseSortOption,
        showReleaseStatuses: Set<ReleaseStatus>,
    ): PagingSource<Int, ReleaseListItemModel> = QueryPagingSource(
        countQuery = getCountOfAllReleasesQuery(
            query = query,
            showReleaseStatuses = showReleaseStatuses,
        ),
        transacter = transacter,
        context = coroutineDispatchers.io,
        queryProvider = { limit, offset ->
            transacter.getAllReleases(
                query = "%$query%",
                sortBy = sortOption.order.toLong(),
                username = username,
                statuses = showReleaseStatuses,
                limit = limit,
                offset = offset,
                mapper = ::mapToReleaseListItemModel,
            )
        },
    )

    fun observeCountOfEachStatus(
        browseMethod: BrowseMethod,
    ): Flow<List<ReleaseStatusCount>> {
        return when (browseMethod) {
            is BrowseMethod.ByEntity -> {
                if (browseMethod.entityType == MusicBrainzEntityType.COLLECTION) {
                    transacter.getCountOfEachStatusByCollection(
                        collectionId = browseMethod.entityId,
                        mapper = ::mapToReleaseStatusWithCount,
                    )
                } else {
                    transacter.getCountOfEachStatusByEntity(
                        entityId = browseMethod.entityId,
                        mapper = ::mapToReleaseStatusWithCount,
                    )
                }
            }

            BrowseMethod.All -> {
                transacter.getCountOfEachStatus(
                    mapper = ::mapToReleaseStatusWithCount,
                )
            }
        }
            .asFlow()
            .mapToList(coroutineDispatchers.io)
    }

    private fun mapToReleaseStatusWithCount(
        status: ReleaseStatus,
        count: Long,
    ): ReleaseStatusCount {
        return ReleaseStatusCount(
            status = status,
            count = count.toInt(),
        )
    }
}
