package ly.david.data.persistence

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ly.david.data.room.MusicBrainzDatabase
import ly.david.data.room.area.AreaRoomModel
import ly.david.data.room.area.CountryCode
import ly.david.data.room.area.places.AreaPlace
import ly.david.data.room.area.releases.ReleaseCountry
import ly.david.data.room.artist.ArtistRoomModel
import ly.david.data.room.artist.UrlRelation
import ly.david.data.room.artist.credit.ArtistCredit
import ly.david.data.room.artist.credit.ArtistCreditNameRoomModel
import ly.david.data.room.artist.credit.ArtistCreditNamesWithResource
import ly.david.data.room.artist.credit.ArtistCreditResource
import ly.david.data.room.artist.releasegroups.ArtistReleaseGroup
import ly.david.data.room.artist.releases.ArtistRelease
import ly.david.data.room.collection.CollectionEntityRoomModel
import ly.david.data.room.collection.CollectionRoomModel
import ly.david.data.room.event.EventRoomModel
import ly.david.data.room.history.LookupHistoryRoomModel
import ly.david.data.room.history.search.SearchHistoryRoomModel
import ly.david.data.room.instrument.InstrumentRoomModel
import ly.david.data.room.label.LabelRoomModel
import ly.david.data.room.label.releases.ReleaseLabel
import ly.david.data.room.place.PlaceRoomModel
import ly.david.data.room.place.events.EventPlace
import ly.david.data.room.recording.RecordingRoomModel
import ly.david.data.room.recording.releases.RecordingRelease
import ly.david.data.room.relation.BrowseResourceCount
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

const val DATABASE_VERSION = 6

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
        ArtistCredit::class, ArtistCreditNameRoomModel::class, ArtistCreditResource::class,
        MediumRoomModel::class, TrackRoomModel::class,
        CountryCode::class,
        WorkAttributeRoomModel::class,

        // Relationship tables
        RelationRoomModel::class,
        HasRelations::class,
        HasUrls::class,
        BrowseResourceCount::class,

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

        CollectionRoomModel::class,
        CollectionEntityRoomModel::class
    ],
    views = [
        LabelWithCatalog::class,
        ArtistCreditNamesWithResource::class,
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
    ]
)
@TypeConverters(MusicBrainzRoomTypeConverters::class)
abstract class MusicBrainzRoomDatabase : RoomDatabase(), MusicBrainzDatabase
