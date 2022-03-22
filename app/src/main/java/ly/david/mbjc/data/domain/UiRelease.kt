package ly.david.mbjc.data.domain

import ly.david.mbjc.data.Release
import ly.david.mbjc.data.network.MusicBrainzRelease
import ly.david.mbjc.data.persistence.RoomRelease

data class UiRelease(
    override val id: String,
    override val name: String,
    override val disambiguation: String,
    override val date: String? = null,
    override val status: String? = null,
    override val barcode: String? = null,
    override val statusId: String? = null,
    override val country: String? = null,
    override val packaging: String? = null,
    override val packagingId: String? = null,
    override val asin: String? = null,
    override val quality: String? = null

    // TODO: string: 170xCD, formatted from network response. Don't need the exact tracks yet
    //  or don't store this. We will make a db call to fetch it, and update live. More flexible this way.
    //  and we will use this same data class when we do look-up in release screen
    //  If we don't find tracks, then we can call network to get and store them.
    //  When we browsed for releases in a release group, we only ask for media, not recordings, to save bandwidth.
    //  Clicking into a release, we can lookup, and include recordings.
    // TODO: string: 10 + 19 + ...

) : UiData(), Release

fun MusicBrainzRelease.toUiRelease() =
    UiRelease(
        id = id,
        name = name,
        disambiguation = disambiguation,
        date = date,
        status = status,
        barcode = barcode,
        statusId = statusId,
        country = country,
        packaging = packaging,
        packagingId = packagingId,
        asin = asin,
        quality = quality
    )

fun RoomRelease.toUiRelease() =
    UiRelease(
        id = id,
        name = name,
        disambiguation = disambiguation,
        date = date,
        status = status,
        barcode = barcode,
        statusId = statusId,
        country = country,
        packaging = packaging,
        packagingId = packagingId,
        asin = asin,
        quality = quality
    )
