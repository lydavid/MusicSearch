package ly.david.data.persistence

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ly.david.data.persistence.area.AreaRoomModel
import ly.david.data.persistence.area.Iso3166_1
import ly.david.data.persistence.area.ReleaseCountry
import ly.david.data.persistence.artist.ArtistCreditNameRoomModel
import ly.david.data.persistence.artist.ArtistCreditNamesWithResource
import ly.david.data.persistence.artist.ArtistCreditResource
import ly.david.data.persistence.artist.ArtistCreditRoomModel
import ly.david.data.persistence.artist.ArtistReleaseGroup
import ly.david.data.persistence.artist.ArtistRoomModel
import ly.david.data.persistence.event.EventRoomModel
import ly.david.data.persistence.history.LookupHistory
import ly.david.data.persistence.instrument.InstrumentRoomModel
import ly.david.data.persistence.label.LabelRoomModel
import ly.david.data.persistence.label.ReleaseLabel
import ly.david.data.persistence.place.PlaceRoomModel
import ly.david.data.persistence.recording.RecordingArtistCreditRoomModel
import ly.david.data.persistence.recording.RecordingRoomModel
import ly.david.data.persistence.recording.ReleaseRecording
import ly.david.data.persistence.relation.BrowseResourceOffset
import ly.david.data.persistence.relation.HasRelationsRoomModel
import ly.david.data.persistence.relation.RelationRoomModel
import ly.david.data.persistence.release.AreaWithReleaseDate
import ly.david.data.persistence.release.LabelWithCatalog
import ly.david.data.persistence.release.MediumRoomModel
import ly.david.data.persistence.release.ReleaseRoomModel
import ly.david.data.persistence.release.TrackRoomModel
import ly.david.data.persistence.releasegroup.ReleaseGroupRoomModel
import ly.david.data.persistence.work.RecordingWork
import ly.david.data.persistence.work.WorkRoomModel

@Database(
    version = 66,
    entities = [
        // Main tables
        ArtistRoomModel::class, ReleaseGroupRoomModel::class, ReleaseRoomModel::class,
        RecordingRoomModel::class, WorkRoomModel::class,
        AreaRoomModel::class, PlaceRoomModel::class,
        InstrumentRoomModel::class, LabelRoomModel::class,
        EventRoomModel::class,

        // Other tables
        ArtistCreditNameRoomModel::class, ArtistCreditRoomModel::class, ArtistCreditResource::class,
        MediumRoomModel::class, TrackRoomModel::class,
        Iso3166_1::class,

        // TODO: delete
        // Full-Text Search (FTS) tables
//        ReleaseGroupFts::class,

        // Relationship tables
        RelationRoomModel::class,
        HasRelationsRoomModel::class,
        BrowseResourceOffset::class,

        ArtistReleaseGroup::class,
        RecordingArtistCreditRoomModel::class,
        RecordingWork::class,
        ReleaseLabel::class,
        ReleaseCountry::class,
        ReleaseRecording::class,

        // Additional features tables
        LookupHistory::class
    ],
    views = [
        LabelWithCatalog::class,
        ArtistCreditNamesWithResource::class,
        AreaWithReleaseDate::class
    ],
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
        AutoMigration(from = 2, to = 3),
        AutoMigration(from = 3, to = 4),
        AutoMigration(from = 4, to = 5, spec = Migrations.RenameCountry::class),
        AutoMigration(from = 5, to = 6, spec = Migrations.RenameReleasesCountryToCountryCode::class),
        AutoMigration(from = 6, to = 7),
        AutoMigration(from = 7, to = 8),
        AutoMigration(from = 8, to = 9),
        AutoMigration(from = 9, to = 10),
        AutoMigration(from = 11, to = 12),
        AutoMigration(from = 12, to = 13, spec = Migrations.GeneralizeRecordingRelation::class),
        AutoMigration(from = 13, to = 14, spec = Migrations.RenameResourceId::class),
        AutoMigration(from = 14, to = 15, spec = Migrations.DeleteResource::class),
        AutoMigration(from = 15, to = 16),
        AutoMigration(from = 16, to = 17),
        AutoMigration(from = 17, to = 18),
        AutoMigration(from = 18, to = 19),
        AutoMigration(from = 19, to = 20),
        AutoMigration(from = 20, to = 21),
        AutoMigration(from = 21, to = 22),
        AutoMigration(from = 22, to = 23),
        AutoMigration(from = 23, to = 24),
        AutoMigration(from = 24, to = 25),
        AutoMigration(from = 25, to = 26),
        AutoMigration(from = 26, to = 27),
        AutoMigration(from = 27, to = 28, spec = Migrations.RenameHistorySummaryToTitle::class),
        AutoMigration(from = 28, to = 29),
        AutoMigration(from = 30, to = 31),
        AutoMigration(from = 31, to = 32, spec = Migrations.DeleteHasRelationsFromLabel::class),
        AutoMigration(from = 33, to = 34, spec = Migrations.DeleteHasRelationsFromReleaseGroup::class),
        AutoMigration(from = 35, to = 36, spec = Migrations.DeleteHasRelationsFromArea::class),
        AutoMigration(from = 37, to = 38, spec = Migrations.DeleteHasRelationsFromArtist::class),
        AutoMigration(from = 38, to = 39),
        AutoMigration(from = 39, to = 40),
        AutoMigration(from = 41, to = 43),
        AutoMigration(from = 44, to = 45),
        AutoMigration(from = 45, to = 46),
        AutoMigration(from = 46, to = 47),
        AutoMigration(from = 47, to = 48, spec = Migrations.DeleteReleaseCount::class),
        AutoMigration(from = 48, to = 49, spec = Migrations.DeleteReleasesReleaseGroups::class),
        AutoMigration(from = 49, to = 50),
        AutoMigration(from = 50, to = 51),
        AutoMigration(from = 51, to = 52),
        AutoMigration(from = 52, to = 53),
        AutoMigration(from = 53, to = 54),
        AutoMigration(from = 54, to = 55),
        AutoMigration(from = 55, to = 56),
        AutoMigration(from = 56, to = 57, spec = Migrations.RenameSortName::class),
        AutoMigration(from = 57, to = 58),
        AutoMigration(from = 58, to = 59),
        AutoMigration(from = 59, to = 60),
        AutoMigration(from = 60, to = 61),
        AutoMigration(from = 61, to = 62),
        AutoMigration(from = 62, to = 63, spec = Migrations.DropReleaseGroupsArtists::class),
        AutoMigration(from = 63, to = 64),
        AutoMigration(from = 64, to = 65),
        AutoMigration(from = 65, to = 66, spec = Migrations.DropReleasesArtists::class),
    ]
)
@TypeConverters(MusicBrainzRoomTypeConverters::class)
abstract class MusicBrainzRoomDatabase : RoomDatabase(), MusicBrainzDatabase
