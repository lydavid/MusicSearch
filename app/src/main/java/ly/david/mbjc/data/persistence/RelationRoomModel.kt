package ly.david.mbjc.data.persistence

import androidx.room.ColumnInfo
import androidx.room.Entity
import java.net.URLDecoder
import ly.david.mbjc.data.Relation
import ly.david.mbjc.data.getDisplayNames
import ly.david.mbjc.data.getLifeSpanForDisplay
import ly.david.mbjc.data.network.MusicBrainzResource
import ly.david.mbjc.data.network.RelationMusicBrainzModel
import ly.david.mbjc.data.network.getFormattedAttributesForDisplay
import ly.david.mbjc.data.network.getLabel
import ly.david.mbjc.ui.common.transformThisIfNotNullOrEmpty

// TODO: recording/a53c97d7-5501-443b-baa3-cb282fc64275 returns "The Sound Factory West" twice.
//  web doesn't display it twice, so maybe we shouldn't either.
@Entity(
    tableName = "relations",
    primaryKeys = ["resource_id", "linked_resource_id", "order"],
)
internal data class RelationRoomModel(
    @ColumnInfo(name = "resource_id")
    val resourceId: String,

    // TODO: can we make it nullable so that we don't pass url id?
    @ColumnInfo(name = "linked_resource_id")
    override val linkedResourceId: String,

    @ColumnInfo(name = "linked_resource")
    override val linkedResource: MusicBrainzResource,

    // TODO: an artist can appear multiple times similar to artist credits
    //  for now, we'll use order which is the order we insert it. But we probably won't display it in this order.
    //  This is not necessarily the order it's displayed on MB website.
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
) : Relation

/**
 * We cannot guarantee that a [RelationRoomModel] will be created in the scenario that target-type points to a resource
 * but that object is null. It's possible that this is never the case, but our models are currently structured such
 * that any of them are nullable.
 */
internal fun RelationMusicBrainzModel.toRelationRoomModel(
    resourceId: String,
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
        MusicBrainzResource.AREA -> {
            if (area == null) return null
            linkedResourceId = area.id
            name = area.name
            disambiguation = area.disambiguation
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
        MusicBrainzResource.INSTRUMENT -> {
            if (instrument == null) return null
            linkedResourceId = instrument.id
            name = if (targetCredit.isNullOrEmpty()) {
                instrument.name
            } else {
                targetCredit
            }
            disambiguation = instrument.disambiguation
        }

        MusicBrainzResource.GENRE -> {
            if (genre == null) return null
            linkedResourceId = genre.id
            name = if (targetCredit.isNullOrEmpty()) {
                genre.name
            } else {
                targetCredit
            }
            disambiguation = genre.disambiguation
        }

        MusicBrainzResource.URL -> {
            if (url == null) return null
            linkedResourceId = url.id
            name = URLDecoder.decode(url.resource, "utf-8")
            disambiguation = null
        }

        // TODO: handle rest

        else -> {
            return null
        }
    }

    return RelationRoomModel(
        resourceId = resourceId,
        linkedResourceId = linkedResourceId,
        linkedResource = targetType,
        order = order,
        label = getLabel(),
        name = name,
        disambiguation = disambiguation,
        attributes = getFormattedAttributesForDisplay(),
        additionalInfo = additionalInfo
    )
}
