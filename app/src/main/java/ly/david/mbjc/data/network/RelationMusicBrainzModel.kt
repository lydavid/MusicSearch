package ly.david.mbjc.data.network

import com.squareup.moshi.Json
import ly.david.mbjc.data.ILifeSpan
import ly.david.mbjc.data.LabelMusicBrainzModel
import ly.david.mbjc.data.WorkMusicBrainzModel
import ly.david.mbjc.ui.common.transformThisIfNotNullOrEmpty

internal data class RelationMusicBrainzModel(
    @Json(name = "type") val type: String,
    @Json(name = "target-type") val targetType: MusicBrainzResource, // artist, place, work, label
    @Json(name = "target-credit") val targetCredit: String? = null, // prefer this credit over object's name if it exists

    @Json(name = "attributes") val attributes: List<String>? = null, // strings, task
    @Json(name = "attribute-values") val attributeValues: AttributeValue? = null, // "director & organizer"

    @Json(name = "begin") override val begin: String?,
    @Json(name = "end") override val end: String?,
    @Json(name = "ended") override val ended: Boolean?,

    @Json(name = "artist") val artist: ArtistMusicBrainzModel? = null, // could be composer, arranger, etc
    @Json(name = "label") val label: LabelMusicBrainzModel? = null,
    @Json(name = "work") val work: WorkMusicBrainzModel? = null,
    @Json(name = "place") val place: PlaceMusicBrainzModel? = null,
    @Json(name = "url") val url: UrlMusicBrainzModel? = null,
    @Json(name = "recording") val recording: RecordingMusicBrainzModel? = null,
): ILifeSpan

internal data class AttributeValue(
    @Json(name = "task")
    val task: String? = null
)

/**
 * Some [RelationMusicBrainzModel.attributes] such as "strings" does not have
 * a corresponding [RelationMusicBrainzModel.attributeValues].
 * Others such as "task" does. List these with their corresponding values.
 *
 * All attributes/attribute-value pairs should be comma-separated.
 */
internal fun RelationMusicBrainzModel.getFormattedAttributesForDisplay(): String =
    attributes?.joinToString(", ") { attribute ->
        if (attribute == "task") {
            attribute + attributeValues?.task.transformThisIfNotNullOrEmpty { ": $it" }
        } else {
            attribute
        }
    }.orEmpty()
