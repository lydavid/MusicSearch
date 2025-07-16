package ly.david.musicsearch.data.musicbrainz.models.core

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ly.david.musicsearch.data.musicbrainz.models.MediumMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.common.AliasMusicBrainzNetworkModel
import ly.david.musicsearch.data.musicbrainz.models.common.ArtistCreditMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.relation.RelationMusicBrainzModel
import ly.david.musicsearch.shared.domain.releasegroup.ReleaseGroup

@Serializable
data class ReleaseGroupMusicBrainzNetworkModel(

    @SerialName("id") override val id: String,
    @SerialName("title") override val name: String = "",
    @SerialName("disambiguation") override val disambiguation: String = "",
    @SerialName("first-release-date") override val firstReleaseDate: String = "",
    @SerialName("primary-type") override val primaryType: String? = null,
    @SerialName("primary-type-id") val primaryTypeId: String? = null,
    @SerialName("secondary-types") override val secondaryTypes: List<String>? = null,
    @SerialName("secondary-type-ids") val secondaryTypeIds: List<String>? = null,

    // Lookup: inc=artists; Browse: inc=artist-credits
    @SerialName("artist-credit") val artistCredits: List<ArtistCreditMusicBrainzModel>? = null,

    // inc=label
    @SerialName("label-info") val labelInfoList: List<LabelInfo>? = null,

    // inc=media
    @SerialName("media") val media: List<MediumMusicBrainzModel>? = null,

    // lookup only, inc=releases
    @SerialName("releases") val releases: List<ReleaseMusicBrainzNetworkModel>? = null,

    @SerialName("relations") val relations: List<RelationMusicBrainzModel>? = null,
    @SerialName("aliases") override val aliases: List<AliasMusicBrainzNetworkModel>? = null,
) : MusicBrainzNetworkModel, ReleaseGroup
