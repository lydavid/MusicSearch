package ly.david.data.test.preferences

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import ly.david.musicsearch.shared.domain.collection.CollectionSortOption
import ly.david.musicsearch.shared.domain.history.HistorySortOption
import ly.david.musicsearch.shared.domain.image.ImagesSortOption
import ly.david.musicsearch.shared.domain.preferences.AppPreferences

open class NoOpAppPreferences : AppPreferences {
    override val theme: Flow<AppPreferences.Theme>
        get() = error("Not implemented")

    override fun setTheme(theme: AppPreferences.Theme) {
        // No-op.
    }

    override val useMaterialYou: Flow<Boolean>
        get() = error("Not implemented")

    override fun setUseMaterialYou(use: Boolean) {
        // No-op.
    }

    override val showMoreInfoInReleaseListItem: Flow<Boolean>
        get() = error("Not implemented")

    override fun setShowMoreInfoInReleaseListItem(show: Boolean) {
        // No-op.
    }

    override val sortReleaseGroupListItems: Flow<Boolean>
        get() = error("Not implemented")

    override fun setSortReleaseGroupListItems(show: Boolean) {
        // No-op.
    }

    override val showLocalCollections: Flow<Boolean>
        get() = error("Not implemented")

    override fun setShowLocalCollections(show: Boolean) {
        // No-op.
    }

    override val showRemoteCollections: Flow<Boolean>
        get() = error("Not implemented")

    override fun setShowRemoteCollections(show: Boolean) {
        // No-op.
    }

    override val historySortOption: Flow<HistorySortOption>
        get() = error("Not implemented")

    override fun setHistorySortOption(sort: HistorySortOption) {
        // No-op.
    }

    override val collectionSortOption: Flow<CollectionSortOption>
        get() = error("Not implemented")

    override fun setCollectionSortOption(sort: CollectionSortOption) {
        // No-op.
    }

    override val imagesSortOption: Flow<ImagesSortOption>
        get() = error("Not implemented")

    override fun setImagesSortOption(sort: ImagesSortOption) {
        // No-op.
    }

    override val showCrashReporterSettings: Boolean = false
    override val isCrashReportingEnabled: Flow<Boolean>
        get() = flowOf(false)

    override fun setEnableCrashReporting(enable: Boolean) {
        // No-op.
    }
}
