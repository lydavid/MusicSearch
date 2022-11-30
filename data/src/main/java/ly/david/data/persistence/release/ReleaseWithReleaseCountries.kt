package ly.david.data.persistence.release

import androidx.room.Embedded
import androidx.room.Relation
import ly.david.data.persistence.RoomModel
import ly.david.data.persistence.area.ReleaseCountry
import ly.david.data.persistence.artist.credit.ArtistCreditNamesWithResource

data class ReleaseWithReleaseCountries(
    @Embedded
    val release: ReleaseRoomModel,

    @Relation(
        parentColumn = "id",
        entityColumn = "release_id",
    )
    val releaseEvents: List<ReleaseCountry>,

    @Relation(
        parentColumn = "id",
        entityColumn = "resource_id"
    )
    val artistCreditNamesWithResources: List<ArtistCreditNamesWithResource>
): RoomModel
