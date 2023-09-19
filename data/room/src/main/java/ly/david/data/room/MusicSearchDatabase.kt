package ly.david.data.room

import ly.david.data.room.area.RoomAreaDao
import ly.david.data.room.area.releases.ReleaseCountryDao
import ly.david.data.room.artist.RoomArtistDao
import ly.david.data.room.artist.releasegroups.ArtistReleaseGroupDao
import ly.david.data.room.artist.releases.ArtistReleaseDao
import ly.david.data.room.collection.RoomCollectionDao
import ly.david.data.room.collection.RoomCollectionEntityDao
import ly.david.data.room.event.RoomEventDao
import ly.david.data.room.history.LookupHistoryDao
import ly.david.data.room.history.nowplaying.NowPlayingHistoryDao
import ly.david.data.room.history.search.SearchHistoryDao
import ly.david.data.room.image.MbidImageDao
import ly.david.data.room.label.RoomLabelDao
import ly.david.data.room.label.releases.ReleaseLabelDao
import ly.david.data.room.place.RoomPlaceDao
import ly.david.data.room.recording.releases.RecordingReleaseDao
import ly.david.data.room.relation.RoomRelationDao
import ly.david.data.room.release.ReleaseDao
import ly.david.data.room.release.tracks.MediumDao
import ly.david.data.room.release.tracks.TrackDao
import ly.david.data.room.releasegroup.RoomReleaseGroupDao
import ly.david.data.room.releasegroup.releases.ReleaseReleaseGroupDao

interface MusicSearchDatabase {
    fun getArtistDao(): RoomArtistDao
    fun getArtistReleaseDao(): ArtistReleaseDao
    fun getArtistReleaseGroupDao(): ArtistReleaseGroupDao
    fun getReleaseGroupDao(): RoomReleaseGroupDao
    fun getReleaseReleaseGroupDao(): ReleaseReleaseGroupDao

    fun getReleaseDao(): ReleaseDao
    fun getMediumDao(): MediumDao
    fun getTrackDao(): TrackDao

    fun getRecordingReleaseDao(): RecordingReleaseDao

    fun getAreaDao(): RoomAreaDao
    fun getReleaseCountryDao(): ReleaseCountryDao

    fun getPlaceDao(): RoomPlaceDao

    fun getLabelDao(): RoomLabelDao
    fun getReleaseLabelDao(): ReleaseLabelDao

    fun getEventDao(): RoomEventDao

    fun getRelationDao(): RoomRelationDao
    fun getLookupHistoryDao(): LookupHistoryDao
    fun getSearchHistoryDao(): SearchHistoryDao
    fun getNowPlayingHistoryDao(): NowPlayingHistoryDao

    fun getCollectionDao(): RoomCollectionDao
    fun getCollectionEntityDao(): RoomCollectionEntityDao

    fun getMbidImageDao(): MbidImageDao
}
