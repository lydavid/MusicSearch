package ly.david.data.persistence.relation

import androidx.room.ColumnInfo
import androidx.room.Entity
import java.net.URLDecoder
import ly.david.data.Relation
import ly.david.data.common.emptyToNull
import ly.david.data.common.transformThisIfNotNullOrEmpty
import ly.david.data.getDisplayNames
import ly.david.data.getLifeSpanForDisplay
import ly.david.data.network.MusicBrainzResource
import ly.david.data.network.RelationMusicBrainzModel
import ly.david.data.network.getFormattedAttributesForDisplay
import ly.david.data.network.getHeader
import ly.david.data.persistence.RoomModel

// TODO: [low priority] recording/a53c97d7-5501-443b-baa3-cb282fc64275 returns "The Sound Factory West" twice.
//  web doesn't display it twice, so maybe we shouldn't either.

/**
 * The existence of this model for a [resourceId] should be enough to indicate that we have locally stored
 * all of its relationships since there's no pagination for relationships.
 * That's not actually true because it won't handle the case where a resource has no relationships.
 * It would make us always true to fetch it even though we've discovered there are none.
 */
@Entity(
    tableName = "relation",
    primaryKeys = ["resource_id", "linked_resource_id", "order"],
)
data class RelationRoomModel(
    @ColumnInfo(name = "resource_id")
    val resourceId: String,

    // TODO: can we make it nullable so that we don't pass url id?
    @ColumnInfo(name = "linked_resource_id")
    override val linkedResourceId: String,

    @ColumnInfo(name = "linked_resource")
    override val linkedResource: MusicBrainzResource,

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
    resourceId: String,
    order: Int,
): RelationRoomModel? {

    var linkedResourceId = ""
    var linkedResourceName = ""
    var linkedResourceDisambiguation: String? = null
    var additionalInfo: String? = null
    val linkedTargetType = targetType
    when (linkedTargetType) {
        MusicBrainzResource.ARTIST -> {
            if (artist == null) return null
            artist?.apply {
                linkedResourceId = id
                linkedResourceName = targetCredit.emptyToNull() ?: name
                linkedResourceDisambiguation = disambiguation
                additionalInfo = getLifeSpanForDisplay().transformThisIfNotNullOrEmpty { "($it)" }
            }
        }
        MusicBrainzResource.RELEASE_GROUP -> {
            if (releaseGroup == null) return null
            releaseGroup?.apply {
                linkedResourceId = id
                linkedResourceName = targetCredit.emptyToNull() ?: name
                linkedResourceDisambiguation = disambiguation
                additionalInfo = getLifeSpanForDisplay().transformThisIfNotNullOrEmpty { "($it)" }
            } ?: return null
        }
        MusicBrainzResource.RELEASE -> {
            if (release == null) return null
            release?.apply {
                linkedResourceId = id
                linkedResourceName = targetCredit.emptyToNull() ?: name
                linkedResourceDisambiguation = disambiguation
                additionalInfo = getLifeSpanForDisplay().transformThisIfNotNullOrEmpty { "($it)" }
            } ?: return null
        }
        MusicBrainzResource.RECORDING -> {
            if (recording == null) return null
            recording?.apply {
                linkedResourceId = id
                linkedResourceName = targetCredit.emptyToNull() ?: name
                linkedResourceDisambiguation = disambiguation
                additionalInfo = artistCredits.getDisplayNames().transformThisIfNotNullOrEmpty { "by $it" } +
                    getLifeSpanForDisplay().transformThisIfNotNullOrEmpty { "($it)" }
            } ?: return null
        }
        MusicBrainzResource.LABEL -> {
            if (label == null) return null
            label?.apply {
                linkedResourceId = id
                linkedResourceName = targetCredit.emptyToNull() ?: name
                linkedResourceDisambiguation = disambiguation
            } ?: return null
        }
        MusicBrainzResource.AREA -> {
            if (area == null) return null
            area?.apply {
                linkedResourceId = id
                linkedResourceName = targetCredit.emptyToNull() ?: name
                linkedResourceDisambiguation = disambiguation
            } ?: return null
        }
        MusicBrainzResource.PLACE -> {
            if (place == null) return null
            place?.apply {
                linkedResourceId = id
                linkedResourceName = targetCredit.emptyToNull() ?: name
                linkedResourceDisambiguation = disambiguation
            } ?: return null
        }
        MusicBrainzResource.WORK -> {
            if (work == null) return null
            work?.apply {
                linkedResourceId = id
                linkedResourceName = targetCredit.emptyToNull() ?: name
                linkedResourceDisambiguation = disambiguation
            } ?: return null
        }
        MusicBrainzResource.INSTRUMENT -> {
            if (instrument == null) return null
            instrument?.apply {
                linkedResourceId = id
                linkedResourceName = targetCredit.emptyToNull() ?: name
                linkedResourceDisambiguation = disambiguation
            } ?: return null
        }
        MusicBrainzResource.GENRE -> {
            if (genre == null) return null
            genre?.apply {
                linkedResourceId = id
                linkedResourceName = targetCredit.emptyToNull() ?: name
                linkedResourceDisambiguation = disambiguation
            } ?: return null
        }
        MusicBrainzResource.EVENT -> {
            if (event == null) return null
            event?.apply {
                linkedResourceId = id
                linkedResourceName = targetCredit.emptyToNull() ?: name
                linkedResourceDisambiguation = disambiguation
            } ?: return null
        }
        MusicBrainzResource.SERIES -> {
            if (series == null) return null
            series?.apply {
                linkedResourceId = id
                linkedResourceName = targetCredit.emptyToNull() ?: name
                linkedResourceDisambiguation = disambiguation
            } ?: return null
        }
        MusicBrainzResource.URL -> {
            if (url == null) return null
            url?.apply {
                linkedResourceId = id
                linkedResourceName = URLDecoder.decode(resource, "utf-8")
                linkedResourceDisambiguation = null
            } ?: return null
        }
        else -> return null
    }

    return RelationRoomModel(
        resourceId = resourceId,
        linkedResourceId = linkedResourceId,
        linkedResource = linkedTargetType,
        order = order,
        label = getHeader(),
        name = linkedResourceName,
        disambiguation = linkedResourceDisambiguation,
        attributes = getFormattedAttributesForDisplay(),
        additionalInfo = additionalInfo
    )
}
