package ly.david.musicsearch.data.database.dao

import app.cash.paging.PagingSource
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOne
import app.cash.sqldelight.paging3.QueryPagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ly.david.musicsearch.core.coroutines.CoroutineDispatchers
import ly.david.musicsearch.data.database.Database
import ly.david.musicsearch.data.database.mapper.mapToReleaseListItemModel
import ly.david.musicsearch.data.musicbrainz.models.core.ReleaseEventMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.ReleaseMusicBrainzModel
import ly.david.musicsearch.shared.domain.area.ReleaseEvent
import ly.david.musicsearch.shared.domain.listitem.ReleaseListItemModel
import lydavidmusicsearchdatadatabase.Release_country

/**
 * Links releases and countries (subset of areas).
 */
class ReleaseCountryDao(
    database: Database,
    private val coroutineDispatchers: CoroutineDispatchers,
) : EntityDao {
    override val transacter = database.release_countryQueries

    @Suppress("SwallowedException")
    private fun insert(
        areaId: String,
        releaseId: String,
        date: String?,
    ): Int {
        return try {
            transacter.insertOrFail(
                Release_country(
                    country_id = areaId,
                    release_id = releaseId,
                    date = date,
                ),
            )
            1
        } catch (ex: Exception) {
            0
        }
    }

    // region countries by release
    fun linkCountriesByRelease(
        releaseId: String,
        releaseEvents: List<ReleaseEventMusicBrainzModel>?,
    ) {
        transacter.transaction {
            releaseEvents?.forEach { releaseEvent ->
                val areaId = releaseEvent.area?.id ?: return@forEach
                insert(
                    areaId = areaId,
                    releaseId = releaseId,
                    date = releaseEvent.date,
                )
            }
        }
    }

    fun getCountriesByRelease(
        releaseId: String,
    ): List<ReleaseEvent> = transacter.getCountriesByRelease(
        releaseId = releaseId,
        mapper = { id, name, date, countryCode, visited ->
            ReleaseEvent(
                id = id,
                name = name,
                date = date,
                countryCode = countryCode,
                visited = visited,
            )
        },
    ).executeAsList()

    fun deleteCountriesByReleaseLinks(releaseId: String) {
        transacter.deleteCountriesByReleaseLinks(releaseId = releaseId)
    }
    // endregion

    // region releases by country
    fun linkReleasesByCountry(
        areaId: String,
        releases: List<ReleaseMusicBrainzModel>,
    ): Int {
        return transacter.transactionWithResult {
            releases.sumOf { release ->
                release.releaseEvents?.sumOf { releaseEvent ->
                    insert(
                        areaId = areaId,
                        releaseId = release.id,
                        date = releaseEvent.date,
                    )
                } ?: 0
            }
        }
    }

    fun observeCountOfReleasesByCountry(areaId: String): Flow<Int> =
        transacter.getNumberOfReleasesByCountry(
            areaId = areaId,
            query = "%%",
        )
            .asFlow()
            .mapToOne(coroutineDispatchers.io)
            .map { it.toInt() }

    fun getCountOfReleasesByCountry(areaId: String): Int =
        transacter.getNumberOfReleasesByCountry(
            areaId = areaId,
            query = "%%",
        )
            .executeAsOne()
            .toInt()

    fun getReleasesByCountry(
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
        transacter.deleteReleasesByCountry(areaId = areaId)
    }
    // endregion
}
