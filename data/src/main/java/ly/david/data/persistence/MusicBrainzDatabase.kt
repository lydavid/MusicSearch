package ly.david.data.persistence

import ly.david.data.persistence.area.AreaDao
import ly.david.data.persistence.area.AreaPlaceDao
import ly.david.data.persistence.area.ReleaseCountryDao
import ly.david.data.persistence.artist.ArtistDao
import ly.david.data.persistence.artist.ArtistReleaseGroupDao
import ly.david.data.persistence.artist.release.ArtistReleaseDao
import ly.david.data.persistence.collection.CollectionDao
import ly.david.data.persistence.collection.CollectionEntityDao
import ly.david.data.persistence.event.EventDao
import ly.david.data.persistence.event.EventPlaceDao
import ly.david.data.persistence.history.LookupHistoryDao
import ly.david.data.persistence.instrument.InstrumentDao
import ly.david.data.persistence.label.LabelDao
import ly.david.data.persistence.label.ReleaseLabelDao
import ly.david.data.persistence.place.PlaceDao
import ly.david.data.persistence.recording.RecordingDao
import ly.david.data.persistence.recording.RecordingReleaseDao
import ly.david.data.persistence.relation.RelationDao
import ly.david.data.persistence.release.MediumDao
import ly.david.data.persistence.release.ReleaseDao
import ly.david.data.persistence.release.TrackDao
import ly.david.data.persistence.release.releasegroup.ReleaseReleaseGroupDao
import ly.david.data.persistence.releasegroup.ReleaseGroupDao
import ly.david.data.persistence.series.SeriesDao
import ly.david.data.persistence.work.RecordingWorkDao
import ly.david.data.persistence.work.WorkDao

interface MusicBrainzDatabase {
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

    fun getEventDao(): EventDao
    fun getSeriesDao(): SeriesDao

    fun getRelationDao(): RelationDao
    fun getLookupHistoryDao(): LookupHistoryDao

    fun getCollectionDao(): CollectionDao
    fun getCollectionEntityDao(): CollectionEntityDao
}
