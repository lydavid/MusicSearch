package ly.david.data.persistence.release

import androidx.room.DatabaseView
import androidx.room.Embedded
import androidx.room.Relation
import ly.david.data.persistence.RoomModel
import ly.david.data.persistence.area.ReleaseCountry
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
 * Don't use this when paging releases.
 */
data class ReleaseWithAllData(
    @Embedded
    val release: ReleaseRoomModel,

    // TODO: no longer using, since we query for these when we visit details
    @Relation(
        parentColumn = "id",
        entityColumn = "release_id",
    )
    val releaseEvents: List<ReleaseCountry>,

    // TODO: we can do the same thing for labels
    @Relation(
        parentColumn = "id", // release.id
        entity = LabelWithCatalog::class,
        entityColumn = "release_id",
    )
    val labels: List<LabelWithCatalog>
) : RoomModel
