package ly.david.musicsearch.data.repository.helpers

import ly.david.data.test.api.FakeLookupApi
import ly.david.musicsearch.data.database.dao.EntityHasRelationsDao
import ly.david.musicsearch.data.database.dao.EventDao
import ly.david.musicsearch.data.database.dao.RelationDao
import ly.david.musicsearch.data.musicbrainz.models.core.EventMusicBrainzNetworkModel
import ly.david.musicsearch.data.repository.RelationRepositoryImpl
import ly.david.musicsearch.data.repository.event.EventRepositoryImpl
import ly.david.musicsearch.shared.domain.event.EventRepository
import ly.david.musicsearch.shared.domain.history.DetailsMetadataDao

interface TestEventRepository {
    val entityHasRelationsDao: EntityHasRelationsDao
    val visitedDao: DetailsMetadataDao
    val relationDao: RelationDao
    val eventDao: EventDao

    fun createEventRepository(
        musicBrainzModel: EventMusicBrainzNetworkModel,
    ): EventRepository {
        val relationRepository = RelationRepositoryImpl(
            lookupApi = object : FakeLookupApi() {
                override suspend fun lookupEvent(
                    eventId: String,
                    include: String?,
                ): EventMusicBrainzNetworkModel {
                    return musicBrainzModel
                }
            },
            entityHasRelationsDao = entityHasRelationsDao,
            detailsMetadataDao = visitedDao,
            relationDao = relationDao,
        )
        return EventRepositoryImpl(
            eventDao = eventDao,
            relationRepository = relationRepository,
            lookupApi = object : FakeLookupApi() {
                override suspend fun lookupEvent(
                    eventId: String,
                    include: String?,
                ): EventMusicBrainzNetworkModel {
                    return musicBrainzModel
                }
            },
        )
    }
}
