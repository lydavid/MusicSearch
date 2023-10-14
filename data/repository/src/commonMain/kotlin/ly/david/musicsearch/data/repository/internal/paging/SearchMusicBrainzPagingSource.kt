package ly.david.musicsearch.data.repository.internal.paging

import app.cash.paging.PagingSource
import app.cash.paging.PagingState
import app.cash.paging.RemoteMediator
import kotlinx.coroutines.delay
import ly.david.data.common.network.RecoverableNetworkException
import ly.david.data.musicbrainz.AreaMusicBrainzModel
import ly.david.data.musicbrainz.ArtistMusicBrainzModel
import ly.david.data.musicbrainz.CoverArtArchiveMusicBrainzModel
import ly.david.data.musicbrainz.EventMusicBrainzModel
import ly.david.data.musicbrainz.GenreMusicBrainzModel
import ly.david.data.musicbrainz.InstrumentMusicBrainzModel
import ly.david.data.musicbrainz.LabelMusicBrainzModel
import ly.david.data.musicbrainz.LifeSpanMusicBrainzModel
import ly.david.data.musicbrainz.MusicBrainzModel
import ly.david.data.musicbrainz.PlaceMusicBrainzModel
import ly.david.data.musicbrainz.RecordingMusicBrainzModel
import ly.david.data.musicbrainz.ReleaseGroupMusicBrainzModel
import ly.david.data.musicbrainz.ReleaseMusicBrainzModel
import ly.david.data.musicbrainz.SeriesMusicBrainzModel
import ly.david.data.musicbrainz.TextRepresentationMusicBrainzModel
import ly.david.data.musicbrainz.WorkAttributeMusicBrainzModel
import ly.david.data.musicbrainz.WorkMusicBrainzModel
import ly.david.data.musicbrainz.api.DELAY_PAGED_API_CALLS_MS
import ly.david.data.musicbrainz.api.STARTING_OFFSET
import ly.david.data.musicbrainz.api.SearchApi
import ly.david.musicsearch.data.core.LifeSpanUiModel
import ly.david.musicsearch.data.core.artist.getDisplayNames
import ly.david.musicsearch.data.core.listitem.AreaListItemModel
import ly.david.musicsearch.data.core.listitem.ArtistListItemModel
import ly.david.musicsearch.data.core.listitem.EventListItemModel
import ly.david.musicsearch.data.core.listitem.GenreListItemModel
import ly.david.musicsearch.data.core.listitem.InstrumentListItemModel
import ly.david.musicsearch.data.core.listitem.LabelListItemModel
import ly.david.musicsearch.data.core.listitem.ListItemModel
import ly.david.musicsearch.data.core.listitem.PlaceListItemModel
import ly.david.musicsearch.data.core.listitem.RecordingListItemModel
import ly.david.musicsearch.data.core.listitem.ReleaseGroupListItemModel
import ly.david.musicsearch.data.core.listitem.ReleaseListItemModel
import ly.david.musicsearch.data.core.listitem.SeriesListItemModel
import ly.david.musicsearch.data.core.listitem.WorkListItemModel
import ly.david.musicsearch.data.core.network.MusicBrainzEntity
import ly.david.musicsearch.data.core.release.CoverArtArchiveUiModel
import ly.david.musicsearch.data.core.release.TextRepresentationUiModel
import ly.david.musicsearch.data.core.work.WorkAttributeUiModel

/**
 * This is not a [RemoteMediator] compared to [BrowseEntityRemoteMediator] and [LookupEntityRemoteMediator].
 *
 * We are not storing search results locally.
 * We want all search results to always be fresh.
 */
