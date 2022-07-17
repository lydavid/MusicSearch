package ly.david.mbjc.data.persistence.instrument

import androidx.room.Dao
import androidx.room.Query
import ly.david.mbjc.data.persistence.BaseDao
import ly.david.mbjc.data.persistence.InstrumentRoomModel

@Dao
internal abstract class InstrumentDao : BaseDao<InstrumentRoomModel> {

    @Query("SELECT * FROM instruments WHERE id = :instrumentId")
    abstract suspend fun getInstrument(instrumentId: String): InstrumentRoomModel?
}
