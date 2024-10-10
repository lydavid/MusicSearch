package ly.david.musicsearch.data.database.dao

import ly.david.musicsearch.data.musicbrainz.models.core.AreaMusicBrainzModel
import ly.david.musicsearch.shared.domain.LifeSpanUiModel
import ly.david.musicsearch.shared.domain.area.AreaDetailsModel
import ly.david.musicsearch.data.database.Database
import lydavidmusicsearchdatadatabase.Area
import lydavidmusicsearchdatadatabase.AreaQueries

class AreaDao(
    database: Database,
    private val countryCodeDao: CountryCodeDao,
) : EntityDao {
    override val transacter: AreaQueries = database.areaQueries

    fun insert(area: AreaMusicBrainzModel?) {
        area?.run {
            transacter.insert(
                Area(
                    id = id,
                    name = name,
                    sort_name = sortName,
                    disambiguation = disambiguation,
                    type = type,
                    type_id = typeId,
                    begin = lifeSpan?.begin,
                    end = lifeSpan?.end,
                    ended = lifeSpan?.ended,
                ),
            )
            countryCodeDao.insertCountryCodesForArea(
                areaId = area.id,
                countryCodes = area.countryCodes.orEmpty(),
            )
        }
    }

    fun insertReplace(area: AreaMusicBrainzModel?) {
        area?.run {
            transacter.insertReplace(
                Area(
                    id = id,
                    name = name,
                    sort_name = sortName,
                    disambiguation = disambiguation,
                    type = type,
                    type_id = typeId,
                    begin = lifeSpan?.begin,
                    end = lifeSpan?.end,
                    ended = lifeSpan?.ended,
                ),
            )
            countryCodeDao.insertCountryCodesForArea(
                areaId = area.id,
                countryCodes = area.countryCodes.orEmpty(),
            )
        }
    }

    fun insertAll(areas: List<AreaMusicBrainzModel>) {
        transacter.transaction {
            areas.forEach { area ->
                insert(area)
            }
        }
    }

    fun getAreaForDetails(areaId: String): AreaDetailsModel? {
        return transacter.getArea(
            id = areaId,
            mapper = ::toDetailsModel,
        ).executeAsOneOrNull()
    }

    private fun toDetailsModel(
        id: String,
        name: String,
        disambiguation: String?,
        type: String?,
        begin: String?,
        end: String?,
        ended: Boolean?,
        countryCode: String?,
    ) = AreaDetailsModel(
        id = id,
        name = name,
        disambiguation = disambiguation,
        type = type,
        lifeSpan = LifeSpanUiModel(
            begin = begin,
            end = end,
            ended = ended,
        ),
        countryCodes = listOfNotNull(countryCode),
    )

    fun delete(areaId: String) {
        withTransaction {
            countryCodeDao.delete(areaId)
            transacter.delete(areaId)
        }
    }
}
