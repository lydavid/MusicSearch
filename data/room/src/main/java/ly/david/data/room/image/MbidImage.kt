package ly.david.data.room.image

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Links between a MusicBrainz entity and an image path.
 *
 * The image might come from:
 * - Cover Art Archive
 * - Spotify
 */
@Entity(tableName = "mbid_image")
data class MbidImage(
    @PrimaryKey @ColumnInfo(name = "mbid") val mbid: String,
    @ColumnInfo(name = "image_path") val imagePath: String
)
