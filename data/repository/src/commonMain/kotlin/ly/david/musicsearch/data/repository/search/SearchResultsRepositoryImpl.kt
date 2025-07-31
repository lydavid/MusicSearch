package ly.david.musicsearch.data.repository.search

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingData
import app.cash.paging.RemoteMediator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import ly.david.musicsearch.data.database.dao.AliasDao
import ly.david.musicsearch.data.database.dao.AreaDao
import ly.david.musicsearch.data.database.dao.ArtistDao
import ly.david.musicsearch.data.database.dao.EventDao
import ly.david.musicsearch.data.database.dao.InstrumentDao
import ly.david.musicsearch.data.database.dao.LabelDao
import ly.david.musicsearch.data.database.dao.PlaceDao
import ly.david.musicsearch.data.database.dao.RecordingDao
import ly.david.musicsearch.data.database.dao.ReleaseDao
import ly.david.musicsearch.data.database.dao.ReleaseGroupDao
import ly.david.musicsearch.data.database.dao.SearchResultDao
import ly.david.musicsearch.data.database.dao.SeriesDao
import ly.david.musicsearch.data.database.dao.WorkDao
import ly.david.musicsearch.data.musicbrainz.api.SearchApi
import ly.david.musicsearch.data.musicbrainz.models.core.MusicBrainzNetworkModel
import ly.david.musicsearch.data.repository.internal.paging.CommonPagingConfig
import ly.david.musicsearch.data.repository.internal.paging.insertFooterItemForNonEmpty
import ly.david.musicsearch.data.repository.internal.paging.insertHeaderItemForNonEmpty
import ly.david.musicsearch.shared.domain.coroutine.CoroutineDispatchers
import ly.david.musicsearch.shared.domain.listitem.Footer
import ly.david.musicsearch.shared.domain.listitem.ListItemModel
import ly.david.musicsearch.shared.domain.listitem.SearchHeader
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.search.results.SearchResultsRepository

