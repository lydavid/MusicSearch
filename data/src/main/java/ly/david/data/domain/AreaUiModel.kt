package ly.david.data.domain

import ly.david.data.Area
import ly.david.data.AreaType
import ly.david.data.LifeSpan
import ly.david.data.network.AreaMusicBrainzModel
import ly.david.data.persistence.area.AreaRoomModel
import ly.david.data.persistence.area.AreaWithReleaseDateOld
import ly.david.data.persistence.release.AreaWithReleaseDate

data class AreaUiModel(
    override val id: String,
    override val name: String,
    override val disambiguation: String? = "",
    override val type: String? = "",
    override val lifeSpan: LifeSpan? = null,
    val iso_3166_1_codes: List<String>? = null,
    val date: String? = null
) : Area, UiModel()

internal fun AreaMusicBrainzModel.toAreaUiModel() =
    AreaUiModel(
        id = id,
        name = name,
        disambiguation = disambiguation,
        type = type,
        lifeSpan = lifeSpan,
        iso_3166_1_codes = iso_3166_1_codes
    )

fun AreaRoomModel.toAreaUiModel(iso_3166_1_codes: List<String>? = null) =
    AreaUiModel(
        id = id,
        name = name,
        disambiguation = disambiguation,
        type = type,
        lifeSpan = lifeSpan,
        iso_3166_1_codes = iso_3166_1_codes
    )

fun AreaWithReleaseDateOld.toAreaUiModel() =
    AreaUiModel(
        id = area.id,
        name = area.name,
        disambiguation = area.disambiguation,
        type = area.type,
        lifeSpan = area.lifeSpan,
        iso_3166_1_codes = countryCodes.map { it.code },
        date = date
    )

fun AreaWithReleaseDate.toAreaUiModel() =
    AreaUiModel(
        id = area.id,
        name = area.name,
        disambiguation = area.disambiguation,
        type = area.type,
        lifeSpan = area.lifeSpan,
        iso_3166_1_codes = countryCodes.map { it.code },
        date = releaseCountry.date
    )

fun AreaUiModel.showReleases(): Boolean =
    type == AreaType.COUNTRY || name == AreaType.WORLDWIDE
