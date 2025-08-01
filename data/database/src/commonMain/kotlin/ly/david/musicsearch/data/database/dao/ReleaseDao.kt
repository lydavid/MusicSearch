package ly.david.musicsearch.data.database.dao

import app.cash.paging.PagingSource
import app.cash.sqldelight.Query
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOne
import app.cash.sqldelight.paging3.QueryPagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import ly.david.musicsearch.data.database.Database
import ly.david.musicsearch.data.database.mapper.mapToReleaseListItemModel
import ly.david.musicsearch.data.musicbrainz.models.core.ReleaseMusicBrainzNetworkModel
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.coroutine.CoroutineDispatchers
import ly.david.musicsearch.shared.domain.details.ReleaseDetailsModel
import ly.david.musicsearch.shared.domain.listitem.ReleaseListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.release.CoverArtArchiveUiModel
import ly.david.musicsearch.shared.domain.release.FormatTrackCount
import ly.david.musicsearch.shared.domain.release.TextRepresentationUiModel
import lydavidmusicsearchdatadatabase.Release
import lydavidmusicsearchdatadatabase.Releases_by_entity

class ReleaseDao(
    database: Database,
    private val artistCreditDao: ArtistCreditDao,
    private val mediumDao: MediumDao,
    private val releaseLabelDao: ReleaseLabelDao,
    private val releaseCountryDao: ReleaseCountryDao,
    private val collectionEntityDao: CollectionEntityDao,
    private val coroutineDispatchers: CoroutineDispatchers,
) : EntityDao {
    override val transacter = database.releaseQueries

    fun insertAll(releases: List<ReleaseMusicBrainzNetworkModel>) {
        transacter.transaction {
            releases.forEach { release ->
                insert(release)
            }
        }
    }

    fun insert(release: ReleaseMusicBrainzNetworkModel) {
        release.run {
            transacter.insertRelease(
                Release(
                    id = id,
                    name = name,
                    disambiguation = disambiguation,
                    date = date.orEmpty(),
                    status = status,
                    barcode = barcode,
                    status_id = statusId,
                    country_code = countryCode,
                    packaging = packaging,
                    packaging_id = packagingId,
                    asin = asin,
                    quality = quality,
                    cover_art_count = coverArtArchive.count,
                    script = textRepresentation?.script,
                    language = textRepresentation?.language,
                ),
            )
            artistCreditDao.insertArtistCredits(
                entityId = id,
                artistCredits = artistCredits,
            )
            mediumDao.insertAll(
                releaseId = id,
                media = media,
            )
            // TODO: insert tracks' recording/aliases?
        }
    }

    fun delete(releaseId: String) {
        transacter.deleteRelease(releaseId)
    }

    fun getReleaseForDetails(releaseId: String): ReleaseDetailsModel? =
        transacter.getReleaseForDetails(
            releaseId = releaseId,
            mapper = ::toDetailsModel,
        ).executeAsOneOrNull()

    private fun toDetailsModel(
        id: String,
        name: String,
        disambiguation: String,
        date: String,
        barcode: String?,
        asin: String?,
        quality: String?,
        countryCode: String?,
        status: String?,
        statusId: String?,
        packaging: String?,
        packagingId: String?,
        script: String?,
        language: String?,
        coverArtCount: Int,
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
        statusId = statusId,
        countryCode = countryCode,
        packaging = packaging,
        packagingId = packagingId,
        asin = asin,
        quality = quality,
        coverArtArchive = CoverArtArchiveUiModel(
            count = coverArtCount,
        ),
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
    ): PagingSource<Int, ReleaseListItemModel> = when (browseMethod) {
        is BrowseMethod.All -> {
            getAllReleases(
                query = query,
            )
        }

        is BrowseMethod.ByEntity -> {
            when (browseMethod.entity) {
                MusicBrainzEntity.AREA -> {
                    getReleasesByCountry(
                        areaId = browseMethod.entityId,
                        query = query,
                    )
                }

                MusicBrainzEntity.LABEL -> {
                    getReleasesByLabel(
                        labelId = browseMethod.entityId,
                        query = query,
                    )
                }

                MusicBrainzEntity.COLLECTION -> {
                    getReleasesByCollection(
                        collectionId = browseMethod.entityId,
                        query = query,
                    )
                }

                else -> {
                    getReleasesByEntity(
                        entityId = browseMethod.entityId,
                        query = query,
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

    // region releases by country
    fun insertReleasesByCountry(
        areaId: String,
        releases: List<ReleaseMusicBrainzNetworkModel>,
    ) {
        transacter.transaction {
            releases.forEach { release ->
                insert(release)
                transacter.insertOrIgnoreReleasesByEntity(
                    Releases_by_entity(
                        entity_id = areaId,
                        release_id = release.id,
                    ),
                )
                release.releaseEvents?.forEach { releaseEvent ->
                    releaseCountryDao.insertOrIgnore(
                        areaId = areaId,
                        releaseId = release.id,
                        date = releaseEvent.date,
                    )
                }
            }
        }
    }

    fun getCountOfReleasesByCountry(areaId: String): Int =
        transacter.getNumberOfReleasesByCountry(
            areaId = areaId,
            query = "%%",
        )
            .executeAsOne()
            .toInt()

    private fun getReleasesByCountry(
        areaId: String,
        query: String,
    ): PagingSource<Int, ReleaseListItemModel> = QueryPagingSource(
        countQuery = transacter.getNumberOfReleasesByCountry(
            areaId = areaId,
            query = "%$query%",
        ),
        transacter = transacter,
        context = coroutineDispatchers.io,
        queryProvider = { limit, offset ->
            transacter.getReleasesByCountry(
                areaId = areaId,
                query = "%$query%",
                limit = limit,
                offset = offset,
                mapper = ::mapToReleaseListItemModel,
            )
        },
    )

    fun deleteReleasesByCountry(areaId: String) {
        deleteReleaseLinksByEntity(entityId = areaId)
        // Do not delete from release_country,
        // so that refreshing an area's release will not remove a release's release events
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
                if (browseMethod.entity == MusicBrainzEntity.COLLECTION) {
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
    ): PagingSource<Int, ReleaseListItemModel> = QueryPagingSource(
        countQuery = getCountOfAllReleases(
            query = query,
        ),
        transacter = transacter,
        context = coroutineDispatchers.io,
        queryProvider = { limit, offset ->
            transacter.getAllReleases(
                query = "%$query%",
                limit = limit,
                offset = offset,
                mapper = ::mapToReleaseListItemModel,
            )
        },
    )
}
