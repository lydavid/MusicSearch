package ly.david.musicsearch.core.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ly.david.musicsearch.core.logging.crash.CrashReporterSettings
import ly.david.musicsearch.shared.domain.DEFAULT_IMAGES_GRID_PADDING_DP
import ly.david.musicsearch.shared.domain.DEFAULT_NUMBER_OF_IMAGES_PER_ROW
import ly.david.musicsearch.shared.domain.DEFAULT_SEED_COLOR_INT
import ly.david.musicsearch.shared.domain.collection.CollectionSortOption
import ly.david.musicsearch.shared.domain.history.HistorySortOption
import ly.david.musicsearch.shared.domain.image.ImagesSortOption
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.network.resourceUri
import ly.david.musicsearch.shared.domain.network.toMusicBrainzEntity
import ly.david.musicsearch.shared.domain.preferences.AppPreferences

private const val THEME_KEY = "theme"
private val THEME_PREFERENCE = stringPreferencesKey(THEME_KEY)
private const val USE_MATERIAL_YOU_KEY = "useMaterialYou"
private val USE_MATERIAL_YOU_PREFERENCE = booleanPreferencesKey(USE_MATERIAL_YOU_KEY)
private const val SEED_COLOR_KEY = "seedColor"
private val SEED_COLOR_PREFERENCE = intPreferencesKey(SEED_COLOR_KEY)
private const val SHOW_MORE_INFO_IN_RELEASE_LIST_ITEM_KEY = "showMoreInfoInReleaseListItem"
private val SHOW_MORE_INFO_IN_RELEASE_LIST_ITEM_PREFERENCE =
    booleanPreferencesKey(SHOW_MORE_INFO_IN_RELEASE_LIST_ITEM_KEY)
private const val SORT_RELEASE_GROUP_LIST_ITEMS = "sortReleaseGroupListItems"
private val SORT_RELEASE_GROUP_LIST_ITEMS_PREFERENCE =
    booleanPreferencesKey(SORT_RELEASE_GROUP_LIST_ITEMS)
private const val SHOW_LOCAL_COLLECTIONS = "showLocalCollections"
private val SHOW_LOCAL_COLLECTIONS_PREFERENCE =
    booleanPreferencesKey(SHOW_LOCAL_COLLECTIONS)
private const val SHOW_REMOTE_COLLECTIONS = "showRemoteCollections"
private val SHOW_REMOTE_COLLECTIONS_PREFERENCE =
    booleanPreferencesKey(SHOW_REMOTE_COLLECTIONS)

