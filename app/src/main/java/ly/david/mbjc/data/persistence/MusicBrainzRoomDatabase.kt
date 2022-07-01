package ly.david.mbjc.data.persistence

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
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
import ly.david.mbjc.data.persistence.recording.RecordingDao
import ly.david.mbjc.data.persistence.recording.RecordingRelationDao
import ly.david.mbjc.data.persistence.recording.RelationRoomModel
import ly.david.mbjc.data.persistence.release.MediumDao
import ly.david.mbjc.data.persistence.release.MediumRoomModel
import ly.david.mbjc.data.persistence.release.ReleaseDao
import ly.david.mbjc.data.persistence.release.ReleasesReleaseGroups
import ly.david.mbjc.data.persistence.release.ReleasesReleaseGroupsDao
import ly.david.mbjc.data.persistence.release.TrackDao
import ly.david.mbjc.data.persistence.release.TrackRoomModel
import ly.david.mbjc.data.persistence.releasegroup.ReleaseGroupDao

@Database(
    version = 14,
    entities = [
        // Main tables
        ArtistRoomModel::class, ReleaseGroupRoomModel::class, ReleaseRoomModel::class,
        MediumRoomModel::class, TrackRoomModel::class, RecordingRoomModel::class,

        // Full-Text Search (FTS) tables
//        ReleaseGroupFts::class,

        // Relationship tables
        ReleaseGroupArtistCreditRoomModel::class, ReleasesReleaseGroups::class,
        RelationRoomModel::class,

        // Additional features tables
        LookupHistory::class
    ],
    views = [],
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
        AutoMigration(from = 2, to = 3),
        AutoMigration(from = 3, to = 4),
        AutoMigration(from = 4, to = 5, spec = MusicBrainzRoomDatabase.RenameCountry::class),
        AutoMigration(from = 5, to = 6, spec = MusicBrainzRoomDatabase.RenameReleasesCountryToCountryCode::class),
        AutoMigration(from = 6, to = 7),
        AutoMigration(from = 7, to = 8),
        AutoMigration(from = 8, to = 9),
        AutoMigration(from = 9, to = 10),
        AutoMigration(from = 11, to = 12),
        AutoMigration(from = 12, to = 13, spec = MusicBrainzRoomDatabase.GeneralizeRecordingRelation::class),
        AutoMigration(from = 13, to = 14, spec = MusicBrainzRoomDatabase.RenameResourceId::class),
    ]
)
@TypeConverters(MusicBrainzRoomTypeConverters::class)
internal abstract class MusicBrainzRoomDatabase : RoomDatabase() {

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
    // endregion

    abstract fun getArtistDao(): ArtistDao
    abstract fun getReleaseGroupArtistDao(): ReleaseGroupArtistDao
    abstract fun getReleaseGroupDao(): ReleaseGroupDao
    abstract fun getReleasesReleaseGroupsDao(): ReleasesReleaseGroupsDao
    abstract fun getReleaseDao(): ReleaseDao
    abstract fun getMediumDao(): MediumDao
    abstract fun getTrackDao(): TrackDao
    abstract fun getRecordingDao(): RecordingDao
    abstract fun getRecordingRelationDao(): RecordingRelationDao

    abstract fun getLookupHistoryDao(): LookupHistoryDao
}

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
    fun provideRecordingRelationDao(db: MusicBrainzRoomDatabase) = db.getRecordingRelationDao()

    @Provides
    fun provideLookupHistoryDao(db: MusicBrainzRoomDatabase) = db.getLookupHistoryDao()
}
