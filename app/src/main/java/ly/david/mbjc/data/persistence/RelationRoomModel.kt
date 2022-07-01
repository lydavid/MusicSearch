package ly.david.mbjc.data.persistence

import androidx.room.ColumnInfo
import androidx.room.Entity
import ly.david.mbjc.data.Relation
import ly.david.mbjc.data.getDisplayNames
import ly.david.mbjc.data.getLifeSpanForDisplay
import ly.david.mbjc.data.network.MusicBrainzResource
import ly.david.mbjc.data.network.RelationMusicBrainzModel
import ly.david.mbjc.data.network.getFormattedAttributesForDisplay
import ly.david.mbjc.ui.common.transformThisIfNotNullOrEmpty

@Entity(
    tableName = "relations",
    primaryKeys = ["resource_id", "linked_resource_id", "order"],
)
internal data class RelationRoomModel(
    @ColumnInfo(name = "resource_id")
    override val resourceId: String,

    // Default is because this used to be for recordings only.
    // We expect to always pass in an appropriate resource, so it shouldn't affect us.
    @ColumnInfo(name = "resource", defaultValue = "recording")
    override val resource: MusicBrainzResource,

    // TODO: can we make it nullable so that we don't pass url id?
    @ColumnInfo(name = "linked_resource_id")
    override val linkedResourceId: String,

    @ColumnInfo(name = "linked_resource")
    override val linkedResource: MusicBrainzResource,

    // TODO: an artist can appear multiple times similar to artist credits
    //  for now, we'll use order which is the order we insert it. But we probably won't display it in this order.
    //  This is not necessarily the order it's displayed on MB website.
    @ColumnInfo(name = "order")
    override val order: Int,

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
) : Relation

/**
 * We cannot guarantee that a [RelationRoomModel] will be created in the scenario that target-type points to a resource
 * but that object is null. It's possible that this is never the case, but our models are currently structured such
 * that any of them are nullable.
 */
internal fun RelationMusicBrainzModel.toRelationRoomModel(
    resourceId: String,
    resource: MusicBrainzResource,
    order: Int,
): RelationRoomModel? {

    val linkedResourceId: String
    val name: String
    val disambiguation: String?
    var additionalInfo: String? = null
    when (targetType) {
        MusicBrainzResource.ARTIST -> {
            if (artist == null) return null
            linkedResourceId = artist.id
            name = if (targetCredit.isNullOrEmpty()) {
                artist.name
            } else {
                targetCredit
            }
            disambiguation = artist.disambiguation
            additionalInfo = getLifeSpanForDisplay().transformThisIfNotNullOrEmpty { "($it)" }
        }
        MusicBrainzResource.RECORDING -> {
            if (recording == null) return null
            linkedResourceId = recording.id
            name = if (targetCredit.isNullOrEmpty()) {
                recording.name
            } else {
                targetCredit
            }
            disambiguation = recording.disambiguation
            additionalInfo = recording.artistCredits.getDisplayNames().transformThisIfNotNullOrEmpty { "by $it" } +
                getLifeSpanForDisplay().transformThisIfNotNullOrEmpty { "($it)" }
        }
        MusicBrainzResource.LABEL -> {
            if (label == null) return null
            linkedResourceId = label.id
            name = if (targetCredit.isNullOrEmpty()) {
                label.name.orEmpty()
            } else {
                targetCredit
            }
            disambiguation = label.disambiguation
        }
        MusicBrainzResource.PLACE -> {
            if (place == null) return null
            linkedResourceId = place.id
            name = if (targetCredit.isNullOrEmpty()) {
                place.name
            } else {
                targetCredit
            }
            disambiguation = place.disambiguation
        }
        MusicBrainzResource.WORK -> {
            if (work == null) return null
            linkedResourceId = work.id
            name = if (targetCredit.isNullOrEmpty()) {
                work.name
            } else {
                targetCredit
            }
            disambiguation = work.disambiguation
        }
        // TODO: handle urls, should just open that url in browser
        //  since we want to support full offline after returning to a screen, we need to save this url.
        //  Either save the url in the relation object, or store an id to the url in a urls table.
        //  Upon navigation to a "url screen", we will instead open the url in the user's browser of choice.
        MusicBrainzResource.URL -> {
            if (url == null) return null
            linkedResourceId = url.id
            name = url.resource
            disambiguation = null
        }

        // TODO: handle rest

        else -> {
            return null
        }
    }

    return RelationRoomModel(
        resourceId = resourceId,
        resource = resource,
        linkedResourceId = linkedResourceId,
        linkedResource = targetType,
        order = order,
        label = type,
        name = name,
        disambiguation = disambiguation,
        attributes = getFormattedAttributesForDisplay(),
        additionalInfo = additionalInfo
    )
}
