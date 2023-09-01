package ly.david.data.domain.listitem

import ly.david.data.Artist
import ly.david.data.LifeSpanUiModel
import ly.david.data.network.ArtistMusicBrainzModel
import ly.david.data.room.artist.ArtistRoomModel
import ly.david.data.toLifeSpanUiModel

data class ArtistListItemModel(
    override val id: String,
    override val name: String,
    override val sortName: String,
    override val disambiguation: String? = null,
    override val type: String? = null,
    override val gender: String? = null,
    override val countryCode: String? = null,
    override val lifeSpan: LifeSpanUiModel? = null,
) : ListItemModel(), Artist

fun ArtistMusicBrainzModel.toArtistListItemModel() =
    ArtistListItemModel(
        id = id,
        name = name,
        sortName = sortName,
        disambiguation = disambiguation,
        type = type,
        gender = gender,
        countryCode = countryCode,
        lifeSpan = lifeSpan?.toLifeSpanUiModel()
    )

fun ArtistRoomModel.toArtistListItemModel() =
    ArtistListItemModel(
        id = id,
        name = name,
        sortName = sortName,
        disambiguation = disambiguation,
        type = type,
        gender = gender,
        countryCode = countryCode,
        lifeSpan = lifeSpan?.toLifeSpanUiModel()
    )
