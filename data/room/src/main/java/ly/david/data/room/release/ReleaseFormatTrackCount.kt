package ly.david.data.room.release

import androidx.room.DatabaseView

@DatabaseView(
    value = """
    SELECT r.id as releaseId, m.format, COUNT(t.id) as trackCount
    FROM track t
    INNER JOIN medium m ON t.medium_id = m.id
    INNER JOIN `release` r ON m.release_id = r.id
    GROUP BY m.id
    """,
    viewName = "release_format_track_count"
)
data class ReleaseFormatTrackCount(
    val releaseId: String,
    val format: String?,
    val trackCount: Int
)

data class FormatTrackCount(
    val format: String?,
    val trackCount: Int
)
