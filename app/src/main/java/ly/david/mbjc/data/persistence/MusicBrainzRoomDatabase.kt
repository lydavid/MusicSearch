package ly.david.mbjc.data.persistence

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
import ly.david.mbjc.data.Artist

@Database(
    entities = [
        // Main tables
        Artist::class, RoomReleaseGroup::class,

        // Full-Text Search (FTS) tables
        ReleaseGroupFts::class,

        // Relationship tables
        RoomReleaseGroupArtistCredit::class,

        // Additional features tables
        LookupHistory::class
    ],
    views = [],
    version = 14
)
@TypeConverters(MusicBrainzRoomTypeConverters::class)
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
abstract class ReleaseGroupDao : BaseDao<RoomReleaseGroup> {

    @Query("SELECT * FROM release_groups WHERE id = :releaseGroupId")
    abstract suspend fun getReleaseGroup(releaseGroupId: String): RoomReleaseGroup?

    @Query(
        """
        SELECT COUNT(rg.id)
        FROM artists a
        LEFT JOIN release_groups_artists rga ON a.id = rga.artist_id
        LEFT JOIN release_groups rg ON rg.id = rga.release_group_id
        WHERE a.id = :artistId
        GROUP BY a.id
    """
    )
    abstract suspend fun getNumberOfReleaseGroupsByArtist(artistId: String): Int

    @Query("""
        SELECT rg.*
        FROM artists a
        LEFT JOIN release_groups_artists rga ON a.id = rga.artist_id
        LEFT JOIN release_groups rg ON rg.id = rga.release_group_id
        WHERE a.id = :artistId
    """)
    abstract suspend fun getAllReleaseGroupsByArtist(artistId: String): List<RoomReleaseGroup>

    // We match FTS with rowid because we use MusicBrainz id for `id`
    @Query(
        """
        SELECT rg.*
        FROM artists a
        LEFT JOIN release_groups_artists rga ON a.id = rga.artist_id
        LEFT JOIN release_groups rg ON rg.id = rga.release_group_id
        JOIN release_groups_fts_table fts ON rg.rowid = fts.rowid
        WHERE a.id = :artistId AND release_groups_fts_table MATCH :query
    """
    )
    abstract suspend fun getAllReleaseGroupsByArtistFilteredFts(artistId: String, query: String): List<RoomReleaseGroup>

    // Not as fast as FTS but allows searching characters within words
    @Query(
        """
        SELECT rg.*
        FROM artists a
        LEFT JOIN release_groups_artists rga ON a.id = rga.artist_id
        LEFT JOIN release_groups rg ON rg.id = rga.release_group_id
        WHERE a.id = :artistId
        AND rg.title LIKE :query OR rg.disambiguation LIKE :query OR rg.`first-release-date` LIKE :query
        OR rg.`primary-type` LIKE :query OR rg.`secondary-types` LIKE :query
    """
    )
    abstract suspend fun getAllReleaseGroupsByArtistFiltered(artistId: String, query: String): List<RoomReleaseGroup>
}

@Dao
abstract class ReleaseGroupArtistDao : BaseDao<RoomReleaseGroupArtistCredit> {
    @Query(
        """
        SELECT rga.*
        FROM release_groups rg
        LEFT JOIN release_groups_artists rga ON rg.id = rga.release_group_id
        where rg.id = :releaseGroupId
        ORDER BY rga.`order`
    """
    )
    abstract suspend fun getReleaseGroupArtistCredits(releaseGroupId: String): List<RoomReleaseGroupArtistCredit>
}

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
