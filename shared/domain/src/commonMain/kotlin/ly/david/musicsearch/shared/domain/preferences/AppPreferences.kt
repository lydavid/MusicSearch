package ly.david.musicsearch.shared.domain.preferences

import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.shared.domain.collection.CollectionSortOption
import ly.david.musicsearch.shared.domain.history.HistorySortOption
import ly.david.musicsearch.shared.domain.image.ImagesSortOption
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity

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

    val observeSeedColor: Flow<Int>
    fun setSeedColor(seedColor: Int)

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

    val imagesSortOption: Flow<ImagesSortOption>
    fun setImagesSortOption(sort: ImagesSortOption)

    val showCrashReporterSettings: Boolean
    val isCrashReportingEnabled: Flow<Boolean>
    fun setEnableCrashReporting(enable: Boolean)

    val isDeveloperMode: Flow<Boolean>
    fun setDeveloperMode(enable: Boolean)

    val observeNumberOfImagesPerRow: Flow<Int>
    fun setNumberOfImagesPerRow(numberOfImagesPerRow: Int)

    val observeImagesGridPaddingDp: Flow<Int>
    fun setImagesGridPaddingDp(padding: Int)

    val observeCollaborationEntityType: Flow<MusicBrainzEntity>
    fun setCollaborationEntityType(entity: MusicBrainzEntity)
}
