package ly.david.musicsearch.data.repository.list

import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.data.database.dao.AllEntitiesDao
import ly.david.musicsearch.shared.domain.list.ObserveLocalCountsOfAllEntities
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType

class ObserveLocalCountsOfAllEntitiesImpl(
    private val allEntitiesDao: AllEntitiesDao,
) : ObserveLocalCountsOfAllEntities {
    override fun invoke(): Flow<List<Pair<MusicBrainzEntityType, Long>>> {
        return allEntitiesDao.getCountsOfAllEntities()
    }
}
