package ly.david.musicsearch.shared.domain.url

import ly.david.musicsearch.shared.domain.listitem.RelationListItemModel

interface LookupUrlRepository {
    suspend fun getEntitiesLinkedToUrl(
        url: String,
        searchLocalOnly: Boolean,
    ): List<RelationListItemModel>
}
