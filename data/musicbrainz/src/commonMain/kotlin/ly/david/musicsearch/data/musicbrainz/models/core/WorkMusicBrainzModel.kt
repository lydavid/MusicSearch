package ly.david.musicsearch.data.musicbrainz.models.core

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ly.david.musicsearch.core.models.work.Work
import ly.david.musicsearch.core.models.work.WorkAttribute
import ly.david.musicsearch.data.musicbrainz.models.relation.RelationMusicBrainzModel

@Serializable
data class WorkMusicBrainzModel(
    @SerialName("id") override val id: String,
    @SerialName("title") override val name: String,
    @SerialName("disambiguation") override val disambiguation: String? = null,
    @SerialName("type") override val type: String? = null,
    @SerialName("type-id") val typeId: String? = null,

    /**
     * This is null when languages is empty.
     */
    @SerialName("language") override val language: String? = null,
//    @SerialName("languages") override val languages: List<String>? = null,

    @SerialName("iswcs") override val iswcs: List<String>? = null,

    @SerialName("attributes") val attributes: List<WorkAttributeMusicBrainzModel>? = null,

    // search API returns relations without target-type
    @SerialName("relations") val relations: List<RelationMusicBrainzModel>? = null,
) : MusicBrainzModel(), Work

@Serializable
data class WorkAttributeMusicBrainzModel(
    @SerialName("type") override val type: String,
    @SerialName("type-id") override val typeId: String,
    @SerialName("value") override val value: String,
) : WorkAttribute

const val ARTIFICIAL_LANGUAGE_CODE = "qaa"
