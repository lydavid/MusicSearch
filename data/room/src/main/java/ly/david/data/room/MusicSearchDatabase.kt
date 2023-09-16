package ly.david.data.room

import ly.david.data.room.area.AreaDao
import ly.david.data.room.area.places.AreaPlaceDao
import ly.david.data.room.area.releases.ReleaseCountryDao
import ly.david.data.room.artist.ArtistDao
import ly.david.data.room.artist.releasegroups.ArtistReleaseGroupDao
import ly.david.data.room.artist.releases.ArtistReleaseDao
import ly.david.data.room.collection.CollectionDao
import ly.david.data.room.collection.CollectionEntityDao
import ly.david.data.room.event.RoomEventDao
import ly.david.data.room.history.LookupHistoryDao
import ly.david.data.room.history.nowplaying.NowPlayingHistoryDao
import ly.david.data.room.history.search.SearchHistoryDao
import ly.david.data.room.image.MbidImageDao
import ly.david.data.room.instrument.InstrumentDao
import ly.david.data.room.label.LabelDao
import ly.david.data.room.label.releases.ReleaseLabelDao
import ly.david.data.room.place.PlaceDao
import ly.david.data.room.place.events.EventPlaceDao
import ly.david.data.room.recording.RecordingDao
import ly.david.data.room.recording.releases.RecordingReleaseDao
import ly.david.data.room.relation.RoomRelationDao
import ly.david.data.room.release.ReleaseDao
import ly.david.data.room.release.tracks.MediumDao
import ly.david.data.room.release.tracks.TrackDao
import ly.david.data.room.releasegroup.ReleaseGroupDao
import ly.david.data.room.releasegroup.releases.ReleaseReleaseGroupDao
import ly.david.data.room.series.SeriesDao
import ly.david.data.room.work.WorkDao
import ly.david.data.room.work.recordings.RecordingWorkDao

interface MusicSearchDatabase {
    fun getArtistDao(): ArtistDao
    fun getArtistReleaseDao(): ArtistReleaseDao
    fun getArtistReleaseGroupDao(): ArtistReleaseGroupDao
    fun getReleaseGroupDao(): ReleaseGroupDao
    fun getReleaseReleaseGroupDao(): ReleaseReleaseGroupDao

    fun getReleaseDao(): ReleaseDao
    fun getMediumDao(): MediumDao
    fun getTrackDao(): TrackDao

    fun getRecordingDao(): RecordingDao
    fun getRecordingReleaseDao(): RecordingReleaseDao

    fun getWorkDao(): WorkDao
    fun getRecordingWorkDao(): RecordingWorkDao

    fun getAreaDao(): AreaDao
    fun getAreaPlaceDao(): AreaPlaceDao
    fun getEventPlaceDao(): EventPlaceDao
    fun getReleaseCountryDao(): ReleaseCountryDao

    fun getPlaceDao(): PlaceDao

    fun getInstrumentDao(): InstrumentDao

    fun getLabelDao(): LabelDao
    fun getReleaseLabelDao(): ReleaseLabelDao

    fun getEventDao(): RoomEventDao
    fun getSeriesDao(): SeriesDao

    fun getRelationDao(): RoomRelationDao
    fun getLookupHistoryDao(): LookupHistoryDao
    fun getSearchHistoryDao(): SearchHistoryDao
    fun getNowPlayingHistoryDao(): NowPlayingHistoryDao

    fun getCollectionDao(): CollectionDao
    fun getCollectionEntityDao(): CollectionEntityDao

    fun getMbidImageDao(): MbidImageDao
}
