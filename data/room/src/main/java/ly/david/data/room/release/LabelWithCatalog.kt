package ly.david.data.room.release

import androidx.room.DatabaseView
import androidx.room.Embedded
import ly.david.data.room.label.LabelRoomModel
import ly.david.data.room.label.releases.ReleaseLabel

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
    val releaseLabel: ReleaseLabel,
)