internal class SearchMusicBrainzPagingSource(
    private val searchApi: SearchApi,
    private val entity: MusicBrainzEntity,
    private val queryString: String,
) : PagingSource<Int, ListItemModel>() {

    override fun getRefreshKey(state: PagingState<Int, ListItemModel>): Int? {
        // We need to get the previous key (or next key if previous is null) of the page
        // that was closest to the most recently accessed index.
        // Anchor position is the most recently accessed index.
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListItemModel> {
        return try {
            val currentOffset = if (params is LoadParams.Refresh) {
                STARTING_OFFSET
            } else {
                delay(DELAY_PAGED_API_CALLS_MS)
                params.key ?: STARTING_OFFSET
            }

            val limit = params.loadSize
            val response = getQueryResults(
                searchApi = searchApi,
                entity = entity,
                queryString = queryString,
                currentOffset = currentOffset,
                limit = limit,
            )
            val searchResults = response.data
            val nextOffset = if (searchResults.size < limit) {
                null
            } else {
                currentOffset + searchResults.size
            }

            LoadResult.Page(
                data = searchResults.map { it.toListItemModel() },
                prevKey = if (currentOffset == STARTING_OFFSET) null else currentOffset,
                nextKey = nextOffset,
            )
        } catch (exception: RecoverableNetworkException) {
            LoadResult.Error(exception)
        }
    }

    private data class QueryResults(
        val offset: Int,
        val data: List<MusicBrainzModel>,
    )

    private suspend fun getQueryResults(
        searchApi: SearchApi,
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
                QueryResults(
                    queryArtists.offset,
                    queryArtists.artists,
                )
            }

            MusicBrainzEntity.RELEASE_GROUP -> {
                val queryReleaseGroups = searchApi.queryReleaseGroups(
                    query = queryString,
                    offset = currentOffset,
                    limit = limit,
                )
                QueryResults(
                    queryReleaseGroups.offset,
                    queryReleaseGroups.releaseGroups,
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
                    queryEvents.events,
                )
            }

            MusicBrainzEntity.SERIES -> {
                val queryEvents = searchApi.querySeries(
                    query = queryString,
                    offset = currentOffset,
                    limit = limit,
                )
                QueryResults(
                    queryEvents.offset,
                    queryEvents.series,
                )
            }

            // TODO: The following are not searchable. Is there a better model to switch on?
            MusicBrainzEntity.COLLECTION,
            MusicBrainzEntity.GENRE,
            MusicBrainzEntity.URL,
            -> {
                QueryResults(
                    offset = 0,
                    data = listOf(),
                )
            }
        }
    }
}

/**
 * Converts a [MusicBrainzModel] that we got from the network to its UI version for display.
 *
 * We can map a [MusicBrainzModel] to [ListItemModel] but not the other way around
 * because there are [ListItemModel] such as [EndOfList] that do not have a 1-to-1 mapping.
 * We could still do it, but the result will be nullable.
 *
 * It seems like this needs to be in the same directory as [ListItemModel] or else it tells us to add an else branch.
 */
internal fun MusicBrainzModel.toListItemModel(): ListItemModel {
    return when (this) {
        is ArtistMusicBrainzModel -> this.toArtistListItemModel()
        is ReleaseGroupMusicBrainzModel -> this.toReleaseGroupListItemModel()
        is ReleaseMusicBrainzModel -> this.toReleaseListItemModel()
        is RecordingMusicBrainzModel -> this.toRecordingListItemModel()
        is PlaceMusicBrainzModel -> this.toPlaceListItemModel()
        is AreaMusicBrainzModel -> this.toAreaListItemModel()
        is InstrumentMusicBrainzModel -> this.toInstrumentListItemModel()
        is LabelMusicBrainzModel -> this.toLabelListItemModel()
        is WorkMusicBrainzModel -> this.toWorkListItemModel()
        is EventMusicBrainzModel -> this.toEventListItemModel()
        is SeriesMusicBrainzModel -> this.toSeriesListItemModel()
        is GenreMusicBrainzModel -> this.toGenreListItemModel()
        else -> error(
            "Converting collection MusicBrainz models directly to list item models not supported.",
        )
    }
}

private fun AreaMusicBrainzModel.toAreaListItemModel(date: String? = null) = AreaListItemModel(
    id = id,
    name = name,
    sortName = sortName,
    disambiguation = disambiguation,
    type = type,
    lifeSpan = lifeSpan?.toLifeSpanUiModel(),
    countryCodes = countryCodes,
    date = date,
)

