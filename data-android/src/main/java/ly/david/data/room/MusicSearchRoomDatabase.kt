package ly.david.data.room

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ly.david.data.room.area.AreaRoomModel
import ly.david.data.room.area.CountryCode
import ly.david.data.room.area.places.AreaPlace
import ly.david.data.room.area.releases.ReleaseCountry
import ly.david.data.room.artist.ArtistRoomModel
import ly.david.data.room.artist.UrlRelation
import ly.david.data.room.artist.credit.ArtistCredit
import ly.david.data.room.artist.credit.ArtistCreditEntityLink
import ly.david.data.room.artist.credit.ArtistCreditNameRoomModel
import ly.david.data.room.artist.credit.ArtistCreditNamesWithEntity
import ly.david.data.room.artist.releasegroups.ArtistReleaseGroup
import ly.david.data.room.artist.releases.ArtistRelease
import ly.david.data.room.collection.CollectionEntityRoomModel
import ly.david.data.room.collection.CollectionRoomModel
import ly.david.data.room.event.EventRoomModel
import ly.david.data.room.history.LookupHistoryRoomModel
import ly.david.data.room.history.nowplaying.NowPlayingHistoryRoomModel
import ly.david.data.room.history.search.SearchHistoryRoomModel
import ly.david.data.room.image.MbidImage
import ly.david.data.room.instrument.InstrumentRoomModel
import ly.david.data.room.label.LabelRoomModel
import ly.david.data.room.label.releases.ReleaseLabel
import ly.david.data.room.place.PlaceRoomModel
import ly.david.data.room.place.events.EventPlace
import ly.david.data.room.recording.RecordingRoomModel
import ly.david.data.room.recording.releases.RecordingRelease
import ly.david.data.room.relation.BrowseEntityCount
import ly.david.data.room.relation.HasRelations
import ly.david.data.room.relation.HasUrls
import ly.david.data.room.relation.RelationRoomModel
import ly.david.data.room.release.AreaWithReleaseDate
import ly.david.data.room.release.LabelWithCatalog
import ly.david.data.room.release.ReleaseFormatTrackCount
import ly.david.data.room.release.ReleaseRoomModel
import ly.david.data.room.release.tracks.MediumRoomModel
import ly.david.data.room.release.tracks.TrackRoomModel
import ly.david.data.room.releasegroup.ReleaseGroupRoomModel
import ly.david.data.room.releasegroup.releases.ReleaseReleaseGroup
import ly.david.data.room.series.SeriesRoomModel
import ly.david.data.room.work.WorkAttributeRoomModel
import ly.david.data.room.work.WorkRoomModel
import ly.david.data.room.work.recordings.RecordingWork

const val DATABASE_VERSION = 18

@Database(
    version = DATABASE_VERSION,
    entities = [
        // Main tables
        ArtistRoomModel::class, ReleaseGroupRoomModel::class, ReleaseRoomModel::class,
        RecordingRoomModel::class, WorkRoomModel::class,
        AreaRoomModel::class, PlaceRoomModel::class,
        InstrumentRoomModel::class, LabelRoomModel::class,
        EventRoomModel::class,
        SeriesRoomModel::class,

        // Other tables
        ArtistCredit::class, ArtistCreditNameRoomModel::class, ArtistCreditEntityLink::class,
        MediumRoomModel::class, TrackRoomModel::class,
        CountryCode::class,
        WorkAttributeRoomModel::class,

        // Relationship tables
        RelationRoomModel::class,
        HasRelations::class,
        HasUrls::class,
        BrowseEntityCount::class,

        AreaPlace::class,
        ArtistRelease::class,
        EventPlace::class,
        ArtistReleaseGroup::class,
        RecordingWork::class,
        ReleaseCountry::class,
        ReleaseLabel::class,
        RecordingRelease::class,
        ReleaseReleaseGroup::class,

        // Additional features tables
        LookupHistoryRoomModel::class,
        SearchHistoryRoomModel::class,
        NowPlayingHistoryRoomModel::class,

        CollectionRoomModel::class,
        CollectionEntityRoomModel::class,

        MbidImage::class,
    ],
    views = [
        LabelWithCatalog::class,
        ArtistCreditNamesWithEntity::class,
        AreaWithReleaseDate::class,
        ReleaseFormatTrackCount::class,
        UrlRelation::class
    ],
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
        AutoMigration(from = 2, to = 3),
        AutoMigration(from = 3, to = 4),
        AutoMigration(from = 4, to = 5),
        AutoMigration(from = 5, to = 6),
        AutoMigration(from = 6, to = 7),
        AutoMigration(from = 8, to = 9, spec = Migrations.DeleteCoverArtPath::class),
        AutoMigration(from = 9, to = 10),
        AutoMigration(from = 10, to = 11, spec = Migrations.RenameThumbnailUrl::class),
        AutoMigration(from = 11, to = 12, spec = Migrations.RenameTablesToEntity::class),
        AutoMigration(from = 12, to = 13, spec = Migrations.RenameColumnsToEntity::class),
        AutoMigration(from = 13, to = 14, spec = Migrations.RenameColumnsToEntityPart2::class),
        AutoMigration(from = 14, to = 15, spec = Migrations.DeleteSearchHistoryId::class),
        AutoMigration(from = 15, to = 16),
        AutoMigration(from = 16, to = 17),
        AutoMigration(from = 17, to = 18),
    ]
)
@TypeConverters(RoomTypeConverters::class)
abstract class MusicSearchRoomDatabase : RoomDatabase(), MusicSearchDatabase
