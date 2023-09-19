package ly.david.musicsearch.data.database.dao

import ly.david.musicsearch.data.database.Database
import lydavidmusicsearchdatadatabase.Country_code
import lydavidmusicsearchdatadatabase.Country_codeQueries

class CountryCodeDao(
    database: Database,
) : EntityDao {
    override val transacter: Country_codeQueries = database.country_codeQueries

    fun insert(countryCode: Country_code) {
        transacter.insert(countryCode)
    }

    fun insertCountryCodesForArea(
        areaId: String,
        countryCodes: List<String>,
    ) {
        withTransaction {
            countryCodes.forEach { countryCode ->
                insert(
                    Country_code(
                        area_id = areaId,
                        code = countryCode,
                    )
                )
            }
        }
    }

    fun getCountryCodesForArea(areaId: String): List<String> =
        transacter.getCountryCodesForArea(areaId).executeAsList()
}
