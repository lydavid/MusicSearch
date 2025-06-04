package ly.david.data.test.preferences

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import ly.david.musicsearch.shared.domain.DEFAULT_IMAGES_GRID_PADDING_DP
import ly.david.musicsearch.shared.domain.DEFAULT_NUMBER_OF_IMAGES_PER_ROW
import ly.david.musicsearch.shared.domain.collection.CollectionSortOption
import ly.david.musicsearch.shared.domain.history.HistorySortOption
import ly.david.musicsearch.shared.domain.image.ImagesSortOption
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
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

    override val observeSeedColor: Flow<Int>
        get() = error("Not implemented")

    override fun setSeedColor(seedColor: Int) {
        // No-op.
    }

    override val showMoreInfoInReleaseListItem: Flow<Boolean>
        get() = flowOf(true)

    override fun setShowMoreInfoInReleaseListItem(show: Boolean) {
        // No-op.
    }

    override val sortReleaseGroupListItems: Flow<Boolean>
        get() = flowOf(true)

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

    override val isDeveloperMode: Flow<Boolean>
        get() = flowOf(false)

    override fun setDeveloperMode(enable: Boolean) {
        // No-op.
    }

    override val observeNumberOfImagesPerRow: Flow<Int>
        get() = flowOf(DEFAULT_NUMBER_OF_IMAGES_PER_ROW)

    override fun setNumberOfImagesPerRow(numberOfImagesPerRow: Int) {
        // No-op.
    }

    override val observeImagesGridPaddingDp: Flow<Int>
        get() = flowOf(DEFAULT_IMAGES_GRID_PADDING_DP)

    override fun setImagesGridPaddingDp(padding: Int) {
        // No-op.
    }

    override val observeCollaborationEntityType: Flow<MusicBrainzEntity>
        get() = flowOf()

    override fun setCollaborationEntityType(entity: MusicBrainzEntity) {
        // No-op.
    }
}
