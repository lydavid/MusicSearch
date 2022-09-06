package ly.david.mbjc.data.persistence.releasegroup

import androidx.room.ColumnInfo
import ly.david.mbjc.data.ReleaseGroupTypes

internal data class ReleaseGroupTypeCount(
    @ColumnInfo(name = "primary_type")
    override val primaryType: String? = null,

    @ColumnInfo(name = "secondary_types")
    override val secondaryTypes: List<String>? = null,

    @ColumnInfo(name = "count")
    val count: Int,
) : ReleaseGroupTypes
