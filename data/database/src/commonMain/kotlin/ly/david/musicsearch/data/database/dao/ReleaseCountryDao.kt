package ly.david.musicsearch.data.database.dao

import ly.david.musicsearch.data.database.Database
import lydavidmusicsearchdatadatabase.Release_country

class ReleaseCountryDao(
    database: Database,
) : EntityDao {
    override val transacter = database.release_countryQueries

    @Suppress("SwallowedException")
    fun insert(
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

    fun deleteCountriesByReleaseLinks(releaseId: String) {
        transacter.deleteCountriesByReleaseLinks(releaseId = releaseId)
    }
}
