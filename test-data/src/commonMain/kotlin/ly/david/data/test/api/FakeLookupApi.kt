package ly.david.data.test.api

import ly.david.musicsearch.data.musicbrainz.api.LookupApi
import ly.david.musicsearch.data.musicbrainz.models.core.AreaMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.ArtistMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.EventMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.GenreMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.InstrumentMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.LabelMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.PlaceMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.RecordingMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.ReleaseGroupMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.ReleaseMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.SeriesMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.WorkMusicBrainzModel

open class FakeLookupApi : LookupApi {
    override suspend fun lookupArea(
        areaId: String,
        include: String?,
    ): AreaMusicBrainzModel {
        return AreaMusicBrainzModel(
            id = "",
            name = "",
        )
    }

    override suspend fun lookupEvent(
        eventId: String,
        include: String?,
    ): EventMusicBrainzModel {
        return EventMusicBrainzModel(
            id = "",
            name = "",
        )
    }

    override suspend fun lookupGenre(
        genreId: String,
        include: String?,
    ): GenreMusicBrainzModel {
        return GenreMusicBrainzModel(
            id = "",
            name = "",
        )
    }

    override suspend fun lookupInstrument(
        instrumentId: String,
        include: String,
    ): InstrumentMusicBrainzModel {
        return InstrumentMusicBrainzModel(
            id = "",
            name = "",
        )
    }

    override suspend fun lookupLabel(
        labelId: String,
        include: String,
    ): LabelMusicBrainzModel {
        return LabelMusicBrainzModel(
            id = "",
            name = "",
        )
    }

    override suspend fun lookupPlace(
        placeId: String,
        include: String?,
    ): PlaceMusicBrainzModel {
        return PlaceMusicBrainzModel(
            id = "",
            name = "",
        )
    }

    override suspend fun lookupRecording(
        recordingId: String,
        include: String,
    ): RecordingMusicBrainzModel {
        return RecordingMusicBrainzModel(
            id = "",
            name = "",
        )
    }

    override suspend fun lookupRelease(
        releaseId: String,
        include: String,
    ): ReleaseMusicBrainzModel {
        return ReleaseMusicBrainzModel(
            id = "",
            name = "",
        )
    }

    override suspend fun lookupReleaseGroup(
        releaseGroupId: String,
        include: String,
    ): ReleaseGroupMusicBrainzModel {
        return ReleaseGroupMusicBrainzModel(
            id = "",
            name = "",
        )
    }

    override suspend fun lookupSeries(
        seriesId: String,
        include: String?,
    ): SeriesMusicBrainzModel {
        return SeriesMusicBrainzModel(
            id = "",
            name = "",
        )
    }

    override suspend fun lookupWork(
        workId: String,
        include: String?,
    ): WorkMusicBrainzModel {
        return WorkMusicBrainzModel(
            id = "",
            name = "",
        )
    }

    override suspend fun lookupArtist(
        artistId: String,
        include: String?,
    ): ArtistMusicBrainzModel {
        return ArtistMusicBrainzModel(
            id = "",
            name = "",
        )
    }
}
