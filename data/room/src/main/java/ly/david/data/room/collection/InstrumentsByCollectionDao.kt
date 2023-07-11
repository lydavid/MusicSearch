package ly.david.data.room.collection

import androidx.paging.PagingSource
import androidx.room.Query
import androidx.room.Transaction
import ly.david.data.room.instrument.InstrumentRoomModel

interface InstrumentsByCollectionDao {

    companion object {
        private const val INSTRUMENTS_BY_COLLECTION = """
            FROM instrument i
            INNER JOIN collection_entity ce ON i.id = ce.entity_id
            INNER JOIN collection c ON c.id = ce.id
            WHERE c.id = :collectionId
        """

        private const val SELECT_INSTRUMENT_BY_COLLECTION = """
            SELECT i.*
            $INSTRUMENTS_BY_COLLECTION
        """

        private const val SELECT_INSTRUMENT_ID_BY_COLLECTION = """
            SELECT i.id
            $INSTRUMENTS_BY_COLLECTION
        """

        private const val FILTERED = """
            AND (
                i.name LIKE :query OR i.disambiguation LIKE :query
                OR i.type LIKE :query OR i.description LIKE :query
            )
        """
    }

    @Query(
        """
        DELETE FROM instrument WHERE id IN (
        $SELECT_INSTRUMENT_ID_BY_COLLECTION
        )
        """
    )
    suspend fun deleteInstrumentsByCollection(collectionId: String)

    @Query(
        """
        SELECT IFNULL(
            (SELECT COUNT(*)
            $INSTRUMENTS_BY_COLLECTION
            ),
            0
        ) AS count
    """
    )
    suspend fun getNumberOfInstrumentsByCollection(collectionId: String): Int

    @Transaction
    @Query(
        """
        $SELECT_INSTRUMENT_BY_COLLECTION
    """
    )
    fun getInstrumentsByCollection(collectionId: String): PagingSource<Int, InstrumentRoomModel>

    @Transaction
    @Query(
        """
        $SELECT_INSTRUMENT_BY_COLLECTION
        $FILTERED
    """
    )
    fun getInstrumentsByCollectionFiltered(
        collectionId: String,
        query: String,
    ): PagingSource<Int, InstrumentRoomModel>
}
