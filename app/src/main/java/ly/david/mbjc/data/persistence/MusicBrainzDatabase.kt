package ly.david.mbjc.data.persistence

import ly.david.mbjc.data.persistence.area.AreaDao
import ly.david.mbjc.data.persistence.area.ReleasesCountriesDao
import ly.david.mbjc.data.persistence.artist.ArtistDao
import ly.david.mbjc.data.persistence.artist.ReleaseGroupArtistDao
import ly.david.mbjc.data.persistence.event.EventDao
import ly.david.mbjc.data.persistence.history.LookupHistoryDao
import ly.david.mbjc.data.persistence.instrument.InstrumentDao
import ly.david.mbjc.data.persistence.label.LabelDao
import ly.david.mbjc.data.persistence.label.ReleasesLabelsDao
import ly.david.mbjc.data.persistence.place.PlaceDao
import ly.david.mbjc.data.persistence.recording.RecordingDao
import ly.david.mbjc.data.persistence.relation.RelationDao
import ly.david.mbjc.data.persistence.release.MediumDao
import ly.david.mbjc.data.persistence.release.ReleaseDao
import ly.david.mbjc.data.persistence.release.TrackDao
import ly.david.mbjc.data.persistence.releasegroup.ReleaseGroupDao
import ly.david.mbjc.data.persistence.releasegroup.ReleasesReleaseGroupsDao
import ly.david.mbjc.data.persistence.work.WorkDao

internal interface MusicBrainzDatabase {
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
