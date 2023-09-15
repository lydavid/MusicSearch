package ly.david.musicsearch.data.database

import app.cash.sqldelight.db.SqlDriver

fun createDatabase(driver: SqlDriver): Database {
    val database = Database(driver)

    return database
}
