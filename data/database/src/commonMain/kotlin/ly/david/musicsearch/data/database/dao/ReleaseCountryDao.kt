package ly.david.musicsearch.data.database.dao

import ly.david.musicsearch.data.database.Database
import lydavidmusicsearchdatadatabase.Release_country

class ReleaseCountryDao(
    database: Database,
) : EntityDao {
    override val transacter = database.release_countryQueries

    fun insertOrIgnore(
        areaId: String,
        releaseId: String,
        date: String?,
    ) {
        transacter.insertOrIgnore(
            Release_country(
                country_id = areaId,
                release_id = releaseId,
                date = date,
            ),
        )
    }

    fun deleteCountriesByReleaseLinks(releaseId: String) {
        transacter.deleteCountriesByReleaseLinks(releaseId = releaseId)
    }
}
