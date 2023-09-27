package ly.david.mbjc

import ly.david.musicsearch.data.database.Database
import org.junit.rules.TestWatcher
import org.junit.runner.Description

class MusicSearchRoomDatabaseTestRule(
    private val database: Database,
) : TestWatcher() {
    override fun starting(description: Description) {
        super.starting(description)
    }

    override fun finished(description: Description) {
        super.finished(description)
//        database.clearAllTables()
    }
}
