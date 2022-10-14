package ly.david.mbjc.data.persistence.relation

import androidx.room.ColumnInfo
import androidx.room.Entity
import java.net.URLDecoder
import ly.david.mbjc.data.Relation
import ly.david.mbjc.data.getDisplayNames
import ly.david.mbjc.data.getLifeSpanForDisplay
import ly.david.mbjc.data.network.MusicBrainzResource
import ly.david.mbjc.data.network.RelationMusicBrainzModel
import ly.david.mbjc.data.network.getFormattedAttributesForDisplay
import ly.david.mbjc.data.network.getHeader
import ly.david.mbjc.data.persistence.RoomModel
import ly.david.mbjc.ui.common.transformThisIfNotNullOrEmpty

// TODO: [low priority] recording/a53c97d7-5501-443b-baa3-cb282fc64275 returns "The Sound Factory West" twice.
//  web doesn't display it twice, so maybe we shouldn't either.

/**
 * The existence of this model for a [resourceId] should be enough to indicate that we have locally stored
 * all of its relationships since there's no pagination for relationships.
 * That's not actually true because it won't handle the case where a resource has no relationships.
 * It would make us always true to fetch it even though we've discovered there are none.
 */
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
        MusicBrainzResource.RELEASE_GROUP -> {
            if (releaseGroup == null) return null
            linkedResourceId = releaseGroup.id
            name = if (targetCredit.isNullOrEmpty()) {
                releaseGroup.name
            } else {
                targetCredit
            }
            disambiguation = releaseGroup.disambiguation
            additionalInfo = getLifeSpanForDisplay().transformThisIfNotNullOrEmpty { "($it)" }
        }
        MusicBrainzResource.RELEASE -> {
            if (release == null) return null
            linkedResourceId = release.id
            name = if (targetCredit.isNullOrEmpty()) {
                release.name
            } else {
                targetCredit
            }
            disambiguation = release.disambiguation
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
                label.name
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

        MusicBrainzResource.EVENT -> {
            if (event == null) return null
            linkedResourceId = event.id
            name = if (targetCredit.isNullOrEmpty()) {
                event.name
            } else {
                targetCredit
            }
            disambiguation = event.disambiguation
        }
        MusicBrainzResource.SERIES -> {
            if (series == null) return null
            linkedResourceId = series.id
            name = if (targetCredit.isNullOrEmpty()) {
                series.name
            } else {
                targetCredit
            }
            disambiguation = series.disambiguation
        }
        null -> return null
    }

    return RelationRoomModel(
        resourceId = resourceId,
        linkedResourceId = linkedResourceId,
        linkedResource = targetType,
        order = order,
        label = getHeader(),
        name = name,
        disambiguation = disambiguation,
        attributes = getFormattedAttributesForDisplay(),
        additionalInfo = additionalInfo
    )
}
