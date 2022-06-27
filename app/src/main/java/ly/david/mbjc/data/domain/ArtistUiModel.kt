package ly.david.mbjc.data.domain

import ly.david.mbjc.data.Artist
import ly.david.mbjc.data.LifeSpan
import ly.david.mbjc.data.network.ArtistMusicBrainzModel
import ly.david.mbjc.data.persistence.ArtistRoomModel

internal data class ArtistUiModel(
    override val id: String,
    override val name: String,
    override val sortName: String,
    override val disambiguation: String? = null,
    override val type: String? = null,
    override val gender: String? = null,
    override val countryCode: String? = null,
    override val lifeSpan: LifeSpan? = null
) : UiModel(), Artist

internal fun ArtistMusicBrainzModel.toArtistUiModel() =
    ArtistUiModel(
        id = id,
        name = name,
        sortName = sortName,
        disambiguation = disambiguation,
        type = type,
        gender = gender,
        countryCode = countryCode,
        lifeSpan = lifeSpan
    )

internal fun ArtistRoomModel.toArtistUiModel() =
    ArtistUiModel(
        id = id,
        name = name,
        sortName = sortName,
        disambiguation = disambiguation,
        type = type,
        gender = gender,
        countryCode = countryCode,
        lifeSpan = lifeSpan
    )
