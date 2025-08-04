package ly.david.musicsearch.shared.feature.details.alias

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import ly.david.musicsearch.shared.domain.alias.BasicAlias

internal fun ImmutableList<BasicAlias>?.filterAliases(query: String): ImmutableList<BasicAlias> {
    return this?.filter { alias ->
        val searchText = query.lowercase()
        listOf(
            alias.name,
            alias.locale,
        ).any { it.lowercase().contains(searchText) }
    }.orEmpty().toPersistentList()
}
