package ly.david.mbjc

import ly.david.data.room.MusicSearchRoomDatabase
import org.junit.rules.TestWatcher
import org.junit.runner.Description

class MusicSearchRoomDatabaseTestRule(
    private val database: MusicSearchRoomDatabase,
) : TestWatcher() {
    override fun starting(description: Description) {
        super.starting(description)
    }

    override fun finished(description: Description) {
        super.finished(description)
        database.clearAllTables()
    }
}
