package ly.david.musicsearch.shared.domain.preferences

import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.shared.domain.collection.CollectionSortOption
import ly.david.musicsearch.shared.domain.coverarts.CoverArtsSortOption
import ly.david.musicsearch.shared.domain.history.HistorySortOption

interface AppPreferences {

    enum class Theme {
        LIGHT,
        DARK,
        SYSTEM,
    }

    val theme: Flow<Theme>
    fun setTheme(theme: Theme)

    val useMaterialYou: Flow<Boolean>
    fun setUseMaterialYou(use: Boolean)

    val showMoreInfoInReleaseListItem: Flow<Boolean>
    fun setShowMoreInfoInReleaseListItem(show: Boolean)

    val sortReleaseGroupListItems: Flow<Boolean>
    fun setSortReleaseGroupListItems(show: Boolean)

    val showLocalCollections: Flow<Boolean>
    fun setShowLocalCollections(show: Boolean)

    val showRemoteCollections: Flow<Boolean>
    fun setShowRemoteCollections(show: Boolean)

    val historySortOption: Flow<HistorySortOption>
    fun setHistorySortOption(sort: HistorySortOption)

    val collectionSortOption: Flow<CollectionSortOption>
    fun setCollectionSortOption(sort: CollectionSortOption)

    val coverArtsSortOption: Flow<CoverArtsSortOption>
    fun setCoverArtsSortOption(sort: CoverArtsSortOption)
}
