package ly.david.mbjc.data.network

import com.squareup.moshi.Json
import ly.david.mbjc.data.LabelInfo
import ly.david.mbjc.data.Medium
import ly.david.mbjc.data.ReleaseGroup

data class MusicBrainzReleaseGroup(

    @Json(name = "id")
    override val id: String,
    @Json(name = "title")
    override val name: String = "",
    @Json(name = "first-release-date")
    override val firstReleaseDate: String = "",
    @Json(name = "disambiguation")
    override val disambiguation: String = "",

    // album, single, ep, other
    @Json(name = "primary-type")
    override val primaryType: String? = null,
//    @Json(name = "primary-type-id")
//    val primaryTypeId: String? = null, // only useful if we plan to allow linking to type

    // audio drama, audiobook, broadcast, compilation, dj-mix, interview, live, mixtape/street, remix, soundtrack, spokenword
    @Json(name = "secondary-types")
    override val secondaryTypes: List<String>? = null,
//    @Json(name = "secondary-type-ids")
//    val secondaryTypeIds: List<String>? = null,

    // Lookup: inc=artists; Browse: inc=artist-credits
    @Json(name = "artist-credit")
    val artistCredits: List<MusicBrainzArtistCredit>? = null,

    // inc=label
    @Json(name = "label-info")
    val labelInfoList: List<LabelInfo>? = null,

    // inc=media
    @Json(name = "media")
    val media: List<Medium>? = null,

    // lookup only, inc=releases
    @Json(name = "releases")
    val musicBrainzReleases: List<MusicBrainzRelease>? = null,
): ReleaseGroup, MusicBrainzData()
