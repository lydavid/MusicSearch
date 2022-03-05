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
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import androidx.room.Update
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.util.Date
import javax.inject.Singleton
import ly.david.mbjc.ui.Destination

// Just need to make sure possible values cannot include this delimiter
private const val DELIMITER = ","

class MusicBrainzTypeConverters {

    // For things like "secondary-types" which does not need its own table.
    @TypeConverter
    fun toString(strings: List<String>?): String? {
        return strings?.joinToString(DELIMITER)
    }

    @TypeConverter
    fun fromString(string: String?): List<String>? {
        return string?.split(DELIMITER)
    }

    @TypeConverter
    fun toDestination(string: String?): Destination? {
        return Destination.values().firstOrNull { it.route == string }
    }

    @TypeConverter
    fun fromDestination(destination: Destination?): String? {
        return destination?.route
    }

    @TypeConverter
    fun toDate(dateLong: Long): Date {
        return Date(dateLong)
    }

    @TypeConverter
    fun fromDate(date: Date): Long {
        return date.time
    }
}

@Database(
    entities = [
        Artist::class, ReleaseGroup::class,
        ReleaseGroupArtist::class,
        LookupHistory::class
    ],
    views = [],
    version = 8
)
@TypeConverters(MusicBrainzTypeConverters::class)
abstract class MusicBrainzRoomDatabase : RoomDatabase() {

    abstract fun getArtistDao(): ArtistDao
    abstract fun getReleaseGroupDao(): ReleaseGroupDao
    abstract fun getReleaseGroupArtistDao(): ReleaseGroupArtistDao

    abstract fun getLookupHistoryDao(): LookupHistoryDao
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

    // TODO: order by last accessed date
    @Query("SELECT * from lookup_history order by last_accessed")
    abstract suspend fun getAllLookupHistory(): List<LookupHistory>

    // TODO: allow updating last visited count, should also update last accessed
    // https://stackoverflow.com/a/50694690

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
    fun provideReleaseGroupDao(db: MusicBrainzRoomDatabase): ReleaseGroupDao = db.getReleaseGroupDao()

    @Provides
    fun provideReleaseGroupArtistDao(db: MusicBrainzRoomDatabase): ReleaseGroupArtistDao = db.getReleaseGroupArtistDao()

    @Provides
    fun provideLookupHistoryDao(db: MusicBrainzRoomDatabase): LookupHistoryDao = db.getLookupHistoryDao()
}
