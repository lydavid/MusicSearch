package ly.david.mbjc.data.persistence

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.DeleteColumn
import androidx.room.RenameColumn
import androidx.room.RenameTable
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.AutoMigrationSpec
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import ly.david.mbjc.data.persistence.area.AreaDao
import ly.david.mbjc.data.persistence.area.AreaRoomModel
import ly.david.mbjc.data.persistence.artist.ArtistDao
import ly.david.mbjc.data.persistence.artist.ArtistRoomModel
import ly.david.mbjc.data.persistence.artist.ReleaseGroupArtistCreditRoomModel
import ly.david.mbjc.data.persistence.artist.ReleaseGroupArtistDao
import ly.david.mbjc.data.persistence.event.EventDao
import ly.david.mbjc.data.persistence.event.EventRoomModel
import ly.david.mbjc.data.persistence.history.LookupHistory
import ly.david.mbjc.data.persistence.history.LookupHistoryDao
import ly.david.mbjc.data.persistence.instrument.InstrumentDao
import ly.david.mbjc.data.persistence.instrument.InstrumentRoomModel
import ly.david.mbjc.data.persistence.label.LabelDao
import ly.david.mbjc.data.persistence.label.LabelRoomModel
import ly.david.mbjc.data.persistence.label.ReleasesLabels
import ly.david.mbjc.data.persistence.label.ReleasesLabelsDao
import ly.david.mbjc.data.persistence.place.PlaceDao
import ly.david.mbjc.data.persistence.place.PlaceRoomModel
import ly.david.mbjc.data.persistence.recording.RecordingDao
import ly.david.mbjc.data.persistence.recording.RecordingRoomModel
import ly.david.mbjc.data.persistence.relation.HasRelationsRoomModel
import ly.david.mbjc.data.persistence.relation.RelationDao
import ly.david.mbjc.data.persistence.relation.RelationRoomModel
import ly.david.mbjc.data.persistence.release.MediumDao
import ly.david.mbjc.data.persistence.release.MediumRoomModel
import ly.david.mbjc.data.persistence.release.ReleaseDao
import ly.david.mbjc.data.persistence.release.ReleaseRoomModel
import ly.david.mbjc.data.persistence.release.TrackDao
import ly.david.mbjc.data.persistence.release.TrackRoomModel
import ly.david.mbjc.data.persistence.releasegroup.ReleaseGroupDao
import ly.david.mbjc.data.persistence.releasegroup.ReleaseGroupRoomModel
import ly.david.mbjc.data.persistence.releasegroup.ReleasesReleaseGroups
import ly.david.mbjc.data.persistence.releasegroup.ReleasesReleaseGroupsDao
import ly.david.mbjc.data.persistence.work.WorkDao
import ly.david.mbjc.data.persistence.work.WorkRoomModel

@Database(
    version = 38,
    entities = [
        // Main tables
        ArtistRoomModel::class, ReleaseGroupRoomModel::class, ReleaseRoomModel::class,
        MediumRoomModel::class, TrackRoomModel::class, RecordingRoomModel::class, WorkRoomModel::class,
        AreaRoomModel::class, PlaceRoomModel::class,
        InstrumentRoomModel::class, LabelRoomModel::class,
        EventRoomModel::class,

        // Full-Text Search (FTS) tables
//        ReleaseGroupFts::class,

        // Relationship tables
        RelationRoomModel::class,
        HasRelationsRoomModel::class,
        ReleaseGroupArtistCreditRoomModel::class, ReleasesReleaseGroups::class,
        ReleasesLabels::class,

        // Additional features tables
        LookupHistory::class
    ],
    views = [],
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
        AutoMigration(from = 2, to = 3),
        AutoMigration(from = 3, to = 4),
        AutoMigration(from = 4, to = 5, spec = RenameCountry::class),
        AutoMigration(from = 5, to = 6, spec = RenameReleasesCountryToCountryCode::class),
        AutoMigration(from = 6, to = 7),
        AutoMigration(from = 7, to = 8),
        AutoMigration(from = 8, to = 9),
        AutoMigration(from = 9, to = 10),
        AutoMigration(from = 11, to = 12),
        AutoMigration(from = 12, to = 13, spec = GeneralizeRecordingRelation::class),
        AutoMigration(from = 13, to = 14, spec = RenameResourceId::class),
        AutoMigration(from = 14, to = 15, spec = DeleteResource::class),
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
        AutoMigration(from = 27, to = 28, spec = RenameHistorySummaryToTitle::class),
        AutoMigration(from = 28, to = 29),
        AutoMigration(from = 30, to = 31),
        AutoMigration(from = 31, to = 32, spec = DeleteHasRelationsFromLabel::class),
        AutoMigration(from = 33, to = 34, spec = DeleteHasRelationsFromReleaseGroup::class),
        AutoMigration(from = 35, to = 36, spec = DeleteHasRelationsFromArea::class),
        AutoMigration(from = 37, to = 38, spec = DeleteHasRelationsFromArtist::class),
    ]
)
@TypeConverters(MusicBrainzRoomTypeConverters::class)
internal abstract class MusicBrainzRoomDatabase : RoomDatabase() {

