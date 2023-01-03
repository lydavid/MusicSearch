package ly.david.data.persistence.release

import androidx.room.DatabaseView
import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import ly.david.data.persistence.RoomModel
import ly.david.data.persistence.area.AreaRoomModel
import ly.david.data.persistence.area.Iso3166_1
import ly.david.data.persistence.area.ReleaseCountry
import ly.david.data.persistence.artist.credit.ArtistCreditNamesWithResource
import ly.david.data.persistence.label.LabelRoomModel
import ly.david.data.persistence.label.ReleaseLabel
import ly.david.data.persistence.release.releasegroup.ReleaseReleaseGroup
import ly.david.data.persistence.releasegroup.ReleaseGroupRoomModel

/**
 * A label together with its catalog number for a release.
 *
 * We include the entirety of [ReleaseLabel] so that we can filter on release id.
 */
@DatabaseView(
    """
    SELECT l.*, rl.*
    FROM label l
    INNER JOIN release_label rl ON l.id = rl.label_id
    ORDER BY rl.catalog_number
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
        FROM area a
        INNER JOIN release_country rc ON rc.country_id = a.id
        INNER JOIN release r ON r.id = rc.release_id
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

    val releaseLength: Int?,

    val hasNullLength: Boolean,

    @Relation(
        entity = ReleaseFormatTrackCount::class,
        parentColumn = "id",
        entityColumn = "releaseId",
        projection = ["format", "trackCount"]
    )
    val formatTrackCounts: List<FormatTrackCount>,

    @Relation(
        parentColumn = "id", // release.id
        entityColumn = "release_id",
    )
    val areas: List<AreaWithReleaseDate>,

    @Relation(
        parentColumn = "id",
        entityColumn = "release_id",
    )
    val labels: List<LabelWithCatalog>,

    @Relation(
        parentColumn = "id",
        entityColumn = "resource_id"
    )
    val artistCreditNamesWithResources: List<ArtistCreditNamesWithResource>,

    @Relation(
        parentColumn = "id",
        entityColumn = "id", // releaseGroup.id
        associateBy = Junction(
            value = ReleaseReleaseGroup::class,
            parentColumn = "release_id",
            entityColumn = "release_group_id"
        )
    )
    val releaseGroup: ReleaseGroupRoomModel?
) : RoomModel
