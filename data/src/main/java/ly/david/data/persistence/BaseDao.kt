package ly.david.data.persistence

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

internal interface BaseDao<in T> {

    // Replace strategy let us replace a release with additional information such as formats/tracks data
    // when doing a lookup vs when first inserting it via release group browse screen.
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(entity: T): Long

    /**
     * In general, we should choose [insert] over this.
     * This is suitable for lookups where we will request for more information than previously stored.
     * Eg. When browsing releases, we don't store its tracks/formats, but on lookup, we do.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReplace(entity: T): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(entities: List<T>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllReplace(entities: List<T>)

    @Delete
    fun delete(entity: T)

    @Update
    suspend fun update(entity: T)
}