    abstract fun getArtistDao(): ArtistDao
    abstract fun getReleaseGroupArtistDao(): ReleaseGroupArtistDao

    abstract fun getReleaseGroupDao(): ReleaseGroupDao
    abstract fun getReleasesReleaseGroupsDao(): ReleasesReleaseGroupsDao

    abstract fun getReleaseDao(): ReleaseDao
    abstract fun getMediumDao(): MediumDao
    abstract fun getTrackDao(): TrackDao

    abstract fun getRecordingDao(): RecordingDao

    abstract fun getWorkDao(): WorkDao

    abstract fun getAreaDao(): AreaDao

    abstract fun getPlaceDao(): PlaceDao

    abstract fun getInstrumentDao(): InstrumentDao

    abstract fun getLabelDao(): LabelDao
    abstract fun getReleasesLabelsDao(): ReleasesLabelsDao

    abstract fun getEventDao(): EventDao

    abstract fun getRelationDao(): RelationDao
    abstract fun getLookupHistoryDao(): LookupHistoryDao
}

// region Migrations
@RenameColumn(tableName = "artists", fromColumnName = "country", toColumnName = "country_code")
class RenameCountry : AutoMigrationSpec

@RenameColumn(tableName = "releases", fromColumnName = "country", toColumnName = "country_code")
class RenameReleasesCountryToCountryCode : AutoMigrationSpec

@RenameTable(fromTableName = "recordings_relations", toTableName = "relations")
@RenameColumn(tableName = "recordings_relations", fromColumnName = "resource", toColumnName = "linked_resource")
class GeneralizeRecordingRelation : AutoMigrationSpec

@RenameColumn(tableName = "relations", fromColumnName = "recording_id", toColumnName = "resource_id")
class RenameResourceId : AutoMigrationSpec

@DeleteColumn(tableName = "relations", columnName = "resource")
class DeleteResource : AutoMigrationSpec

@RenameColumn(tableName = "lookup_history", fromColumnName = "summary", toColumnName = "title")
class RenameHistorySummaryToTitle : AutoMigrationSpec

val MIGRATION_10_11 = object : Migration(10, 11) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            """
                ALTER TABLE lookup_history
                RENAME COLUMN destination TO resource
            """
        )

        // removes leading "lookup/"
        database.execSQL(
            """
                UPDATE lookup_history
                SET resource = SUBSTR(resource, 8)
            """
        )

        database.execSQL(
            """
                ALTER TABLE recordings_relations
                RENAME COLUMN destination TO resource
            """
        )
        database.execSQL(
            """
                UPDATE recordings_relations
                SET resource = SUBSTR(resource, 8)
            """
        )
    }
}

val MIGRATION_29_30 = object : Migration(29, 30) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            """
                INSERT INTO has_relations ( resource_id, has_relations )
                SELECT id, has_default_relations
                FROM labels
            """
        )
    }
}

@DeleteColumn(tableName = "labels", columnName = "has_default_relations")
private class DeleteHasRelationsFromLabel : AutoMigrationSpec

