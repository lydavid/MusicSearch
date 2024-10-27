package ly.david.musicsearch.data.repository.internal.paging

import androidx.paging.ExperimentalPagingApi
import app.cash.paging.LoadType
import app.cash.paging.PagingState
import app.cash.paging.RemoteMediator
import kotlinx.coroutines.delay
import ly.david.musicsearch.data.database.dao.ArtistDao
import ly.david.musicsearch.data.database.dao.SearchResultDao
import ly.david.musicsearch.data.musicbrainz.DELAY_PAGED_API_CALLS_MS
import ly.david.musicsearch.data.musicbrainz.SEARCH_BROWSE_LIMIT
import ly.david.musicsearch.data.musicbrainz.api.SearchApi
import ly.david.musicsearch.data.musicbrainz.models.core.MusicBrainzModel
import ly.david.musicsearch.shared.domain.listitem.ListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity

/**
 * Compared with [BrowseEntityRemoteMediator] and [LookupEntityRemoteMediator].
 */
@ExperimentalPagingApi
internal class SearchMusicBrainzRemoteMediator(
    private val searchApi: SearchApi,
    private val searchResultDao: SearchResultDao,
    private val artistDao: ArtistDao,
    private val entity: MusicBrainzEntity,
    private val queryString: String,
) : RemoteMediator<Int, ListItemModel>() {

    override suspend fun initialize(): InitializeAction {
        val metadata = searchResultDao.getMetadata()
        return if (metadata == null || metadata.query != queryString || metadata.entity != entity) {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        } else {
            InitializeAction.SKIP_INITIAL_REFRESH
        }
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ListItemModel>,
    ): MediatorResult {
        return try {
            val nextOffset: Int = when (loadType) {
                LoadType.REFRESH -> {
                    searchResultDao.removeAll()
                    0
                }

                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)

                LoadType.APPEND -> {
                    val localCount = searchResultDao.getLocalCount().toInt()
                    val remoteCount = searchResultDao.getMetadata()?.remoteCount

                    if (localCount == remoteCount) {
                        return MediatorResult.Success(endOfPaginationReached = true)
                    }

                    delay(DELAY_PAGED_API_CALLS_MS)
                    // TODO: this count doesn't always match up due to dupe results showing up
                    //  so we should join a different table from our normal entity tables
                    localCount
                }
            }

            val response = getQueryResults(
                entity = entity,
                queryString = queryString,
                currentOffset = nextOffset,
                limit = 100,
            )

            MediatorResult.Success(
                endOfPaginationReached = response.data.size < SEARCH_BROWSE_LIMIT,
            )
        } catch (ex: Exception) {
            MediatorResult.Error(ex)
        }
    }

    private data class QueryResults(
        val offset: Int,
        val count: Int,
        val data: List<MusicBrainzModel>,
    )

    private suspend fun getQueryResults(
        entity: MusicBrainzEntity,
        queryString: String,
        currentOffset: Int,
        limit: Int,
    ): QueryResults {
        return when (entity) {
            MusicBrainzEntity.ARTIST -> {
                val queryArtists = searchApi.queryArtists(
                    query = queryString,
                    offset = currentOffset,
                    limit = limit,
                )
                artistDao.withTransaction {
                    artistDao.insertAll(queryArtists.artists)
                    searchResultDao.insertAll(queryArtists.artists.map { it.id })
                    // TODO: don't need to do this on append
                    searchResultDao.rewriteMetadata(
                        entity = entity,
                        query = queryString,
                        count = queryArtists.count
                    )
                }
                QueryResults(
                    offset = queryArtists.offset,
                    count = queryArtists.count,
                    data = queryArtists.artists,
                )
            }

            MusicBrainzEntity.RELEASE_GROUP -> {
                val queryReleaseGroups = searchApi.queryReleaseGroups(
                    query = queryString,
                    offset = currentOffset,
                    limit = limit,
                )
                // TODO: store rest of entities
                QueryResults(
                    offset = queryReleaseGroups.offset,
                    count = queryReleaseGroups.count,
                    data = queryReleaseGroups.releaseGroups,
                )
            }

            MusicBrainzEntity.RELEASE -> {
                val queryReleases = searchApi.queryReleases(
                    query = queryString,
                    offset = currentOffset,
                    limit = limit,
                )
                QueryResults(
                    queryReleases.offset,
                    queryReleases.count,
                    queryReleases.releases,
                )
            }

            MusicBrainzEntity.RECORDING -> {
                val queryRecordings = searchApi.queryRecordings(
                    query = queryString,
                    offset = currentOffset,
                    limit = limit,
                )
                QueryResults(
                    queryRecordings.offset,
                    queryRecordings.count,
                    queryRecordings.recordings,
                )
            }

            MusicBrainzEntity.WORK -> {
                val queryWorks = searchApi.queryWorks(
                    query = queryString,
                    offset = currentOffset,
                    limit = limit,
                )
                QueryResults(
                    queryWorks.offset,
                    queryWorks.count,
                    queryWorks.works,
                )
            }

            MusicBrainzEntity.AREA -> {
                val queryAreas = searchApi.queryAreas(
                    query = queryString,
                    offset = currentOffset,
                    limit = limit,
                )
                QueryResults(
                    queryAreas.offset,
                    queryAreas.count,
                    queryAreas.areas,
                )
            }

            MusicBrainzEntity.PLACE -> {
                val queryPlaces = searchApi.queryPlaces(
                    query = queryString,
                    offset = currentOffset,
                    limit = limit,
                )
                QueryResults(
                    queryPlaces.offset,
                    queryPlaces.count,
                    queryPlaces.places,
                )
            }

            MusicBrainzEntity.INSTRUMENT -> {
                val queryInstruments = searchApi.queryInstruments(
                    query = queryString,
                    offset = currentOffset,
                    limit = limit,
                )
                QueryResults(
                    queryInstruments.offset,
                    queryInstruments.count,
                    queryInstruments.instruments,
                )
            }

            MusicBrainzEntity.LABEL -> {
                val queryLabels = searchApi.queryLabels(
                    query = queryString,
                    offset = currentOffset,
                    limit = limit,
                )
                QueryResults(
                    queryLabels.offset,
                    queryLabels.count,
                    queryLabels.labels,
                )
            }

            MusicBrainzEntity.EVENT -> {
                val queryEvents = searchApi.queryEvents(
                    query = queryString,
                    offset = currentOffset,
                    limit = limit,
                )
                QueryResults(
                    queryEvents.offset,
                    queryEvents.count,
                    queryEvents.events,
                )
            }

            MusicBrainzEntity.SERIES -> {
                val series = searchApi.querySeries(
                    query = queryString,
                    offset = currentOffset,
                    limit = limit,
                )
                QueryResults(
                    series.offset,
                    series.count,
                    series.series,
                )
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
