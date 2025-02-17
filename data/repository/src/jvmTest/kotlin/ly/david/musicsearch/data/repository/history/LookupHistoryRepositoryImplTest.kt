package ly.david.musicsearch.data.repository.history

import androidx.paging.testing.asSnapshot
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import ly.david.musicsearch.data.database.dao.LookupHistoryDao
import ly.david.data.test.KoinTestRule
import ly.david.musicsearch.data.repository.LookupHistoryRepositoryImpl
import ly.david.musicsearch.shared.domain.history.HistorySortOption
import ly.david.musicsearch.shared.domain.history.LookupHistory
import ly.david.musicsearch.shared.domain.listitem.ListItemModel
import ly.david.musicsearch.shared.domain.listitem.ListSeparator
import ly.david.musicsearch.shared.domain.listitem.LookupHistoryListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject

class LookupHistoryRepositoryImplTest : KoinTest {

    @get:Rule(order = 0)
    val koinTestRule = KoinTestRule()

    private val lookupHistoryDao: LookupHistoryDao by inject()

    @Test
    fun `empty, upsert, filter`() = runTest {
        val repository = LookupHistoryRepositoryImpl(
            lookupHistoryDao = lookupHistoryDao,
        )

        val emptyListItemModelList: List<ListItemModel> = repository.observeAllLookupHistory(
            "",
            sortOption = HistorySortOption.RECENTLY_VISITED,
        ).asSnapshot()
        assertEquals(
            listOf<ListItemModel>(),
            emptyListItemModelList,
        )

        val currentTime = Instant.parse("2025-02-09T13:37:02Z")

        repository.upsert(
            LookupHistory(
                mbid = "81d75493-78b6-4a37-b5ae-2a3918ee3756",
                title = "欠けた心象、世のよすが",
                entity = MusicBrainzEntity.RELEASE_GROUP,
                lastAccessed = currentTime,
            ),
        )
        val listItemModelList: List<ListItemModel> = repository.observeAllLookupHistory(
            "",
            sortOption = HistorySortOption.RECENTLY_VISITED,
        ).asSnapshot()
        assertEquals(
            listOf(
                ListSeparator(
                    id = "Sunday, February 9",
                    text = "Sunday, February 9",
                ),
                LookupHistoryListItemModel(
                    title = "欠けた心象、世のよすが",
                    entity = MusicBrainzEntity.RELEASE_GROUP,
                    id = "81d75493-78b6-4a37-b5ae-2a3918ee3756",
                    numberOfVisits = 1,
                    lastAccessed = currentTime,
                ),
            ),
            listItemModelList,
        )

        val filteredListItemModelList: List<ListItemModel> = repository.observeAllLookupHistory(
            "not found",
            sortOption = HistorySortOption.RECENTLY_VISITED,
        ).asSnapshot()
        assertEquals(
            listOf<ListItemModel>(),
            filteredListItemModelList,
        )
    }
}
