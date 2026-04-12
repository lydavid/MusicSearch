package ly.david.musicsearch.core.preferences.internal

import androidx.datastore.core.DataMigration
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import ly.david.musicsearch.shared.domain.coroutine.CoroutineDispatchers
import ly.david.musicsearch.shared.domain.preferences.AppPreferencesKey
import java.io.File

internal fun createDataStore(
    dispatchers: CoroutineDispatchers,
    path: String,
): DataStore<Preferences> = PreferenceDataStoreFactory.create(
    corruptionHandler = ReplaceFileCorruptionHandler(
        produceNewData = { emptyPreferences() },
    ),
    migrations = listOf(
        standardizationMigrations,
    ),
    scope = CoroutineScope(SupervisorJob() + dispatchers.io),
    produceFile = { File(path) },
)

private data class KeyMigration<T>(
    val old: Preferences.Key<T>,
    val new: Preferences.Key<T>,
)

private val standardizationMigrations = object : DataMigration<Preferences> {

    val migrations = listOf(
        KeyMigration(
            stringPreferencesKey("theme"),
            stringPreferencesKey(AppPreferencesKey.THEME.name),
        ),
        KeyMigration(
            booleanPreferencesKey("useMaterialYou"),
            booleanPreferencesKey(AppPreferencesKey.USE_MATERIAL_YOU.name),
        ),
        KeyMigration(
            intPreferencesKey("seedColor"),
            intPreferencesKey(AppPreferencesKey.SEED_COLOR.name),
        ),
        KeyMigration(
            stringPreferencesKey("recordingSortOption"),
            stringPreferencesKey(AppPreferencesKey.RECORDING_SORT_OPTION.name),
        ),
        KeyMigration(
            stringPreferencesKey("releaseSortOption"),
            stringPreferencesKey(AppPreferencesKey.RELEASE_SORT_OPTION.name),
        ),
        KeyMigration(
            booleanPreferencesKey("showMoreInfoInReleaseListItem"),
            booleanPreferencesKey(AppPreferencesKey.SHOW_MORE_INFO_IN_RELEASE_LIST_ITEM.name),
        ),
        KeyMigration(
            stringPreferencesKey("releaseGroupSortOption"),
            stringPreferencesKey(AppPreferencesKey.RELEASE_GROUP_SORT_OPTION.name),
        ),
        KeyMigration(
            booleanPreferencesKey("showLocalCollections"),
            booleanPreferencesKey(AppPreferencesKey.SHOW_LOCAL_COLLECTIONS.name),
        ),
        KeyMigration(
            booleanPreferencesKey("showRemoteCollections"),
            booleanPreferencesKey(AppPreferencesKey.SHOW_REMOTE_COLLECTIONS.name),
        ),
        KeyMigration(
            stringPreferencesKey("historySortOption"),
            stringPreferencesKey(AppPreferencesKey.HISTORY_SORT_OPTION.name),
        ),
        KeyMigration(
            stringPreferencesKey("collectionSortOption"),
            stringPreferencesKey(AppPreferencesKey.COLLECTION_SORT_OPTION.name),
        ),
        KeyMigration(
            stringPreferencesKey("coverArtsSortOption"),
            stringPreferencesKey(AppPreferencesKey.COVER_ARTS_SORT_OPTION.name),
        ),
        KeyMigration(
            booleanPreferencesKey("isDeveloperMode"),
            booleanPreferencesKey(AppPreferencesKey.IS_DEVELOPER_MODE.name),
        ),
        KeyMigration(
            intPreferencesKey("numberOfImagesPerRow"),
            intPreferencesKey(AppPreferencesKey.NUMBER_OF_IMAGES_PER_ROW.name),
        ),
        KeyMigration(
            intPreferencesKey("imagesGridPaddingDp"),
            intPreferencesKey(AppPreferencesKey.IMAGES_GRID_PADDING_DP.name),
        ),
        KeyMigration(
            stringPreferencesKey("collaborationEntityType"),
            stringPreferencesKey(AppPreferencesKey.COLLABORATION_ENTITY_TYPE.name),
        ),

        KeyMigration(
            stringPreferencesKey("LISTENBRAINZ_USERNAME"),
            stringPreferencesKey(AppPreferencesKey.LISTENBRAINZ_BROWSE_USERNAME.name),
        ),

        KeyMigration(
            stringPreferencesKey("SPOTIFY_ACCESS_TOKEN_KEY"),
            stringPreferencesKey(AppPreferencesKey.SPOTIFY_ACCESS_TOKEN.name),
        ),
    )

    override suspend fun shouldMigrate(currentData: Preferences): Boolean =
        migrations.any { currentData[it.old] != null }

    override suspend fun migrate(currentData: Preferences): Preferences =
        currentData.toMutablePreferences().apply {
            migrations.forEach { migrate(currentData, it) }
        }

    override suspend fun cleanUp() {
        /* no-op */
    }

    private fun <T> MutablePreferences.migrate(
        currentData: Preferences,
        migration: KeyMigration<T>,
    ) {
        currentData[migration.old]?.let {
            this[migration.new] = it
            remove(migration.old)
        }
    }
}