val MIGRATION_32_33 = object : Migration(32, 33) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            """
                INSERT INTO has_relations ( resource_id, has_relations )
                SELECT id, has_default_relations
                FROM release_groups
            """
        )
    }
}

// ALTER TABLE DROP COLUMN isn't supported but @DeleteColumn automigration is
@DeleteColumn(tableName = "release_groups", columnName = "has_default_relations")
private class DeleteHasRelationsFromReleaseGroup : AutoMigrationSpec

private val MIGRATION_34_35 = object : Migration(34, 35) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            """
                INSERT INTO has_relations ( resource_id, has_relations )
                SELECT id, has_default_relations
                FROM areas
            """
        )
    }
}

@DeleteColumn(tableName = "areas", columnName = "has_default_relations")
private class DeleteHasRelationsFromArea : AutoMigrationSpec

private val MIGRATION_36_37 = object : Migration(36, 37) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            """
                INSERT INTO has_relations ( resource_id, has_relations )
                SELECT id, has_default_relations
                FROM artists
            """
        )
    }
}

@DeleteColumn(tableName = "artists", columnName = "has_default_relations")
private class DeleteHasRelationsFromArtist : AutoMigrationSpec
// endregion

private const val DATABASE_NAME = "mbjc.db"

@InstallIn(SingletonComponent::class)
@Module
internal object DatabaseModule {
    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ): MusicBrainzRoomDatabase {
        return Room.databaseBuilder(context, MusicBrainzRoomDatabase::class.java, DATABASE_NAME)
            .addMigrations(MIGRATION_10_11)
            .addMigrations(MIGRATION_29_30)
            .addMigrations(MIGRATION_32_33)
            .addMigrations(MIGRATION_34_35)
            .addMigrations(MIGRATION_36_37)
            .fallbackToDestructiveMigration()
            .build()
    }
}

@InstallIn(SingletonComponent::class)
@Module
internal object DatabaseDaoModule {
    @Provides
    fun provideArtistDao(db: MusicBrainzRoomDatabase): ArtistDao = db.getArtistDao()

    @Provides
    fun provideReleaseGroupArtistDao(db: MusicBrainzRoomDatabase) = db.getReleaseGroupArtistDao()

    @Provides
    fun provideReleaseGroupDao(db: MusicBrainzRoomDatabase) = db.getReleaseGroupDao()

    @Provides
    fun provideReleasesReleaseGroupsDao(db: MusicBrainzRoomDatabase) = db.getReleasesReleaseGroupsDao()

    @Provides
    fun provideReleaseDao(db: MusicBrainzRoomDatabase) = db.getReleaseDao()

    @Provides
    fun provideMediumDao(db: MusicBrainzRoomDatabase) = db.getMediumDao()

    @Provides
    fun provideTrackDao(db: MusicBrainzRoomDatabase) = db.getTrackDao()

    @Provides
    fun provideRecordingDao(db: MusicBrainzRoomDatabase) = db.getRecordingDao()

    @Provides
    fun provideWorkDao(db: MusicBrainzRoomDatabase) = db.getWorkDao()

    @Provides
    fun provideAreaDao(db: MusicBrainzRoomDatabase) = db.getAreaDao()

    @Provides
    fun providePlaceDao(db: MusicBrainzRoomDatabase) = db.getPlaceDao()

    @Provides
    fun provideInstrumentDao(db: MusicBrainzRoomDatabase) = db.getInstrumentDao()

    @Provides
    fun provideLabelDao(db: MusicBrainzRoomDatabase) = db.getLabelDao()

    @Provides
    fun provideReleasesLabelsDao(db: MusicBrainzRoomDatabase) = db.getReleasesLabelsDao()

    @Provides
    fun provideEventDao(db: MusicBrainzRoomDatabase) = db.getEventDao()

    @Provides
    fun provideRelationDao(db: MusicBrainzRoomDatabase) = db.getRelationDao()

    @Provides
    fun provideLookupHistoryDao(db: MusicBrainzRoomDatabase) = db.getLookupHistoryDao()
}
