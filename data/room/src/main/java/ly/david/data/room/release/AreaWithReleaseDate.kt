package ly.david.data.room.release

import androidx.room.DatabaseView
import androidx.room.Embedded
import androidx.room.Relation
import ly.david.data.room.RoomModel
import ly.david.data.room.area.AreaRoomModel
import ly.david.data.room.area.CountryCode
import ly.david.data.room.area.releases.ReleaseCountry

/**
 * An area together with its country codes.
 *
 * We include [ReleaseLabel] so that we can filter on release id.
 */
@DatabaseView(
    """
        SELECT a.*, rc.*
        FROM area a
        INNER JOIN release_country rc ON rc.country_id = a.id
        INNER JOIN `release` r ON r.id = rc.release_id
        ORDER BY a.name
    """
)
data class AreaWithReleaseDate(
    @Embedded
    val area: AreaRoomModel,

    @Embedded
    val releaseCountry: ReleaseCountry,

    @Relation(
        parentColumn = "id",
        entityColumn = "area_id"
    )
    val countryCodes: List<CountryCode>,
) : RoomModel
