package ly.david.data.test.api

import ly.david.musicsearch.data.musicbrainz.api.LookupApi
import ly.david.musicsearch.data.musicbrainz.models.core.AreaMusicBrainzNetworkModel
import ly.david.musicsearch.data.musicbrainz.models.core.ArtistMusicBrainzNetworkModel
import ly.david.musicsearch.data.musicbrainz.models.core.EventMusicBrainzNetworkModel
import ly.david.musicsearch.data.musicbrainz.models.core.GenreMusicBrainzNetworkModel
import ly.david.musicsearch.data.musicbrainz.models.core.InstrumentMusicBrainzNetworkModel
import ly.david.musicsearch.data.musicbrainz.models.core.LabelMusicBrainzNetworkModel
import ly.david.musicsearch.data.musicbrainz.models.core.PlaceMusicBrainzNetworkModel
import ly.david.musicsearch.data.musicbrainz.models.core.RecordingMusicBrainzNetworkModel
import ly.david.musicsearch.data.musicbrainz.models.core.ReleaseGroupMusicBrainzNetworkModel
import ly.david.musicsearch.data.musicbrainz.models.core.ReleaseMusicBrainzNetworkModel
import ly.david.musicsearch.data.musicbrainz.models.core.SeriesMusicBrainzNetworkModel
import ly.david.musicsearch.data.musicbrainz.models.core.WorkMusicBrainzNetworkModel

open class FakeLookupApi : LookupApi {
    override suspend fun lookupArea(
        areaId: String,
        include: String?,
    ): AreaMusicBrainzNetworkModel {
        return AreaMusicBrainzNetworkModel(
            id = "",
            name = "",
        )
    }

    override suspend fun lookupEvent(
        eventId: String,
        include: String?,
    ): EventMusicBrainzNetworkModel {
        return EventMusicBrainzNetworkModel(
            id = "",
            name = "",
        )
    }

    override suspend fun lookupGenre(
        genreId: String,
        include: String?,
    ): GenreMusicBrainzNetworkModel {
        return GenreMusicBrainzNetworkModel(
            id = "",
            name = "",
        )
    }

    override suspend fun lookupInstrument(
        instrumentId: String,
        include: String,
    ): InstrumentMusicBrainzNetworkModel {
        return InstrumentMusicBrainzNetworkModel(
            id = "",
            name = "",
        )
    }

    override suspend fun lookupLabel(
        labelId: String,
        include: String,
    ): LabelMusicBrainzNetworkModel {
        return LabelMusicBrainzNetworkModel(
            id = "",
            name = "",
        )
    }

    override suspend fun lookupPlace(
        placeId: String,
        include: String?,
    ): PlaceMusicBrainzNetworkModel {
        return PlaceMusicBrainzNetworkModel(
            id = "",
            name = "",
        )
    }

    override suspend fun lookupRecording(
        recordingId: String,
        include: String,
    ): RecordingMusicBrainzNetworkModel {
        return RecordingMusicBrainzNetworkModel(
            id = "",
            name = "",
        )
    }

    override suspend fun lookupRelease(
        releaseId: String,
        include: String,
    ): ReleaseMusicBrainzNetworkModel {
        return ReleaseMusicBrainzNetworkModel(
            id = "",
            name = "",
        )
    }

    override suspend fun lookupReleaseGroup(
        releaseGroupId: String,
        include: String,
    ): ReleaseGroupMusicBrainzNetworkModel {
        return ReleaseGroupMusicBrainzNetworkModel(
            id = "",
            name = "",
        )
    }

    override suspend fun lookupSeries(
        seriesId: String,
        include: String?,
    ): SeriesMusicBrainzNetworkModel {
        return SeriesMusicBrainzNetworkModel(
            id = "",
            name = "",
        )
    }

    override suspend fun lookupWork(
        workId: String,
        include: String?,
    ): WorkMusicBrainzNetworkModel {
        return WorkMusicBrainzNetworkModel(
            id = "",
            name = "",
        )
    }

    override suspend fun lookupArtist(
        artistId: String,
        include: String?,
    ): ArtistMusicBrainzNetworkModel {
        return ArtistMusicBrainzNetworkModel(
            id = "",
            name = "",
        )
    }
}
