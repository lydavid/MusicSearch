package ly.david.data.room

import ly.david.data.room.area.RoomAreaDao
import ly.david.data.room.area.releases.RoomReleaseCountryDao
import ly.david.data.room.artist.RoomArtistDao
import ly.david.data.room.artist.releasegroups.RoomArtistReleaseGroupDao
import ly.david.data.room.artist.releases.RoomArtistReleaseDao
import ly.david.data.room.collection.RoomCollectionDao
import ly.david.data.room.collection.RoomCollectionEntityDao
import ly.david.data.room.event.RoomEventDao
import ly.david.data.room.history.RoomLookupHistoryDao
import ly.david.data.room.history.nowplaying.NowPlayingHistoryDao
import ly.david.data.room.history.search.SearchHistoryDao
import ly.david.data.room.image.MbidImageDao
import ly.david.data.room.label.RoomLabelDao
import ly.david.data.room.label.releases.RoomReleaseLabelDao
import ly.david.data.room.place.RoomPlaceDao
import ly.david.data.room.recording.releases.RoomRecordingReleaseDao
import ly.david.data.room.relation.RoomRelationDao
import ly.david.data.room.release.RoomReleaseDao
import ly.david.data.room.release.tracks.RoomMediumDao
import ly.david.data.room.release.tracks.RoomTrackDao
import ly.david.data.room.releasegroup.RoomReleaseGroupDao
import ly.david.data.room.releasegroup.releases.RoomReleaseReleaseGroupDao

interface MusicSearchDatabase {
    fun getArtistDao(): RoomArtistDao
    fun getArtistReleaseDao(): RoomArtistReleaseDao
    fun getArtistReleaseGroupDao(): RoomArtistReleaseGroupDao
    fun getReleaseGroupDao(): RoomReleaseGroupDao
    fun getReleaseReleaseGroupDao(): RoomReleaseReleaseGroupDao

    fun getReleaseDao(): RoomReleaseDao
    fun getMediumDao(): RoomMediumDao
    fun getTrackDao(): RoomTrackDao

    fun getRecordingReleaseDao(): RoomRecordingReleaseDao

    fun getAreaDao(): RoomAreaDao
    fun getReleaseCountryDao(): RoomReleaseCountryDao

    fun getPlaceDao(): RoomPlaceDao

    fun getLabelDao(): RoomLabelDao
    fun getReleaseLabelDao(): RoomReleaseLabelDao

    fun getEventDao(): RoomEventDao

    fun getRelationDao(): RoomRelationDao
    fun getLookupHistoryDao(): RoomLookupHistoryDao
    fun getSearchHistoryDao(): SearchHistoryDao
    fun getNowPlayingHistoryDao(): NowPlayingHistoryDao

    fun getCollectionDao(): RoomCollectionDao
    fun getCollectionEntityDao(): RoomCollectionEntityDao

    fun getMbidImageDao(): MbidImageDao
}
