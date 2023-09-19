package ly.david.data.domain.artist

import ly.david.data.domain.common.LifeSpanUiModel
import ly.david.data.domain.listitem.RelationListItemModel
import ly.david.data.domain.listitem.toRelationListItemModel
import lydavidmusicsearchdatadatabase.Artist
import lydavidmusicsearchdatadatabase.Mb_relation

data class ArtistScaffoldModel(
    override val id: String,
    override val name: String,
    override val sortName: String,
    override val disambiguation: String? = null,
    override val type: String? = null,
    override val gender: String? = null,
    override val countryCode: String? = null,
    override val lifeSpan: LifeSpanUiModel? = null,
    val urls: List<RelationListItemModel> = listOf(),
    val imageUrl: String? = null,
) : ly.david.data.core.Artist

internal fun Artist.toArtistScaffoldModel(
    imageUrl: String?,
    urls: List<Mb_relation>,
) = ArtistScaffoldModel(
    id = id,
    name = name,
    sortName = sort_name,
    disambiguation = disambiguation,
    type = type,
    gender = gender,
    countryCode = country_code,
    lifeSpan = LifeSpanUiModel(
        begin = begin,
        end = end,
        ended = ended,
    ),
    urls = urls.map { it.toRelationListItemModel() },
    imageUrl = imageUrl
)
