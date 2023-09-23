package ly.david.musicsearch.data.database.dao

import app.cash.paging.PagingSource
import app.cash.sqldelight.paging3.QueryPagingSource
import kotlinx.coroutines.Dispatchers
import ly.david.data.core.ReleaseForListItem
import ly.david.data.musicbrainz.ReleaseMusicBrainzModel
import ly.david.musicsearch.data.database.Database
import ly.david.musicsearch.data.database.mapper.mapToReleaseForListItem
import lydavidmusicsearchdatadatabase.Release_country

/**
 * Links releases and areas that are countries.
 */
class ReleaseCountryDao(
    database: Database,
) : EntityDao {
    override val transacter = database.release_countryQueries

    fun insert(
        areaId: String,
        releaseId: String,
        date: String?,
    ) {
        transacter.insert(
            Release_country(
                country_id = areaId,
                release_id = releaseId,
                date = date,
            )
        )
    }

    fun insertAll(
        areaId: String,
        releases: List<ReleaseMusicBrainzModel>,
    ) {
        transacter.transaction {
            releases.forEach { release ->
                release.releaseEvents?.forEach { releaseEvent ->
                    insert(
                        areaId = areaId,
                        releaseId = release.id,
                        date = releaseEvent.date,
                    )
                }
            }
        }
    }

    fun deleteReleasesByCountry(areaId: String) {
        withTransaction {
            transacter.deleteReleasesByCountry(areaId)
            transacter.deleteReleaseCountryLinks(areaId)
        }
    }

    fun getNumberOfReleasesByCountry(areaId: String): Int =
        transacter.getNumberOfReleasesByCountry(
            areaId = areaId,
            query = "%%",
        ).executeAsOne().toInt()

    fun getReleasesByCountry(
        areaId: String,
        query: String,
    ): PagingSource<Int, ReleaseForListItem> = QueryPagingSource(
        countQuery = transacter.getNumberOfReleasesByCountry(
            areaId = areaId,
            query = query,
        ),
        transacter = transacter,
        context = Dispatchers.IO,
    ) { limit, offset ->
        transacter.getReleasesByCountry(
            areaId = areaId,
            query = query,
            limit = limit,
            offset = offset,
            mapper = ::mapToReleaseForListItem,
        )
    }
}
