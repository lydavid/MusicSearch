package ly.david.musicsearch.data.musicbrainz.models.core

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ly.david.musicsearch.data.musicbrainz.models.common.AliasMusicBrainzNetworkModel
import ly.david.musicsearch.data.musicbrainz.models.common.LifeSpanMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.relation.RelationMusicBrainzModel
import ly.david.musicsearch.shared.domain.event.Event

@Serializable
data class EventMusicBrainzNetworkModel(
    @SerialName("id") override val id: String,
    @SerialName("name") override val name: String,
    @SerialName("disambiguation") override val disambiguation: String? = null,
    @SerialName("type") val type: String? = null,
    @SerialName("type-id") val typeId: String? = null,
    @SerialName("time") override val time: String? = null,
    @SerialName("cancelled") override val cancelled: Boolean? = null,
    @SerialName("life-span") override val lifeSpan: LifeSpanMusicBrainzModel? = null,

    // search API returns relations without target-type
    @SerialName("relations") override val relations: List<RelationMusicBrainzModel>? = null,
    @SerialName("aliases") override val aliases: List<AliasMusicBrainzNetworkModel>? = null,
    @SerialName("genres") override val genres: List<GenreMusicBrainzNetworkModel>? = null,
    @SerialName("tags") override val tags: List<TagMusicBrainzNetworkModel>? = null,
    @SerialName("user-genres") override val userGenres: List<GenreMusicBrainzNetworkModel>? = null,
    @SerialName("user-tags") override val userTags: List<TagMusicBrainzNetworkModel>? = null,
) : MusicBrainzNetworkModel, Event
