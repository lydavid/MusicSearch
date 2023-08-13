package ly.david.data.room.instrument

import androidx.room.Dao
import androidx.room.Query
import ly.david.data.room.BaseDao

@Dao
abstract class InstrumentDao : BaseDao<InstrumentRoomModel>() {

    @Query("SELECT * FROM instrument WHERE id = :instrumentId")
    abstract suspend fun getInstrument(instrumentId: String): InstrumentWithAllData?
}
