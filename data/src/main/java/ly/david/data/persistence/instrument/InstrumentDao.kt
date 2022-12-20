package ly.david.data.persistence.instrument

import androidx.room.Dao
import androidx.room.Query
import ly.david.data.persistence.BaseDao

@Dao
abstract class InstrumentDao : BaseDao<InstrumentRoomModel>() {

    @Query("SELECT * FROM instrument WHERE id = :instrumentId")
    abstract suspend fun getInstrument(instrumentId: String): InstrumentRoomModel?
}