@OptIn(ExperimentalPagingApi::class)
internal class SearchResultsRepositoryImpl(
    private val searchApi: SearchApi,
    private val searchResultDao: SearchResultDao,
    private val areaDao: AreaDao,
    private val artistDao: ArtistDao,
    private val eventDao: EventDao,
    private val instrumentDao: InstrumentDao,
    private val labelDao: LabelDao,
    private val placeDao: PlaceDao,
    private val recordingDao: RecordingDao,
    private val releaseDao: ReleaseDao,
    private val releaseGroupDao: ReleaseGroupDao,
    private val seriesDao: SeriesDao,
    private val workDao: WorkDao,
    private val aliasDao: AliasDao,
    private val coroutineDispatchers: CoroutineDispatchers,
) : SearchResultsRepository {

    override fun observeSearchResults(
        entity: MusicBrainzEntity,
        query: String,
    ): Flow<PagingData<ListItemModel>> = Pager(
        config = CommonPagingConfig.pagingConfig,
        remoteMediator = getRemoteMediator(
            entity = entity,
            query = query,
            limit = CommonPagingConfig.pagingConfig.pageSize,
        ),
        pagingSourceFactory = {
            searchResultDao.getSearchResults(entity)
        },
    ).flow.map { pagingData ->
        pagingData
            .insertHeaderItemForNonEmpty(
                item = SearchHeader(remoteCount = searchResultDao.getMetadata()?.remoteCount ?: 0),
            )
            .insertFooterItemForNonEmpty(item = Footer())
    }

    private fun getRemoteMediator(
        entity: MusicBrainzEntity,
        query: String,
        limit: Int,
    ): RemoteMediator<Int, ListItemModel> {
        return SearchMusicBrainzRemoteMediator(
            searchResultDao = searchResultDao,
            entity = entity,
            query = query,
            fetchAndStore = { offset, removeAll ->
                fetchAndStore(
                    entity = entity,
                    query = query,
                    offset = offset,
                    limit = limit,
                    removeAll = removeAll,
                )
            },
        )
    }

    @Suppress("LongMethod")
    private suspend fun fetchAndStore(
        entity: MusicBrainzEntity,
        query: String,
        offset: Int,
        limit: Int,
        removeAll: () -> Unit,
    ): Int {
        return withContext(coroutineDispatchers.io) {
            when (entity) {
                MusicBrainzEntity.AREA -> {
                    val response = searchApi.queryAreas(
                        query = query,
                        offset = offset,
                        limit = limit,
                    )
                    val areas = response.areas
                    areaDao.withTransaction {
                        removeAll()
                        areaDao.insertReplaceAll(areas = areas)
                        insertIntoCommonTables(
                            entityType = entity,
                            entities = areas,
                            query = query,
                            offset = offset,
                            remoteCount = response.count,
                        )
                    }
                    areas.size
                }

                MusicBrainzEntity.ARTIST -> {
                    val response = searchApi.queryArtists(
                        query = query,
                        offset = offset,
                        limit = limit,
                    )
                    val artists = response.artists
                    artistDao.withTransaction {
                        removeAll()
                        artistDao.insertAll(artists)
                        insertIntoCommonTables(
                            entityType = entity,
                            entities = artists,
                            query = query,
                            offset = offset,
                            remoteCount = response.count,
                        )
                    }
                    artists.size
                }

                MusicBrainzEntity.EVENT -> {
                    val response = searchApi.queryEvents(
                        query = query,
                        offset = offset,
                        limit = limit,
                    )
                    val events = response.events
                    eventDao.withTransaction {
                        removeAll()
                        eventDao.insertAll(events)
                        insertIntoCommonTables(
                            entityType = entity,
                            entities = events,
                            query = query,
                            offset = offset,
                            remoteCount = response.count,
                        )
                    }
                    events.size
                }

                MusicBrainzEntity.INSTRUMENT -> {
                    val response = searchApi.queryInstruments(
                        query = query,
                        offset = offset,
                        limit = limit,
                    )
                    val instruments = response.instruments
                    instrumentDao.withTransaction {
                        removeAll()
                        instrumentDao.insertAll(instruments)
                        insertIntoCommonTables(
                            entityType = entity,
                            entities = instruments,
                            query = query,
                            offset = offset,
                            remoteCount = response.count,
                        )
                    }
                    instruments.size
                }

                MusicBrainzEntity.LABEL -> {
                    val response = searchApi.queryLabels(
                        query = query,
                        offset = offset,
                        limit = limit,
                    )
                    val labels = response.labels
                    labelDao.withTransaction {
                        removeAll()
                        labelDao.insertAll(labels)
                        insertIntoCommonTables(
                            entityType = entity,
                            entities = labels,
                            query = query,
                            offset = offset,
                            remoteCount = response.count,
                        )
                    }
                    labels.size
                }

                MusicBrainzEntity.PLACE -> {
                    val response = searchApi.queryPlaces(
                        query = query,
                        offset = offset,
                        limit = limit,
                    )
                    val places = response.places
                    placeDao.withTransaction {
                        removeAll()
                        placeDao.insertAll(places)
                        insertIntoCommonTables(
                            entityType = entity,
                            entities = places,
                            query = query,
                            offset = offset,
                            remoteCount = response.count,
                        )
                    }
                    places.size
                }

                MusicBrainzEntity.RECORDING -> {
                    val response = searchApi.queryRecordings(
                        query = query,
                        offset = offset,
                        limit = limit,
                    )
                    val recordings = response.recordings
                    recordingDao.withTransaction {
                        removeAll()
                        recordingDao.insertAll(recordings)
                        insertIntoCommonTables(
                            entityType = entity,
                            entities = recordings,
                            query = query,
                            offset = offset,
                            remoteCount = response.count,
                        )
                    }
                    recordings.size
                }

                MusicBrainzEntity.RELEASE -> {
                    val response = searchApi.queryReleases(
                        query = query,
                        offset = offset,
                        limit = limit,
                    )
                    val releases = response.releases
                    releaseDao.withTransaction {
                        removeAll()
                        releaseDao.insertAll(releases)
                        insertIntoCommonTables(
                            entityType = entity,
                            entities = releases,
                            query = query,
                            offset = offset,
                            remoteCount = response.count,
                        )
                    }
                    releases.size
                }

                MusicBrainzEntity.RELEASE_GROUP -> {
                    val response = searchApi.queryReleaseGroups(
                        query = query,
                        offset = offset,
                        limit = limit,
                    )
                    val releaseGroups = response.releaseGroups
                    releaseGroupDao.withTransaction {
                        removeAll()
                        releaseGroupDao.insertAllReleaseGroups(releaseGroups)
                        insertIntoCommonTables(
                            entityType = entity,
                            entities = releaseGroups,
                            query = query,
                            offset = offset,
                            remoteCount = response.count,
                        )
                    }
                    releaseGroups.size
                }

                MusicBrainzEntity.SERIES -> {
                    val response = searchApi.querySeries(
                        query = query,
                        offset = offset,
                        limit = limit,
                    )
                    val series = response.series
                    seriesDao.withTransaction {
                        removeAll()
                        seriesDao.insertAll(series)
                        insertIntoCommonTables(
                            entityType = entity,
                            entities = series,
                            query = query,
                            offset = offset,
                            remoteCount = response.count,
                        )
                    }
                    series.size
                }

                MusicBrainzEntity.WORK -> {
                    val response = searchApi.queryWorks(
                        query = query,
                        offset = offset,
                        limit = limit,
                    )
                    val works = response.works
                    workDao.withTransaction {
                        removeAll()
                        workDao.insertOrUpdateAll(works)
                        insertIntoCommonTables(
                            entityType = entity,
                            entities = works,
                            query = query,
                            offset = offset,
                            remoteCount = response.count,
                        )
                    }
                    works.size
                }

                MusicBrainzEntity.COLLECTION,
                MusicBrainzEntity.GENRE,
                MusicBrainzEntity.URL,
                -> {
                    error(IllegalStateException("Cannot search $entity"))
                }
            }
        }
    }

    private fun insertIntoCommonTables(
        entityType: MusicBrainzEntity,
        entities: List<MusicBrainzNetworkModel>,
        query: String,
        offset: Int,
        remoteCount: Int,
    ) {
        aliasDao.insertAll(
            musicBrainzNetworkModels = entities,
            // https://tickets.metabrainz.org/browse/SEARCH-746
            deleteExisting = !setOf(
                MusicBrainzEntity.RECORDING,
                MusicBrainzEntity.RELEASE,
                MusicBrainzEntity.RELEASE_GROUP,
            ).contains(entityType),
        )
        searchResultDao.insertAll(entities.map { it.id })
        searchResultDao.rewriteMetadata(
            entity = entityType,
            query = query,
            localCount = offset + entities.size,
            remoteCount = remoteCount,
        )
    }
}
