package ly.david.musicsearch.shared.domain.listitem

import ly.david.musicsearch.shared.domain.history.NowPlayingHistory
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
class ToNowPlayingHistoryListItemModelTest {

    private val datetime = Instant.parse("2022-01-01T00:00:00Z")

    @Test
    fun simpleEN() {
        assertEquals(
            NowPlayingHistoryListItemModel(
                id = "Song Title by Artist Name",
                title = "Song Title",
                artist = "Artist Name",
                lastPlayed = datetime,
            ),
            NowPlayingHistory(
                raw = "Song Title by Artist Name",
                lastPlayed = datetime,
            ).toNowPlayingHistoryListItemModel(),
        )
    }

    @Test
    fun titleIncludesDelimiter() {
        assertEquals(
            NowPlayingHistoryListItemModel(
                id = "Day by Day by Nathan Drake",
                title = "Day by Day",
                artist = "Nathan Drake",
                lastPlayed = datetime,
            ),
            NowPlayingHistory(
                raw = "Day by Day by Nathan Drake",
                lastPlayed = datetime,
            ).toNowPlayingHistoryListItemModel(),
        )
    }

    @Test
    fun simpleDE() {
        assertEquals(
            NowPlayingHistoryListItemModel(
                id = "„Moth to a Flame“ von Swedish House Mafia",
                title = "Moth to a Flame",
                artist = "Swedish House Mafia",
                lastPlayed = datetime,
            ),
            NowPlayingHistory(
                raw = "„Moth to a Flame“ von Swedish House Mafia",
                lastPlayed = datetime,
            ).toNowPlayingHistoryListItemModel(),
        )
    }

    @Test
    fun simpleJA() {
        assertEquals(
            NowPlayingHistoryListItemModel(
                id = "TAIDADA（Zutomayo）",
                title = "TAIDADA",
                artist = "Zutomayo",
                lastPlayed = datetime,
            ),
            NowPlayingHistory(
                raw = "TAIDADA（Zutomayo）",
                lastPlayed = datetime,
            ).toNowPlayingHistoryListItemModel(),
        )
    }

    @Test
    fun simpleZH() {
        assertEquals(
            NowPlayingHistoryListItemModel(
                id = "Lou Bega的《Mambo No. 5 (a Little Bit of...)》",
                title = "Mambo No. 5 (a Little Bit of...)",
                artist = "Lou Bega",
                lastPlayed = datetime,
            ),
            NowPlayingHistory(
                raw = "Lou Bega的《Mambo No. 5 (a Little Bit of...)》",
                lastPlayed = datetime,
            ).toNowPlayingHistoryListItemModel(),
        )
    }
}
