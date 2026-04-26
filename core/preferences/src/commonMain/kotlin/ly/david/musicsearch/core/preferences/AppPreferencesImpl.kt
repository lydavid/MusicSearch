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
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.domain.network.resourceUri
import ly.david.musicsearch.shared.domain.network.toMusicBrainzEntityType
import ly.david.musicsearch.shared.domain.preferences.AppPreferences
import ly.david.musicsearch.shared.domain.preferences.AppPreferencesKey
import ly.david.musicsearch.shared.domain.release.ReleaseStatus
import ly.david.musicsearch.shared.domain.sort.AreaSortOption
import ly.david.musicsearch.shared.domain.sort.ArtistSortOption
import ly.david.musicsearch.shared.domain.sort.EventSortOption
import ly.david.musicsearch.shared.domain.sort.LabelSortOption
import ly.david.musicsearch.shared.domain.sort.RecordingSortOption
import ly.david.musicsearch.shared.domain.sort.ReleaseGroupSortOption
import ly.david.musicsearch.shared.domain.sort.ReleaseSortOption
import ly.david.musicsearch.shared.domain.sort.WorkSortOption

internal class AppPreferencesImpl(
    private val preferencesDataStore: DataStore<Preferences>,
    private val coroutineScope: CoroutineScope,
    private val crashReporterSettings: CrashReporterSettings,
) : AppPreferences {

    private val themePreference = stringPreferencesKey(AppPreferencesKey.THEME.name)
    override val theme: Flow<AppPreferences.Theme>
        get() = preferencesDataStore.data
            .map {
                AppPreferences.Theme.valueOf(it[themePreference] ?: AppPreferences.Theme.SYSTEM.name)
            }
            .distinctUntilChanged()

    override fun setTheme(theme: AppPreferences.Theme) {
        coroutineScope.launch {
            preferencesDataStore.edit {
                it[themePreference] = theme.name
            }
        }
    }

    private val useMaterialYouPreference = booleanPreferencesKey(AppPreferencesKey.USE_MATERIAL_YOU.name)
    override val useMaterialYou: Flow<Boolean>
        get() = preferencesDataStore.data
            .map {
                it[useMaterialYouPreference] ?: false
            }
            .distinctUntilChanged()

    override fun setUseMaterialYou(use: Boolean) {
        coroutineScope.launch {
            preferencesDataStore.edit {
                it[useMaterialYouPreference] = use
            }
        }
    }

    private val seedColorPreference = intPreferencesKey(AppPreferencesKey.SEED_COLOR.name)
    override val observeSeedColor: Flow<Int>
        get() = preferencesDataStore.data
            .map {
                it[seedColorPreference] ?: DEFAULT_SEED_COLOR_INT
            }
            .distinctUntilChanged()

    override fun setSeedColor(seedColor: Int) {
        coroutineScope.launch {
            preferencesDataStore.edit {
                it[seedColorPreference] = seedColor
            }
        }
    }

    // region Area
    private val areaSortOptionPreference = stringPreferencesKey(AppPreferencesKey.AREA_SORT_OPTION.name)

    override val areaSortOption: Flow<AreaSortOption>
        get() = preferencesDataStore.data
            .map {
                it[areaSortOptionPreference].toEnumOrDefault(AreaSortOption.InsertedAscending)
            }
            .distinctUntilChanged()

    override fun setAreaSortOption(sort: AreaSortOption) {
        coroutineScope.launch {
            preferencesDataStore.edit {
                it[areaSortOptionPreference] = sort.name
            }
        }
    }
    // endregion

    // region Artist
    private val artistSortOptionPreference = stringPreferencesKey(AppPreferencesKey.ARTIST_SORT_OPTION.name)

    override val artistSortOption: Flow<ArtistSortOption>
        get() = preferencesDataStore.data
            .map {
                it[artistSortOptionPreference].toEnumOrDefault(ArtistSortOption.InsertedAscending)
            }
            .distinctUntilChanged()

    override fun setArtistSortOption(sort: ArtistSortOption) {
        coroutineScope.launch {
            preferencesDataStore.edit {
                it[artistSortOptionPreference] = sort.name
            }
        }
    }
    // endregion

    // region Event
    private val eventSortOptionPreference = stringPreferencesKey(AppPreferencesKey.EVENT_SORT_OPTION.name)

    override val eventSortOption: Flow<EventSortOption>
        get() = preferencesDataStore.data
            .map {
                it[eventSortOptionPreference].toEnumOrDefault(EventSortOption.InsertedAscending)
            }
            .distinctUntilChanged()

    override fun setEventSortOption(sort: EventSortOption) {
        coroutineScope.launch {
            preferencesDataStore.edit {
                it[eventSortOptionPreference] = sort.name
            }
        }
    }
    // endregion

    // region Label
    private val labelSortOptionPreference = stringPreferencesKey(AppPreferencesKey.LABEL_SORT_OPTION.name)

    override val labelSortOption: Flow<LabelSortOption>
        get() = preferencesDataStore.data
            .map {
                it[labelSortOptionPreference].toEnumOrDefault(LabelSortOption.InsertedAscending)
            }
            .distinctUntilChanged()

    override fun setLabelSortOption(sort: LabelSortOption) {
        coroutineScope.launch {
            preferencesDataStore.edit {
                it[labelSortOptionPreference] = sort.name
            }
        }
    }
    // endregion

    // region Recording
    private val recordingSortOptionPreference = stringPreferencesKey(AppPreferencesKey.RECORDING_SORT_OPTION.name)

    override val recordingSortOption: Flow<RecordingSortOption>
        get() = preferencesDataStore.data
            .map {
                it[recordingSortOptionPreference].toEnumOrDefault(RecordingSortOption.InsertedAscending)
            }
            .distinctUntilChanged()

    override fun setRecordingSortOption(sort: RecordingSortOption) {
        coroutineScope.launch {
            preferencesDataStore.edit {
                it[recordingSortOptionPreference] = sort.name
            }
        }
    }
    // endregion

    // region Releases
    private val releaseSortOptionPreference = stringPreferencesKey(AppPreferencesKey.RELEASE_SORT_OPTION.name)
    override val releaseSortOption: Flow<ReleaseSortOption>
        get() = preferencesDataStore.data
            .map {
                ReleaseSortOption.valueOf(
                    it[releaseSortOptionPreference] ?: ReleaseSortOption.InsertedAscending.name,
                )
            }
            .distinctUntilChanged()

    override fun setReleaseSortOption(sort: ReleaseSortOption) {
        coroutineScope.launch {
            preferencesDataStore.edit {
                it[releaseSortOptionPreference] = sort.name
            }
        }
    }

    private val showMoreInfoInReleaseListItemPreference =
        booleanPreferencesKey(AppPreferencesKey.SHOW_MORE_INFO_IN_RELEASE_LIST_ITEM.name)
    override val showMoreInfoInReleaseListItem: Flow<Boolean>
        get() = preferencesDataStore.data
            .map {
                it[showMoreInfoInReleaseListItemPreference] ?: true
            }
            .distinctUntilChanged()

    override fun setShowMoreInfoInReleaseListItem(show: Boolean) {
        coroutineScope.launch {
            preferencesDataStore.edit {
                it[showMoreInfoInReleaseListItemPreference] = show
            }
        }
    }

    private val showReleaseStatusesPreference =
        intPreferencesKey(AppPreferencesKey.SHOW_RELEASE_STATUSES.name)

    private fun Set<ReleaseStatus>.toBitmask(): Int =
        fold(0) { accumulator, status -> accumulator or status.bit }

    private fun Int.toReleaseStatusSet(): Set<ReleaseStatus> =
        ReleaseStatus.entries.filterTo(mutableSetOf()) { status -> (this and status.bit) != 0 }

    private fun Preferences.getShownReleaseStatuses(): Set<ReleaseStatus> =
        this[showReleaseStatusesPreference]?.toReleaseStatusSet() ?: ReleaseStatus.entries.toSet()

    override val showReleaseStatuses: Flow<Set<ReleaseStatus>>
        get() = preferencesDataStore.data
            .map { it.getShownReleaseStatuses() }
            .distinctUntilChanged()

    override fun setShowReleaseStatus(status: ReleaseStatus?) {
        coroutineScope.launch {
            preferencesDataStore.edit {
                val newStatuses = if (status == null) {
                    val current = it.getShownReleaseStatuses()
                    if (current.size == ReleaseStatus.entries.size) emptySet() else ReleaseStatus.entries.toSet()
                } else {
                    val current = it.getShownReleaseStatuses()
                    if (status in current) current - status else current + status
                }
                it[showReleaseStatusesPreference] = newStatuses.toBitmask()
            }
        }
    }
    // endregion

    // region Release Groups
    private val releaseGroupSortOptionPreference =
        stringPreferencesKey(AppPreferencesKey.RELEASE_GROUP_SORT_OPTION.name)
    override val releaseGroupSortOption: Flow<ReleaseGroupSortOption>
        get() = preferencesDataStore.data
            .map {
                it[releaseGroupSortOptionPreference].toEnumOrDefault(ReleaseGroupSortOption.InsertedAscending)
            }
            .distinctUntilChanged()

    override fun setReleaseGroupSortOption(sortOption: ReleaseGroupSortOption) {
        coroutineScope.launch {
            preferencesDataStore.edit {
                it[releaseGroupSortOptionPreference] = sortOption.name
            }
        }
    }
    // endregion

    // region Works
    private val workSortOptionPreference = stringPreferencesKey(AppPreferencesKey.WORK_SORT_OPTION.name)

    override val workSortOption: Flow<WorkSortOption>
        get() = preferencesDataStore.data
            .map {
                it[workSortOptionPreference].toEnumOrDefault(WorkSortOption.InsertedAscending)
            }
            .distinctUntilChanged()

    override fun setWorkSortOption(sort: WorkSortOption) {
        coroutineScope.launch {
            preferencesDataStore.edit {
                it[workSortOptionPreference] = sort.name
            }
        }
    }
    // endregion

    private val showLocalCollectionsPreference = booleanPreferencesKey(AppPreferencesKey.SHOW_LOCAL_COLLECTIONS.name)
    override val showLocalCollections: Flow<Boolean>
        get() = preferencesDataStore.data
            .map {
                it[showLocalCollectionsPreference] ?: true
            }
            .distinctUntilChanged()

    override fun setShowLocalCollections(show: Boolean) {
        coroutineScope.launch {
            preferencesDataStore.edit {
                it[showLocalCollectionsPreference] = show
            }
        }
    }

    private val showRemoteCollectionsPreference = booleanPreferencesKey(AppPreferencesKey.SHOW_REMOTE_COLLECTIONS.name)
    override val showRemoteCollections: Flow<Boolean>
        get() = preferencesDataStore.data
            .map {
                it[showRemoteCollectionsPreference] ?: true
            }
            .distinctUntilChanged()

    override fun setShowRemoteCollections(show: Boolean) {
        coroutineScope.launch {
            preferencesDataStore.edit {
                it[showRemoteCollectionsPreference] = show
            }
        }
    }

    // region History
    private val historySortOptionPreference = stringPreferencesKey(AppPreferencesKey.HISTORY_SORT_OPTION.name)
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
    private val collectionSortOptionPreference = stringPreferencesKey(AppPreferencesKey.COLLECTION_SORT_OPTION.name)
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
    private val coverArtsSortOptionPreference = stringPreferencesKey(AppPreferencesKey.COVER_ARTS_SORT_OPTION.name)
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

    private val isDeveloperModePreference = booleanPreferencesKey(AppPreferencesKey.IS_DEVELOPER_MODE.name)
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

    private val numberOfImagesPerRowPreference = intPreferencesKey(AppPreferencesKey.NUMBER_OF_IMAGES_PER_ROW.name)
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

    private val imagesGridPaddingDpPreference = intPreferencesKey(AppPreferencesKey.IMAGES_GRID_PADDING_DP.name)
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

    private val collaborationEntityTypePreference =
        stringPreferencesKey(AppPreferencesKey.COLLABORATION_ENTITY_TYPE.name)
    override val observeCollaborationEntityType: Flow<MusicBrainzEntityType>
        get() = preferencesDataStore.data
            .map {
                it[collaborationEntityTypePreference]?.toMusicBrainzEntityType() ?: MusicBrainzEntityType.RECORDING
            }
            .distinctUntilChanged()

    override fun setCollaborationEntityType(entity: MusicBrainzEntityType) {
        coroutineScope.launch {
            preferencesDataStore.edit {
                it[collaborationEntityTypePreference] = entity.resourceUri
            }
        }
    }

    private val scrollToHideTopAppBarPreference =
        booleanPreferencesKey(AppPreferencesKey.SCROLL_TO_HIDE_TOP_APP_BAR.name)
    override val scrollToHideTopAppBar: Flow<Boolean>
        get() {
            return preferencesDataStore.data
                .map {
                    it[scrollToHideTopAppBarPreference] == true
                }
                .distinctUntilChanged()
        }

    override fun setScrollToHideTopAppBar(enable: Boolean) {
        coroutineScope.launch {
            preferencesDataStore.edit {
                it[scrollToHideTopAppBarPreference] = enable
            }
        }
    }
}
