package ly.david.data.persistence

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ly.david.data.persistence.area.AreaPlace
import ly.david.data.persistence.area.AreaRoomModel
import ly.david.data.persistence.area.CountryCode
import ly.david.data.persistence.area.ReleaseCountry
import ly.david.data.persistence.artist.ArtistReleaseGroup
import ly.david.data.persistence.artist.ArtistRoomModel
import ly.david.data.persistence.artist.credit.ArtistCredit
import ly.david.data.persistence.artist.credit.ArtistCreditNameRoomModel
import ly.david.data.persistence.artist.credit.ArtistCreditNamesWithResource
import ly.david.data.persistence.artist.credit.ArtistCreditResource
import ly.david.data.persistence.artist.release.ArtistRelease
import ly.david.data.persistence.collection.CollectionEntityRoomModel
import ly.david.data.persistence.collection.CollectionRoomModel
import ly.david.data.persistence.event.EventPlace
import ly.david.data.persistence.event.EventRoomModel
import ly.david.data.persistence.history.LookupHistoryRoomModel
import ly.david.data.persistence.instrument.InstrumentRoomModel
import ly.david.data.persistence.label.LabelRoomModel
import ly.david.data.persistence.label.ReleaseLabel
import ly.david.data.persistence.place.PlaceRoomModel
import ly.david.data.persistence.recording.RecordingRelease
import ly.david.data.persistence.recording.RecordingRoomModel
import ly.david.data.persistence.relation.BrowseResourceCount
import ly.david.data.persistence.relation.HasRelationsRoomModel
import ly.david.data.persistence.relation.RelationRoomModel
import ly.david.data.persistence.release.AreaWithReleaseDate
import ly.david.data.persistence.release.LabelWithCatalog
import ly.david.data.persistence.release.MediumRoomModel
import ly.david.data.persistence.release.ReleaseFormatTrackCount
import ly.david.data.persistence.release.ReleaseRoomModel
import ly.david.data.persistence.release.TrackRoomModel
import ly.david.data.persistence.release.releasegroup.ReleaseReleaseGroup
import ly.david.data.persistence.releasegroup.ReleaseGroupRoomModel
import ly.david.data.persistence.series.SeriesRoomModel
import ly.david.data.persistence.work.RecordingWork
import ly.david.data.persistence.work.WorkAttributeRoomModel
import ly.david.data.persistence.work.WorkRoomModel

const val DATABASE_VERSION = 2

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
        HasRelationsRoomModel::class,
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

        CollectionRoomModel::class,
        CollectionEntityRoomModel::class
    ],
    views = [
        LabelWithCatalog::class,
        ArtistCreditNamesWithResource::class,
        AreaWithReleaseDate::class,
        ReleaseFormatTrackCount::class,
    ],
    autoMigrations = [
        AutoMigration(from = 1, to = 2)
    ]
)
@TypeConverters(MusicBrainzRoomTypeConverters::class)
abstract class MusicBrainzRoomDatabase : RoomDatabase(), MusicBrainzDatabase
