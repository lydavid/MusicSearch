package ly.david.mbjc.ui.place.events

import androidx.paging.PagingSource
import ly.david.musicsearch.data.core.network.MusicBrainzEntity
import ly.david.data.musicbrainz.EventMusicBrainzModel
import ly.david.data.musicbrainz.api.BrowseEventsResponse
import ly.david.data.musicbrainz.api.MusicBrainzApi
import ly.david.musicsearch.data.database.dao.BrowseEntityCountDao
import ly.david.musicsearch.data.database.dao.EventDao
import ly.david.musicsearch.data.database.dao.EventPlaceDao
import ly.david.musicsearch.data.core.listitem.EventListItemModel
import ly.david.musicsearch.domain.listitem.toEventListItemModel
import ly.david.ui.common.event.EventsPagedList
import ly.david.ui.common.paging.BrowseEntitiesByEntityViewModel
import lydavidmusicsearchdatadatabase.Event
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class EventsByPlaceViewModel(
    private val musicBrainzApi: MusicBrainzApi,
    private val eventPlaceDao: EventPlaceDao,
    private val eventDao: EventDao,
    private val browseEntityCountDao: BrowseEntityCountDao,
    pagedList: EventsPagedList,
) : BrowseEntitiesByEntityViewModel<Event, EventListItemModel, EventMusicBrainzModel, BrowseEventsResponse>(
    byEntity = MusicBrainzEntity.EVENT,
    browseEntityCountDao = browseEntityCountDao,
    pagedList = pagedList
) {

    override suspend fun browseEntitiesByEntity(entityId: String, offset: Int): BrowseEventsResponse {
        return musicBrainzApi.browseEventsByPlace(
            placeId = entityId,
            offset = offset
        )
    }

    override suspend fun insertAllLinkingModels(entityId: String, musicBrainzModels: List<EventMusicBrainzModel>) {
        eventDao.insertAll(musicBrainzModels)
        eventPlaceDao.insertAll(
            musicBrainzModels.map { event ->
                event.id to entityId
            }
        )
    }

    override suspend fun deleteLinkedEntitiesByEntity(entityId: String) {
        eventPlaceDao.withTransaction {
            eventPlaceDao.deleteEventsByPlace(entityId)
            browseEntityCountDao.deleteBrowseEntityCountByEntity(entityId, MusicBrainzEntity.EVENT)
        }
    }

    override fun getLinkedEntitiesPagingSource(
        entityId: String,
        query: String,
    ): PagingSource<Int, Event> =
        eventPlaceDao.getEventsByPlace(
            placeId = entityId,
            query = "%$query%"
        )

    override fun transformDatabaseToListItemModel(databaseModel: Event): EventListItemModel {
        return databaseModel.toEventListItemModel()
    }
}
