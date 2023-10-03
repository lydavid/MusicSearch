package ly.david.musicsearch.domain.area

import ly.david.data.musicbrainz.AreaMusicBrainzModel
import ly.david.data.musicbrainz.RelationMusicBrainzModel
import ly.david.data.musicbrainz.api.LookupApi.Companion.INC_ALL_RELATIONS_EXCEPT_URLS
import ly.david.data.musicbrainz.api.MusicBrainzApi
import ly.david.musicsearch.data.database.dao.AreaDao
import ly.david.musicsearch.data.database.dao.CountryCodeDao
import ly.david.musicsearch.domain.RelationsListRepository
import ly.david.musicsearch.domain.relation.RelationRepository
import org.koin.core.annotation.Single

@Single
class AreaRepository(
    private val musicBrainzApi: MusicBrainzApi,
    private val areaDao: AreaDao,
    private val countryCodeDao: CountryCodeDao,
    private val relationRepository: RelationRepository,
) : RelationsListRepository {

    /**
     * Returns area for display.
     *
     * Lookup area, and stores all relevant data.
     */
    suspend fun lookupArea(areaId: String): AreaScaffoldModel {
        val area = areaDao.getArea(areaId)
        val countryCodes: List<String> = countryCodeDao.getCountryCodesForArea(areaId)
        val urlRelations = relationRepository.getEntityUrlRelationships(areaId)
        val hasUrlsBeenSavedForEntity = relationRepository.hasUrlsBeenSavedFor(areaId)
        if (area != null && hasUrlsBeenSavedForEntity) {
            return area.toAreaScaffoldModel(
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
                countryCodes = area.countryCodes.orEmpty()
            )
            relationRepository.insertAllUrlRelations(
                entityId = area.id,
                relationMusicBrainzModels = area.relations,
            )
        }
    }

    override suspend fun lookupRelationsFromNetwork(entityId: String): List<RelationMusicBrainzModel>? {
        return musicBrainzApi.lookupArea(
            areaId = entityId,
            include = INC_ALL_RELATIONS_EXCEPT_URLS,
        ).relations
    }
}
