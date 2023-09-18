package ly.david.data.room.artist.credit

import androidx.room.ColumnInfo
import androidx.room.DatabaseView
import androidx.room.Embedded

// TODO: continue using a view or no?
//  may not need this if we're querying for this separately
//  cause rec/rel/rg joins with artist_credit to get its name
//  whereas this will give us the artist_credit_name.artist_id
@DatabaseView(
    value = """
    SELECT acr.entity_id, acn.*
    FROM artist_credit_entity acr
    INNER JOIN artist_credit ac ON ac.id = acr.artist_credit_id
    INNER JOIN artist_credit_name acn ON acn.artist_credit_id = ac.id
    """,
    viewName = "artist_credit_names_entity"
)
data class ArtistCreditNamesWithEntity(

    @ColumnInfo(name = "entity_id")
    val entityId: String,

    @Embedded
    val artistCreditNameRoomModel: ArtistCreditNameRoomModel,
)
