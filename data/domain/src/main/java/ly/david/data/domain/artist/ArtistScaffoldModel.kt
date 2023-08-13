package ly.david.data.domain.artist

import ly.david.data.Artist
import ly.david.data.LifeSpan
import ly.david.data.domain.listitem.RelationListItemModel
import ly.david.data.domain.listitem.toRelationListItemModel
import ly.david.data.room.artist.ArtistWithAllData

data class ArtistScaffoldModel(
    override val id: String,
    override val name: String,
    override val sortName: String,
    override val disambiguation: String? = null,
    override val type: String? = null,
    override val gender: String? = null,
    override val countryCode: String? = null,
    override val lifeSpan: LifeSpan? = null,
    val urls: List<RelationListItemModel> = listOf(),
    val imageUrl: String? = null,
) : Artist

internal fun ArtistWithAllData.toArtistScaffoldModel() =
    ArtistScaffoldModel(
        id = artist.id,
        name = artist.name,
        sortName = artist.sortName,
        disambiguation = artist.disambiguation,
        type = artist.type,
        gender = artist.gender,
        countryCode = artist.countryCode,
        lifeSpan = artist.lifeSpan,
        urls = urls.map { it.relation.toRelationListItemModel() },
        imageUrl = largeUrl
    )
