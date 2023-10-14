package ly.david.musicsearch.data.repository

import ly.david.data.musicbrainz.AreaMusicBrainzModel
import ly.david.data.musicbrainz.api.MusicBrainzApi
import ly.david.musicsearch.data.core.area.AreaScaffoldModel
import ly.david.musicsearch.data.database.dao.AreaDao
import ly.david.musicsearch.data.database.dao.CountryCodeDao
import ly.david.musicsearch.data.repository.internal.toRelationWithOrderList
import ly.david.musicsearch.domain.area.AreaRepository
import ly.david.musicsearch.domain.relation.RelationRepository

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
    override suspend fun lookupArea(areaId: String): AreaScaffoldModel {
        val area = areaDao.getArea(areaId)
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
                countryCodes = area.countryCodes.orEmpty()
            )

            val relationWithOrderList = area.relations.toRelationWithOrderList(area.id)
            relationRepository.insertAllUrlRelations(
                entityId = area.id,
                relationWithOrderList = relationWithOrderList,
            )
        }
    }

//    override suspend fun lookupRelationsFromNetwork(entityId: String): List<RelationWithOrder>? {
//        val relations = musicBrainzApi.lookupArea(
//            areaId = entityId,
//            include = LookupApi.INC_ALL_RELATIONS_EXCEPT_URLS,
//        ).relations
//        val relationWithOrderList = relations?.mapIndexedNotNull { index, relationMusicBrainzModel ->
//            relationMusicBrainzModel.toRelationDatabaseModel(
//                entityId = entityId,
//                order = index,
//            )
//        }
//        return relationWithOrderList
//    }
}
