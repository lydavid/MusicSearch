package ly.david.data.persistence.release

import androidx.room.DatabaseView
import androidx.room.Embedded
import androidx.room.Relation
import ly.david.data.persistence.RoomModel
import ly.david.data.persistence.area.AreaRoomModel
import ly.david.data.persistence.area.Iso3166_1
import ly.david.data.persistence.area.ReleaseCountry
import ly.david.data.persistence.artist.credit.ArtistCreditNamesWithResource
import ly.david.data.persistence.label.LabelRoomModel
import ly.david.data.persistence.label.ReleaseLabel

/**
 * A label together with its catalog number for a release.
 *
 * We include the entirety of [ReleaseLabel] so that we can filter on release id.
 */
@DatabaseView(
    """
    SELECT l.*, rl.*
    FROM labels l
    INNER JOIN releases_labels rl ON l.id = rl.label_id
"""
)
data class LabelWithCatalog(
    @Embedded
    val label: LabelRoomModel,

    @Embedded
    val releaseLabel: ReleaseLabel
)

/**
 * An area together with its country codes.
 *
 * We include [ReleaseLabel] so that we can filter on release id.
 */
@DatabaseView(
    """
        SELECT a.*, rc.*
        FROM areas a
        INNER JOIN releases_countries rc ON rc.country_id = a.id
        INNER JOIN releases r ON r.id = rc.release_id
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
    val countryCodes: List<Iso3166_1>
) : RoomModel

/**
 * Don't use this when paging releases.
 */
data class ReleaseWithAllData(
    @Embedded
    val release: ReleaseRoomModel,

    @Relation(
        parentColumn = "id", // release.id
        entityColumn = "release_id",
    )
    val areas: List<AreaWithReleaseDate>,

    @Relation(
        parentColumn = "id", // release.id
        entity = LabelWithCatalog::class,
        entityColumn = "release_id",
    )
    val labels: List<LabelWithCatalog>,

    @Relation(
        parentColumn = "id",
        entityColumn = "resource_id"
    )
    val artistCreditNamesWithResources: List<ArtistCreditNamesWithResource>
) : RoomModel
