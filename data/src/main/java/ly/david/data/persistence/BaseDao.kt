package ly.david.data.persistence

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

internal interface BaseDao<in T> {

    // Replace strategy let us replace a release with additional information such as formats/tracks data
    // when doing a lookup vs when first inserting it via release group browse screen.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: T): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entities: List<T>)

    @Delete
    fun delete(entity: T)

    @Update
    suspend fun update(entity: T)
}
