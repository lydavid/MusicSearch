package ly.david.data.core.artist

import ly.david.data.core.LifeSpan

data class ArtistForDetails(
    override val id: String,
    override val name: String,
    override val sortName: String,
    override val disambiguation: String?,
    override val type: String?,
    override val gender: String?,
    override val countryCode: String?,
    override val begin: String?,
    override val end: String?,
    override val ended: Boolean?,
    val imageUrl: String?,
) : Artist, LifeSpan
