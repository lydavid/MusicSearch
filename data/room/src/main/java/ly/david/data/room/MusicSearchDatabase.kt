package ly.david.data.room

import ly.david.data.room.artist.releasegroups.RoomArtistReleaseGroupDao
import ly.david.data.room.artist.releases.RoomArtistReleaseDao
import ly.david.data.room.collection.RoomCollectionEntityDao
import ly.david.data.room.history.nowplaying.RoomNowPlayingHistoryDao
import ly.david.data.room.history.search.RoomSearchHistoryDao
import ly.david.data.room.image.MbidImageDao
import ly.david.data.room.label.releases.RoomReleaseLabelDao
import ly.david.data.room.recording.releases.RoomRecordingReleaseDao
import ly.david.data.room.relation.RoomRelationDao
import ly.david.data.room.releasegroup.releases.RoomReleaseReleaseGroupDao

interface MusicSearchDatabase {
    fun getArtistReleaseDao(): RoomArtistReleaseDao
    fun getArtistReleaseGroupDao(): RoomArtistReleaseGroupDao
    fun getReleaseReleaseGroupDao(): RoomReleaseReleaseGroupDao

    fun getRecordingReleaseDao(): RoomRecordingReleaseDao

    fun getReleaseLabelDao(): RoomReleaseLabelDao

    fun getRelationDao(): RoomRelationDao
    fun getSearchHistoryDao(): RoomSearchHistoryDao
    fun getNowPlayingHistoryDao(): RoomNowPlayingHistoryDao

    fun getCollectionEntityDao(): RoomCollectionEntityDao

    fun getMbidImageDao(): MbidImageDao
}
