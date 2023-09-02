package ly.david.data.room.relation

import androidx.room.ColumnInfo
import androidx.room.Entity
import java.net.URLDecoder
import ly.david.data.core.Relation
import ly.david.data.core.common.emptyToNull
import ly.david.data.core.common.transformThisIfNotNullOrEmpty
import ly.david.data.core.getDisplayNames
import ly.david.data.core.getLifeSpanForDisplay
import ly.david.data.core.network.MusicBrainzEntity
import ly.david.data.network.RelationMusicBrainzModel
import ly.david.data.network.getFormattedAttributesForDisplay
import ly.david.data.network.getHeader
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

/**
 * We cannot guarantee that a [RelationRoomModel] will be created in the scenario that target-type points to a resource
 * but that object is null. It's possible that this is never the case, but our models are currently structured such
 * that any of them are nullable.
 */
fun RelationMusicBrainzModel.toRelationRoomModel(
    entityId: String,
    order: Int,
): RelationRoomModel? {
    var linkedEntityId = ""
    var linkedEntityName = ""
    var linkedEntityDisambiguation: String? = null
    var additionalInfo: String? = null
    val linkedTargetType = targetType
    when (linkedTargetType) {
        MusicBrainzEntity.ARTIST -> {
            if (artist == null) return null
            artist?.apply {
                linkedEntityId = id
                linkedEntityName = targetCredit.emptyToNull() ?: name
                linkedEntityDisambiguation = disambiguation
                additionalInfo = getLifeSpanForDisplay().transformThisIfNotNullOrEmpty { "($it)" }
            }
        }
        MusicBrainzEntity.RELEASE_GROUP -> {
            if (releaseGroup == null) return null
            releaseGroup?.apply {
                linkedEntityId = id
                linkedEntityName = targetCredit.emptyToNull() ?: name
                linkedEntityDisambiguation = disambiguation
                additionalInfo = getLifeSpanForDisplay().transformThisIfNotNullOrEmpty { "($it)" }
            } ?: return null
        }
        MusicBrainzEntity.RELEASE -> {
            if (release == null) return null
            release?.apply {
                linkedEntityId = id
                linkedEntityName = targetCredit.emptyToNull() ?: name
                linkedEntityDisambiguation = disambiguation
                additionalInfo = getLifeSpanForDisplay().transformThisIfNotNullOrEmpty { "($it)" }
            } ?: return null
        }
        MusicBrainzEntity.RECORDING -> {
            if (recording == null) return null
            recording?.apply {
                linkedEntityId = id
                linkedEntityName = targetCredit.emptyToNull() ?: name
                linkedEntityDisambiguation = disambiguation
                additionalInfo = artistCredits.getDisplayNames().transformThisIfNotNullOrEmpty { "by $it" } +
                    getLifeSpanForDisplay().transformThisIfNotNullOrEmpty { "($it)" }
            } ?: return null
        }
        MusicBrainzEntity.LABEL -> {
            if (label == null) return null
            label?.apply {
                linkedEntityId = id
                linkedEntityName = targetCredit.emptyToNull() ?: name
                linkedEntityDisambiguation = disambiguation
            } ?: return null
        }
        MusicBrainzEntity.AREA -> {
            if (area == null) return null
            area?.apply {
                linkedEntityId = id
                linkedEntityName = targetCredit.emptyToNull() ?: name
                linkedEntityDisambiguation = disambiguation
            } ?: return null
        }
        MusicBrainzEntity.PLACE -> {
            if (place == null) return null
            place?.apply {
                linkedEntityId = id
                linkedEntityName = targetCredit.emptyToNull() ?: name
                linkedEntityDisambiguation = disambiguation
            } ?: return null
        }
        MusicBrainzEntity.WORK -> {
            if (work == null) return null
            work?.apply {
                linkedEntityId = id
                linkedEntityName = targetCredit.emptyToNull() ?: name
                linkedEntityDisambiguation = disambiguation
            } ?: return null
        }
        MusicBrainzEntity.INSTRUMENT -> {
            if (instrument == null) return null
            instrument?.apply {
                linkedEntityId = id
                linkedEntityName = targetCredit.emptyToNull() ?: name
                linkedEntityDisambiguation = disambiguation
            } ?: return null
        }
        MusicBrainzEntity.GENRE -> {
            if (genre == null) return null
            genre?.apply {
                linkedEntityId = id
                linkedEntityName = targetCredit.emptyToNull() ?: name
                linkedEntityDisambiguation = disambiguation
            } ?: return null
        }
        MusicBrainzEntity.EVENT -> {
            if (event == null) return null
            event?.apply {
                linkedEntityId = id
                linkedEntityName = targetCredit.emptyToNull() ?: name
                linkedEntityDisambiguation = disambiguation
            } ?: return null
        }
        MusicBrainzEntity.SERIES -> {
            if (series == null) return null
            series?.apply {
                linkedEntityId = id
                linkedEntityName = targetCredit.emptyToNull() ?: name
                linkedEntityDisambiguation = disambiguation
            } ?: return null
        }
        MusicBrainzEntity.URL -> {
            if (url == null) return null
            url?.apply {
                linkedEntityId = id
                linkedEntityName = URLDecoder.decode(resource, "utf-8")
                linkedEntityDisambiguation = null
            } ?: return null
        }
        else -> return null
    }

    return RelationRoomModel(
        entityId = entityId,
        linkedEntityId = linkedEntityId,
        linkedEntity = linkedTargetType,
        order = order,
        label = getHeader(),
        name = linkedEntityName,
        disambiguation = linkedEntityDisambiguation,
        attributes = getFormattedAttributesForDisplay(),
        additionalInfo = additionalInfo
    )
}
