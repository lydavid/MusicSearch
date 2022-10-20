package ly.david.data.persistence

import ly.david.data.persistence.area.AreaDao
import ly.david.data.persistence.area.ReleasesCountriesDao
import ly.david.data.persistence.artist.ArtistDao
import ly.david.data.persistence.artist.ReleaseGroupArtistDao
import ly.david.data.persistence.event.EventDao
import ly.david.data.persistence.history.LookupHistoryDao
import ly.david.data.persistence.instrument.InstrumentDao
import ly.david.data.persistence.label.LabelDao
import ly.david.data.persistence.label.ReleasesLabelsDao
import ly.david.data.persistence.place.PlaceDao
import ly.david.data.persistence.recording.RecordingDao
import ly.david.data.persistence.relation.RelationDao
import ly.david.data.persistence.release.MediumDao
import ly.david.data.persistence.release.ReleaseDao
import ly.david.data.persistence.release.TrackDao
import ly.david.data.persistence.releasegroup.ReleaseGroupDao
import ly.david.data.persistence.releasegroup.ReleasesReleaseGroupsDao
import ly.david.data.persistence.work.WorkDao

interface MusicBrainzDatabase {
    fun getArtistDao(): ArtistDao
    fun getReleaseGroupArtistDao(): ReleaseGroupArtistDao

    fun getReleaseGroupDao(): ReleaseGroupDao
    fun getReleasesReleaseGroupsDao(): ReleasesReleaseGroupsDao

    fun getReleaseDao(): ReleaseDao
    fun getMediumDao(): MediumDao
    fun getTrackDao(): TrackDao

    fun getRecordingDao(): RecordingDao

    fun getWorkDao(): WorkDao

    fun getAreaDao(): AreaDao
    fun getReleasesCountriesDao(): ReleasesCountriesDao

    fun getPlaceDao(): PlaceDao

    fun getInstrumentDao(): InstrumentDao

    fun getLabelDao(): LabelDao
    fun getReleasesLabelsDao(): ReleasesLabelsDao

    fun getEventDao(): EventDao

    fun getRelationDao(): RelationDao
    fun getLookupHistoryDao(): LookupHistoryDao
}