private fun ArtistMusicBrainzModel.toArtistListItemModel() =
    ArtistListItemModel(
        id = id,
        name = name,
        sortName = sortName,
        disambiguation = disambiguation,
        type = type,
        gender = gender,
        countryCode = countryCode,
        lifeSpan = lifeSpan?.toLifeSpanUiModel(),
    )

private fun EventMusicBrainzModel.toEventListItemModel() =
    EventListItemModel(
        id = id,
        name = name,
        disambiguation = disambiguation,
        type = type,
        time = time,
        cancelled = cancelled,
        lifeSpan = lifeSpan?.toLifeSpanUiModel(),
    )

private fun InstrumentMusicBrainzModel.toInstrumentListItemModel() =
    InstrumentListItemModel(
        id = id,
        name = name,
        disambiguation = disambiguation,
        description = description,
        type = type,
    )

private fun LabelMusicBrainzModel.toLabelListItemModel() =
    LabelListItemModel(
        id = id,
        name = name,
        disambiguation = disambiguation,
        type = type,
        labelCode = labelCode,
    )

private fun PlaceMusicBrainzModel.toPlaceListItemModel() =
    PlaceListItemModel(
        id = id,
        name = name,
        disambiguation = disambiguation,
        address = address,
        type = type,
        coordinates = coordinates,
        lifeSpan = lifeSpan?.toLifeSpanUiModel(),
        area = area?.toAreaListItemModel(),
    )

private fun RecordingMusicBrainzModel.toRecordingListItemModel() = RecordingListItemModel(
    id = id,
    name = name,
    firstReleaseDate = firstReleaseDate,
    disambiguation = disambiguation,
    length = length,
    video = video ?: false,
    formattedArtistCredits = artistCredits.getDisplayNames(),
)

private fun ReleaseGroupMusicBrainzModel.toReleaseGroupListItemModel(): ReleaseGroupListItemModel {
    return ReleaseGroupListItemModel(
        id = id,
        name = name,
        firstReleaseDate = firstReleaseDate,
        disambiguation = disambiguation,
        primaryType = primaryType,
        secondaryTypes = secondaryTypes,
        formattedArtistCredits = artistCredits.getDisplayNames(),
    )
}

private fun ReleaseMusicBrainzModel.toReleaseListItemModel() = ReleaseListItemModel(
    id = id,
    name = name,
    disambiguation = disambiguation,
    date = date,
    status = status,
    barcode = barcode,
    statusId = statusId,
    countryCode = countryCode,
    packaging = packaging,
    packagingId = packagingId,
    coverArtArchive = coverArtArchive.toCoverArtArchiveUiModel(),
    textRepresentation = textRepresentation?.toTextRepresentationUiModel(),
    asin = asin,
    quality = quality,
    imageUrl = null,
    formattedArtistCredits = artistCredits.getDisplayNames(),
    releaseCountryCount = releaseEvents?.count() ?: 0,
)

private fun SeriesMusicBrainzModel.toSeriesListItemModel() =
    SeriesListItemModel(
        id = id,
        name = name,
        disambiguation = disambiguation,
        type = type,
    )

private fun WorkMusicBrainzModel.toWorkListItemModel() =
    WorkListItemModel(
        id = id,
        name = name,
        disambiguation = disambiguation,
        type = type,
        language = language,
        iswcs = iswcs,
        attributes = attributes?.map { it.toWorkAttributeUiModel() }.orEmpty(),
    )

private fun LifeSpanMusicBrainzModel.toLifeSpanUiModel() = LifeSpanUiModel(
    begin = begin,
    end = end,
    ended = ended,
)

private fun TextRepresentationMusicBrainzModel.toTextRepresentationUiModel() = TextRepresentationUiModel(
    script = script,
    language = language,
)

private fun CoverArtArchiveMusicBrainzModel.toCoverArtArchiveUiModel() = CoverArtArchiveUiModel(
    count = count,
)

private fun WorkAttributeMusicBrainzModel.toWorkAttributeUiModel() =
    WorkAttributeUiModel(
        type = type,
        typeId = typeId,
        value = value,
    )

private fun GenreMusicBrainzModel.toGenreListItemModel() =
    GenreListItemModel(
        id = id,
        name = name,
        disambiguation = disambiguation,
    )
