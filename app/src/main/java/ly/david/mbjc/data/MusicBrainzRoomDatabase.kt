package ly.david.mbjc.data

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.Update
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.util.Date
import javax.inject.Singleton

@Database(
    entities = [
        Artist::class, ReleaseGroup::class,
        ReleaseGroupArtist::class,
        LookupHistory::class
    ],
    views = [],
    version = 9
)
@TypeConverters(MusicBrainzTypeConverters::class)
abstract class MusicBrainzRoomDatabase : RoomDatabase() {

    abstract fun getArtistDao(): ArtistDao
    abstract fun getReleaseGroupDao(): ReleaseGroupDao
    abstract fun getReleaseGroupArtistDao(): ReleaseGroupArtistDao

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

interface BaseDao<in T> {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(entity: T): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(entities: List<T>)

    @Delete
    fun delete(entity: T)

    @Update
    suspend fun update(entity: T)
}

@Dao
abstract class ArtistDao : BaseDao<Artist>

@Dao
abstract class ReleaseGroupDao : BaseDao<ReleaseGroup> {

//    @Query("")
//    suspend fun getAllReleaseGroupsByArtist(artistId: String): List<ReleaseGroup>
}

@Dao
abstract class ReleaseGroupArtistDao : BaseDao<ReleaseGroupArtist>

@Dao
abstract class LookupHistoryDao : BaseDao<LookupHistory> {

    @Query("SELECT * FROM lookup_history ORDER BY last_accessed DESC")
    abstract suspend fun getAllLookupHistory(): List<LookupHistory>

    @Query(
        """
        UPDATE lookup_history 
        SET number_of_visits = number_of_visits + 1,
            last_accessed = :lastAccessed
        WHERE mbid = :mbid
        """
    )
    abstract suspend fun incrementVisitAndDateAccessed(mbid: String, lastAccessed: Date = Date()): Int

    /**
     *
     */
    suspend fun incrementOrInsertLookupHistory(lookupHistory: LookupHistory) {
        val numUpdated = incrementVisitAndDateAccessed(lookupHistory.mbid)
        if (numUpdated == 0) {
            insert(lookupHistory)
        }
    }
}

@InstallIn(SingletonComponent::class)
@Module
object DatabaseDaoModule {
    @Provides
    fun provideArtistDao(db: MusicBrainzRoomDatabase): ArtistDao = db.getArtistDao()

    @Provides
    fun provideReleaseGroupDao(db: MusicBrainzRoomDatabase): ReleaseGroupDao = db.getReleaseGroupDao()

    @Provides
    fun provideReleaseGroupArtistDao(db: MusicBrainzRoomDatabase): ReleaseGroupArtistDao = db.getReleaseGroupArtistDao()

    @Provides
    fun provideLookupHistoryDao(db: MusicBrainzRoomDatabase): LookupHistoryDao = db.getLookupHistoryDao()
}
