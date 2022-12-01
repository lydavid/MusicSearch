package ly.david.data.persistence.artist.credit

import androidx.room.ColumnInfo
import androidx.room.DatabaseView
import androidx.room.Embedded
import ly.david.data.domain.ArtistCreditUiModel
import ly.david.data.domain.toUiModel

@DatabaseView(
    """
    SELECT acr.resource_id, acn.*
    FROM artist_credits_resources acr
    INNER JOIN artist_credits ac ON ac.id = acr.artist_credit_id
    INNER JOIN artist_credit_names acn ON acn.artist_credit_id = ac.id
"""
)
data class ArtistCreditNamesWithResource(

    @ColumnInfo(name = "resource_id")
    val resourceId: String,

    @Embedded
    val artistCreditNameRoomModel: ArtistCreditNameRoomModel,
)

fun List<ArtistCreditNamesWithResource>.toUiModels(): List<ArtistCreditUiModel> =
    map {
        it.artistCreditNameRoomModel.toUiModel()
    }
