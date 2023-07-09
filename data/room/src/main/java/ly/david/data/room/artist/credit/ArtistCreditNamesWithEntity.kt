package ly.david.data.room.artist.credit

import androidx.room.ColumnInfo
import androidx.room.DatabaseView
import androidx.room.Embedded

@DatabaseView(
    value = """
    SELECT acr.resource_id, acn.*
    FROM artist_credit_entity acr
    INNER JOIN artist_credit ac ON ac.id = acr.artist_credit_id
    INNER JOIN artist_credit_name acn ON acn.artist_credit_id = ac.id
    """,
    viewName = "artist_credit_names_entity"
)
data class ArtistCreditNamesWithEntity(

    @ColumnInfo(name = "resource_id")
    val entityId: String,

    @Embedded
    val artistCreditNameRoomModel: ArtistCreditNameRoomModel,
)
