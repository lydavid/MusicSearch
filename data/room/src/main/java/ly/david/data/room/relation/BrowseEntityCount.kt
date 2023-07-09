package ly.david.data.room.relation

import androidx.room.ColumnInfo
import androidx.room.Entity
import ly.david.data.network.MusicBrainzEntity

/**
 * Tracks how many [browseEntity] there are in MusicBrainz [remoteCount]
 * and how many we have collected [localCount] linked to a entity with [entityId].
 *
 * Originally, we used a SQL count query to sum up the local entities.
 * But that would mess up our browsing because we might have collected an entity via other means than browsing,
 * (such as lookup) which would make us incorrectly begin our offset at 0.
 * Because we cannot guarantee the order of individually lookup'd entities, we will make sure to browse everything
 * starting from offset 0, while making sure to Ignore rather than Replace when inserting entities.
 *
 * @param entityId The entity's id we're tracking the progress of browsing a paginated [browseEntity].
 *  Ex. An Area we want to browse all of its Releases.
 *  We know for certain that mbid are unique between different entities, because there is an option to lookup by
 *  mbid: https://musicbrainz.org/search.
 * @param browseEntity The entity we're browsing.
 *  Ex. Releases in an Area.
 * @param localCount This is not actually the number of [browseEntity] we have in our local database,
 *  but rather the current offset for browsing [browseEntity].
 * @param remoteCount How many [browseEntity] exists in MusicBrainz's database.
 *  null means we have not yet started browsing. 0 means there were none.
 */
@Entity(
    tableName = "browse_entity_count",
    primaryKeys = ["entity_id", "browse_entity"]
)
data class BrowseEntityCount(
    @ColumnInfo(name = "entity_id") val entityId: String,
    @ColumnInfo(name = "browse_entity") val browseEntity: MusicBrainzEntity,
    @ColumnInfo(name = "local_count") val localCount: Int = 0,
    @ColumnInfo(name = "remote_count") val remoteCount: Int? = null,
)