internal class AppPreferencesImpl(
    private val preferencesDataStore: DataStore<Preferences>,
    private val coroutineScope: CoroutineScope,
    private val crashReporterSettings: CrashReporterSettings,
) : AppPreferences {

    override val theme: Flow<AppPreferences.Theme>
        get() = preferencesDataStore.data
            .map {
                AppPreferences.Theme.valueOf(it[THEME_PREFERENCE] ?: AppPreferences.Theme.SYSTEM.name)
            }
            .distinctUntilChanged()

    override fun setTheme(theme: AppPreferences.Theme) {
        coroutineScope.launch {
            preferencesDataStore.edit {
                it[THEME_PREFERENCE] = theme.name
            }
        }
    }

    override val useMaterialYou: Flow<Boolean>
        get() = preferencesDataStore.data
            .map {
                it[USE_MATERIAL_YOU_PREFERENCE] ?: false
            }
            .distinctUntilChanged()

    override fun setUseMaterialYou(use: Boolean) {
        coroutineScope.launch {
            preferencesDataStore.edit {
                it[USE_MATERIAL_YOU_PREFERENCE] = use
            }
        }
    }

    override val observeSeedColor: Flow<Int>
        get() = preferencesDataStore.data
            .map {
                it[SEED_COLOR_PREFERENCE] ?: DEFAULT_SEED_COLOR_INT
            }
            .distinctUntilChanged()

    override fun setSeedColor(seedColor: Int) {
        coroutineScope.launch {
            preferencesDataStore.edit {
                it[SEED_COLOR_PREFERENCE] = seedColor
            }
        }
    }

    override val showMoreInfoInReleaseListItem: Flow<Boolean>
        get() = preferencesDataStore.data
            .map {
                it[SHOW_MORE_INFO_IN_RELEASE_LIST_ITEM_PREFERENCE] != false
            }
            .distinctUntilChanged()

    override fun setShowMoreInfoInReleaseListItem(show: Boolean) {
        coroutineScope.launch {
            preferencesDataStore.edit {
                it[SHOW_MORE_INFO_IN_RELEASE_LIST_ITEM_PREFERENCE] = show
            }
        }
    }

    override val sortReleaseGroupListItems: Flow<Boolean>
        get() = preferencesDataStore.data
            .map {
                it[SORT_RELEASE_GROUP_LIST_ITEMS_PREFERENCE] != false
            }
            .distinctUntilChanged()

    override fun setSortReleaseGroupListItems(show: Boolean) {
        coroutineScope.launch {
            preferencesDataStore.edit {
                it[SORT_RELEASE_GROUP_LIST_ITEMS_PREFERENCE] = show
            }
        }
    }

    override val showLocalCollections: Flow<Boolean>
        get() = preferencesDataStore.data
            .map {
                it[SHOW_LOCAL_COLLECTIONS_PREFERENCE] ?: true
            }
            .distinctUntilChanged()

    override fun setShowLocalCollections(show: Boolean) {
        coroutineScope.launch {
            preferencesDataStore.edit {
                it[SHOW_LOCAL_COLLECTIONS_PREFERENCE] = show
            }
        }
    }

    override val showRemoteCollections: Flow<Boolean>
        get() = preferencesDataStore.data
            .map {
                it[SHOW_REMOTE_COLLECTIONS_PREFERENCE] ?: true
            }
            .distinctUntilChanged()

    override fun setShowRemoteCollections(show: Boolean) {
        coroutineScope.launch {
            preferencesDataStore.edit {
                it[SHOW_REMOTE_COLLECTIONS_PREFERENCE] = show
            }
        }
    }

    // region History
    private val historySortOptionPreference = stringPreferencesKey("historySortOption")

    override val historySortOption: Flow<HistorySortOption>
        get() = preferencesDataStore.data
            .map {
                HistorySortOption.valueOf(it[historySortOptionPreference] ?: HistorySortOption.RECENTLY_VISITED.name)
            }

    override fun setHistorySortOption(sort: HistorySortOption) {
        coroutineScope.launch {
            preferencesDataStore.edit {
                it[historySortOptionPreference] = sort.name
            }
        }
    }
    // endregion

    // region Collection
    private val collectionSortOptionPreference = stringPreferencesKey("collectionSortOption")

    override val collectionSortOption: Flow<CollectionSortOption>
        get() = preferencesDataStore.data
            .map {
                CollectionSortOption.valueOf(
                    it[collectionSortOptionPreference] ?: CollectionSortOption.ALPHABETICALLY.name,
                )
            }

    override fun setCollectionSortOption(sort: CollectionSortOption) {
        coroutineScope.launch {
            preferencesDataStore.edit {
                it[collectionSortOptionPreference] = sort.name
            }
        }
    }
    // endregion

    // region Cover Arts
    private val coverArtsSortOptionPreference = stringPreferencesKey("coverArtsSortOption")

    override val imagesSortOption: Flow<ImagesSortOption>
        get() = preferencesDataStore.data
            .map {
                ImagesSortOption.valueOf(
                    it[coverArtsSortOptionPreference] ?: ImagesSortOption.RECENTLY_ADDED.name,
                )
            }

    override fun setImagesSortOption(sort: ImagesSortOption) {
        coroutineScope.launch {
            preferencesDataStore.edit {
                it[coverArtsSortOptionPreference] = sort.name
            }
        }
    }
    // endregion

    override val showCrashReporterSettings: Boolean = crashReporterSettings.showCrashReporterSettings

    override val isCrashReportingEnabled: Flow<Boolean>
        get() = crashReporterSettings.isCrashReportingEnabled

    override fun setEnableCrashReporting(enable: Boolean) {
        crashReporterSettings.enableCrashReporting(enable)
    }

    private val isDeveloperModePreference = booleanPreferencesKey("isDeveloperMode")
    override val isDeveloperMode: Flow<Boolean>
        get() = preferencesDataStore.data
            .map {
                it[isDeveloperModePreference] == true
            }
            .distinctUntilChanged()

    override fun setDeveloperMode(enable: Boolean) {
        coroutineScope.launch {
            preferencesDataStore.edit {
                it[isDeveloperModePreference] = enable
            }
        }
    }

    private val numberOfImagesPerRowPreference = intPreferencesKey("numberOfImagesPerRow")
    override val observeNumberOfImagesPerRow: Flow<Int>
        get() = preferencesDataStore.data
            .map {
                it[numberOfImagesPerRowPreference] ?: DEFAULT_NUMBER_OF_IMAGES_PER_ROW
            }

    override fun setNumberOfImagesPerRow(numberOfImagesPerRow: Int) {
        coroutineScope.launch {
            preferencesDataStore.edit {
                it[numberOfImagesPerRowPreference] = numberOfImagesPerRow
            }
        }
    }

    private val imagesGridPaddingDpPreference = intPreferencesKey("imagesGridPaddingDp")
    override val observeImagesGridPaddingDp: Flow<Int>
        get() = preferencesDataStore.data
            .map {
                it[imagesGridPaddingDpPreference] ?: DEFAULT_IMAGES_GRID_PADDING_DP
            }

    override fun setImagesGridPaddingDp(padding: Int) {
        coroutineScope.launch {
            preferencesDataStore.edit {
                it[imagesGridPaddingDpPreference] = padding
            }
        }
    }

    private val collaborationEntityTypePreference = stringPreferencesKey("collaborationEntityType")
    override val observeCollaborationEntityType: Flow<MusicBrainzEntity>
        get() = preferencesDataStore.data
            .map {
                it[collaborationEntityTypePreference]?.toMusicBrainzEntity() ?: MusicBrainzEntity.RECORDING
            }
            .distinctUntilChanged()

    override fun setCollaborationEntityType(entity: MusicBrainzEntity) {
        coroutineScope.launch {
            preferencesDataStore.edit {
                it[collaborationEntityTypePreference] = entity.resourceUri
            }
        }
    }
}
