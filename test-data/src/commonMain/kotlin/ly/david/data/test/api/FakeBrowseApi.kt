package ly.david.data.test.api

import ly.david.musicsearch.data.musicbrainz.api.BrowseApi
import ly.david.musicsearch.data.musicbrainz.api.BrowseAreasResponse
import ly.david.musicsearch.data.musicbrainz.api.BrowseArtistsResponse
import ly.david.musicsearch.data.musicbrainz.api.BrowseEventsResponse
import ly.david.musicsearch.data.musicbrainz.api.BrowseGenresResponse
import ly.david.musicsearch.data.musicbrainz.api.BrowseInstrumentsResponse
import ly.david.musicsearch.data.musicbrainz.api.BrowseLabelsResponse
import ly.david.musicsearch.data.musicbrainz.api.BrowsePlacesResponse
import ly.david.musicsearch.data.musicbrainz.api.BrowseRecordingsResponse
import ly.david.musicsearch.data.musicbrainz.api.BrowseReleaseGroupsResponse
import ly.david.musicsearch.data.musicbrainz.api.BrowseReleasesResponse
import ly.david.musicsearch.data.musicbrainz.api.BrowseSeriesResponse
import ly.david.musicsearch.data.musicbrainz.api.BrowseWorksResponse
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity

open class FakeBrowseApi : BrowseApi {
    override suspend fun browseAreasByCollection(
        collectionId: String,
        limit: Int,
        offset: Int,
        include: String,
    ): BrowseAreasResponse {
        return BrowseAreasResponse(
            count = 0,
            offset = 0,
            musicBrainzModels = listOf(),
        )
    }

    override suspend fun browseArtistsByEntity(
        entityId: String,
        entity: MusicBrainzEntity,
        limit: Int,
        offset: Int,
        include: String,
    ): BrowseArtistsResponse {
        return BrowseArtistsResponse(
            count = 0,
            offset = 0,
            musicBrainzModels = listOf(),
        )
    }

    override suspend fun browseEventsByEntity(
        entityId: String,
        entity: MusicBrainzEntity,
        limit: Int,
        offset: Int,
        include: String,
    ): BrowseEventsResponse {
        return BrowseEventsResponse(
            count = 0,
            offset = 0,
            musicBrainzModels = listOf(),
        )
    }

    override suspend fun browseGenresByEntity(
        entityId: String,
        entity: MusicBrainzEntity,
        limit: Int,
        offset: Int,
    ): BrowseGenresResponse {
        return BrowseGenresResponse(
            count = 0,
            offset = 0,
            musicBrainzModels = listOf(),
        )
    }

    override suspend fun browseInstrumentsByCollection(
        collectionId: String,
        limit: Int,
        offset: Int,
        include: String,
    ): BrowseInstrumentsResponse {
        return BrowseInstrumentsResponse(
            count = 0,
            offset = 0,
            musicBrainzModels = listOf(),
        )
    }

    override suspend fun browseLabelsByEntity(
        entityId: String,
        entity: MusicBrainzEntity,
        limit: Int,
        offset: Int,
        include: String,
    ): BrowseLabelsResponse {
        return BrowseLabelsResponse(
            count = 0,
            offset = 0,
            musicBrainzModels = listOf(),
        )
    }

    override suspend fun browsePlacesByEntity(
        entityId: String,
        entity: MusicBrainzEntity,
        limit: Int,
        offset: Int,
        include: String,
    ): BrowsePlacesResponse {
        return BrowsePlacesResponse(
            count = 0,
            offset = 0,
            musicBrainzModels = listOf(),
        )
    }

    override suspend fun browseRecordingsByEntity(
        entityId: String,
        entity: MusicBrainzEntity,
        limit: Int,
        offset: Int,
        include: String,
    ): BrowseRecordingsResponse {
        return BrowseRecordingsResponse(
            count = 0,
            offset = 0,
            musicBrainzModels = listOf(),
        )
    }

    override suspend fun browseReleaseGroupsByEntity(
        entityId: String,
        entity: MusicBrainzEntity,
        limit: Int,
        offset: Int,
        include: String,
    ): BrowseReleaseGroupsResponse {
        return BrowseReleaseGroupsResponse(
            count = 0,
            offset = 0,
            musicBrainzModels = listOf(),
        )
    }

    override suspend fun browseSeriesByCollection(
        collectionId: String,
        limit: Int,
        offset: Int,
        include: String,
    ): BrowseSeriesResponse {
        return BrowseSeriesResponse(
            count = 0,
            offset = 0,
            musicBrainzModels = listOf(),
        )
    }

    override suspend fun browseWorksByEntity(
        entityId: String,
        entity: MusicBrainzEntity,
        limit: Int,
        offset: Int,
        include: String,
    ): BrowseWorksResponse {
        return BrowseWorksResponse(
            count = 0,
            offset = 0,
            musicBrainzModels = listOf(),
        )
    }

    override suspend fun browseReleasesByEntity(
        entityId: String,
        entity: MusicBrainzEntity,
        limit: Int,
        offset: Int,
        include: String,
    ): BrowseReleasesResponse {
        return BrowseReleasesResponse(
            count = 0,
            offset = 0,
            musicBrainzModels = listOf(),
        )
    }
}
