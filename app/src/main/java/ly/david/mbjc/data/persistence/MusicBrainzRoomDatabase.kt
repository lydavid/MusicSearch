package ly.david.mbjc.data.persistence

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RenameColumn
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.AutoMigrationSpec
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import ly.david.mbjc.data.persistence.release.MediumDao
import ly.david.mbjc.data.persistence.release.ReleaseDao
import ly.david.mbjc.data.persistence.release.ReleasesReleaseGroups
import ly.david.mbjc.data.persistence.release.ReleasesReleaseGroupsDao
import ly.david.mbjc.data.persistence.release.RoomMedium
import ly.david.mbjc.data.persistence.release.RoomTrack
import ly.david.mbjc.data.persistence.release.TrackDao

@Database(
    version = 5,
    entities = [
        // Main tables
        RoomArtist::class, RoomReleaseGroup::class, RoomRelease::class,
        RoomMedium::class, RoomTrack::class,

        // Full-Text Search (FTS) tables
//        ReleaseGroupFts::class,

        // Relationship tables
        RoomReleaseGroupArtistCredit::class, ReleasesReleaseGroups::class,

        // Additional features tables
        LookupHistory::class
    ],
    views = [],
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
        AutoMigration(from = 2, to = 3),
        AutoMigration(from = 3, to = 4),
        AutoMigration(from = 4, to = 5, spec = MusicBrainzRoomDatabase.RenameCountry::class),
    ]
)
@TypeConverters(MusicBrainzRoomTypeConverters::class)
abstract class MusicBrainzRoomDatabase : RoomDatabase() {

    @RenameColumn(tableName = "artists", fromColumnName = "country", toColumnName = "country_code")
    class RenameCountry : AutoMigrationSpec

    abstract fun getArtistDao(): ArtistDao

    abstract fun getReleaseGroupArtistDao(): ReleaseGroupArtistDao

    abstract fun getReleaseGroupDao(): ReleaseGroupDao

    abstract fun getReleasesReleaseGroupsDao(): ReleasesReleaseGroupsDao

    abstract fun getReleaseDao(): ReleaseDao
    abstract fun getMediumDao(): MediumDao
    abstract fun getTrackDao(): TrackDao

    abstract fun getLookupHistoryDao(): LookupHistoryDao
}

private const val DATABASE_NAME = "mbjc.db"

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {
    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ): MusicBrainzRoomDatabase {
        return Room.databaseBuilder(context, MusicBrainzRoomDatabase::class.java, DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }
}

@InstallIn(SingletonComponent::class)
@Module
object DatabaseDaoModule {
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
    fun provideLookupHistoryDao(db: MusicBrainzRoomDatabase) = db.getLookupHistoryDao()
}
