package ly.david.musicsearch.data.musicbrainz.models.core

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ly.david.musicsearch.data.musicbrainz.models.common.AliasMusicBrainzNetworkModel
import ly.david.musicsearch.data.musicbrainz.models.relation.RelationMusicBrainzModel
import ly.david.musicsearch.shared.domain.work.Work
import ly.david.musicsearch.shared.domain.work.WorkAttribute

@Serializable
data class WorkMusicBrainzNetworkModel(
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
    @SerialName("aliases") override val aliases: List<AliasMusicBrainzNetworkModel>? = null,
) : MusicBrainzNetworkModel, Work

@Serializable
data class WorkAttributeMusicBrainzModel(
    @SerialName("type") override val type: String,
    @SerialName("type-id") override val typeId: String,
    @SerialName("value") override val value: String,
) : WorkAttribute
