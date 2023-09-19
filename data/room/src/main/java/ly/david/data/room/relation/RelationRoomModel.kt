package ly.david.data.room.relation

import androidx.room.ColumnInfo
import androidx.room.Entity
import ly.david.data.core.Relation
import ly.david.data.core.network.MusicBrainzEntity
import ly.david.data.musicbrainz.RelationMusicBrainzModel
import ly.david.data.room.RoomModel

// TODO: [low priority] recording/a53c97d7-5501-443b-baa3-cb282fc64275 returns "The Sound Factory West" twice.
//  web doesn't display it twice, so maybe we shouldn't either.

/**
 * The existence of this model for a [entityId] should be enough to indicate that we have locally stored
 * all of its relationships since there's no pagination for relationships.
 * That's not actually true because it won't handle the case where a resource has no relationships.
 * It would make us always true to fetch it even though we've discovered there are none.
 */
@Entity(
    tableName = "relation",
    primaryKeys = ["entity_id", "linked_entity_id", "order"],
)
data class RelationRoomModel(
    @ColumnInfo(name = "entity_id")
    val entityId: String,

    // TODO: can we make it nullable so that we don't pass url id?
    @ColumnInfo(name = "linked_entity_id")
    override val linkedEntityId: String,

    @ColumnInfo(name = "linked_entity")
    override val linkedEntity: MusicBrainzEntity,

    // TODO: an artist can appear multiple times similar to artist credits
    //  how about using label as another key?
    @ColumnInfo(name = "order")
    val order: Int,

    /**
     * [RelationMusicBrainzModel.type].
     */
    @ColumnInfo(name = "label")
    override val label: String,

    @ColumnInfo(name = "name")
    override val name: String,

    @ColumnInfo(name = "disambiguation")
    override val disambiguation: String? = null,

    /**
     * Combined [RelationMusicBrainzModel.attributes].
     */
    @ColumnInfo(name = "attributes")
    override val attributes: String? = null,

    /**
     * Includes things like:
     * - by Artists
     * - in Area
     * - (in 1970-01)
     * - (order: 8)
     */
    @ColumnInfo(name = "additional_info")
    override val additionalInfo: String? = null,
) : Relation, RoomModel
