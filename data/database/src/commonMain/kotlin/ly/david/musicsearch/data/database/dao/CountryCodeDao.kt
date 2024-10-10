package ly.david.musicsearch.data.database.dao

import ly.david.musicsearch.data.database.Database
import lydavidmusicsearchdatadatabase.Country_code
import lydavidmusicsearchdatadatabase.Country_codeQueries

/**
 * Always prefer to interacting with country codes through [AreaDao]
 * to maintain constraints.
 */
class CountryCodeDao(
    database: Database,
) : EntityDao {
    override val transacter: Country_codeQueries = database.country_codeQueries

    internal fun insert(countryCode: Country_code) {
        transacter.insert(countryCode)
    }

    internal fun insertCountryCodesForArea(
        areaId: String,
        countryCodes: List<String>?,
    ) {
        withTransaction {
            countryCodes?.forEach { countryCode ->
                insert(
                    Country_code(
                        area_id = areaId,
                        code = countryCode,
                    ),
                )
            }
        }
    }

    internal fun delete(areaId: String) {
        transacter.delete(areaId)
    }
}
