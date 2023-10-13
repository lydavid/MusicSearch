package ly.david.data.musicbrainz

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ly.david.musicsearch.data.core.releasegroup.ReleaseGroup

@Serializable
data class ReleaseGroupMusicBrainzModel(

    @SerialName("id") override val id: String,
    @SerialName("title") override val name: String = "",
    @SerialName("first-release-date") override val firstReleaseDate: String = "",
    @SerialName("disambiguation") override val disambiguation: String = "",
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
    @SerialName("releases") val releases: List<ReleaseMusicBrainzModel>? = null,

    @SerialName("relations") val relations: List<RelationMusicBrainzModel>? = null,
) : MusicBrainzModel(), ReleaseGroup
