package ly.david.data.network

import com.squareup.moshi.Json
import ly.david.data.ReleaseGroup

data class ReleaseGroupMusicBrainzModel(

    @Json(name = "id") override val id: String,
    @Json(name = "title") override val name: String = "",
    @Json(name = "first-release-date") override val firstReleaseDate: String = "",
    @Json(name = "disambiguation") override val disambiguation: String = "",
    @Json(name = "primary-type") override val primaryType: String? = null,
//    @Json(name = "primary-type-id")
//    val primaryTypeId: String? = null, // only useful if we plan to allow linking to type

    // audio drama, audiobook, broadcast, compilation, dj-mix, interview, live, mixtape/street, remix, soundtrack, spokenword
    @Json(name = "secondary-types") override val secondaryTypes: List<String>? = null,
//    @Json(name = "secondary-type-ids")
//    val secondaryTypeIds: List<String>? = null,

    // Lookup: inc=artists; Browse: inc=artist-credits
    @Json(name = "artist-credit") val artistCredits: List<ArtistCreditMusicBrainzModel>? = null,

    // inc=label
    @Json(name = "label-info") val labelInfoList: List<LabelInfo>? = null,

    // inc=media
    @Json(name = "media") val media: List<MediumMusicBrainzModel>? = null,

    // lookup only, inc=releases
    @Json(name = "releases") val releases: List<ReleaseMusicBrainzModel>? = null,

    @Json(name = "relations") val relations: List<RelationMusicBrainzModel>? = null
) : MusicBrainzModel(), ReleaseGroup
