package ly.david.data.persistence

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Transaction
import androidx.room.Update
import androidx.room.Upsert

abstract class BaseDao<in T> {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insert(entity: T): Long

    @Upsert
    abstract suspend fun upsert(entity: T)

    /**
     * In general, we should choose [insert] over this.
     * This is suitable for lookups where we will request for more information than previously stored.
     * Eg. When browsing releases, we don't store its tracks/formats, but on lookup, we do.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertReplace(entity: T): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insertAll(entities: List<T>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertAllReplace(entities: List<T>)

    @Update
    abstract suspend fun update(entity: T)

    suspend fun insertOrUpdate(entity: T) {
        val insertAttempt = insert(entity)
        if (insertAttempt == INSERTION_FAILED_DUE_TO_CONFLICT) {
            update(entity)
        }
    }

    @Transaction
    open suspend fun insertOrUpdate(entities: List<T>) {
        entities.forEach {
            insertOrUpdate(it)
        }
    }

    @Transaction
    open suspend fun withTransaction(block: suspend () -> Unit) {
        block()
    }
}
