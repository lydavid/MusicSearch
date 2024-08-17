package ly.david.musicsearch.data.repository.area

import ly.david.musicsearch.data.musicbrainz.models.core.AreaMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.api.MusicBrainzApi
import ly.david.musicsearch.shared.domain.area.AreaDetailsModel
import ly.david.musicsearch.data.database.dao.AreaDao
import ly.david.musicsearch.data.database.dao.CountryCodeDao
import ly.david.musicsearch.data.repository.internal.toRelationWithOrderList
import ly.david.musicsearch.shared.domain.area.AreaRepository
import ly.david.musicsearch.shared.domain.relation.RelationRepository

class AreaRepositoryImpl(
    private val musicBrainzApi: MusicBrainzApi,
    private val areaDao: AreaDao,
    private val countryCodeDao: CountryCodeDao,
    private val relationRepository: RelationRepository,
) : AreaRepository {

    /**
     * Returns area for display.
     *
     * Lookup area, and stores all relevant data.
     */
    override suspend fun lookupArea(areaId: String): AreaDetailsModel {
        val area = areaDao.getAreaForDetails(areaId)
        val countryCodes: List<String> = countryCodeDao.getCountryCodesForArea(areaId)
        val urlRelations = relationRepository.getEntityUrlRelationships(areaId)
        val hasUrlsBeenSavedForEntity = relationRepository.hasUrlsBeenSavedFor(areaId)
        if (area != null && hasUrlsBeenSavedForEntity) {
            return area.copy(
                countryCodes = countryCodes,
                urls = urlRelations,
            )
        }

        val areaMusicBrainzModel = musicBrainzApi.lookupArea(areaId)
        cache(areaMusicBrainzModel)
        return lookupArea(areaId)
    }

    private fun cache(area: AreaMusicBrainzModel) {
        areaDao.withTransaction {
            areaDao.insert(area)
            countryCodeDao.insertCountryCodesForArea(
                areaId = area.id,
                countryCodes = area.countryCodes.orEmpty(),
            )

            val relationWithOrderList = area.relations.toRelationWithOrderList(area.id)
            relationRepository.insertAllUrlRelations(
                entityId = area.id,
                relationWithOrderList = relationWithOrderList,
            )
        }
    }
}
