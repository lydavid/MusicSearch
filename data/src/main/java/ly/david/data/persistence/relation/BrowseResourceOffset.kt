package ly.david.data.persistence.relation

import androidx.room.ColumnInfo
import androidx.room.Entity
import ly.david.data.network.MusicBrainzResource

// TODO: rename
/**
 * Tracks how many resources [browseResource] there are in MusicBrainz [remoteCount]
 * and how many we have collected [localCount] linked to a resource with [resourceId].
 *
 * Originally, we used a SQL count query to sum up the local resources.
 * But that would mess up our browsing because we might have collected a resource via other means than browsing,
 * (such as lookup) which would make us incorrectly begin our offset at 0.
 * Because we cannot guarantee the order of individually lookup'd resources, we will make sure to browse everything
 * starting from offset 0, while making sure to Ignore rather than Replace its entry when we encounter it.
 *
 * @param resourceId The resource's id we're tracking the progress of browsing a paginated [browseResource].
 *  Ex. An Area we want to browse all of its Releases.
 *  We know for certain that mbid are unique between different resources, because there is an option to lookup by
 *  mbid: https://musicbrainz.org/search.
 * @param browseResource The resource we're browsing.
 *  Ex. Releases in an Area.
 * @param localCount This is not actually the number of [browseResource] we have in our local database,
 *  but rather the current offset for browsing [browseResource].
 *
 * @param remoteCount How many [browseResource] exists in MusicBrainz's database.
 *  null means we have not yet started browsing. 0 means there were none.
 */
@Entity(
    tableName = "browse_resource_counts",
    primaryKeys = ["resource_id", "browse_resource"]
)
data class BrowseResourceOffset(
    @ColumnInfo(name = "resource_id") val resourceId: String,
    @ColumnInfo(name = "browse_resource") val browseResource: MusicBrainzResource,
    @ColumnInfo(name = "local_count") val localCount: Int = 0,
    @ColumnInfo(name = "remote_count") val remoteCount: Int? = null,
)
