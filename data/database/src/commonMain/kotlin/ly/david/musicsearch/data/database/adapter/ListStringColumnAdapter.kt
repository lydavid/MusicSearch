package ly.david.musicsearch.data.database.adapter

import app.cash.sqldelight.ColumnAdapter

internal object ListStringColumnAdapter : ColumnAdapter<List<String>, String> {
    override fun decode(databaseValue: String): List<String> =
        if (databaseValue.isEmpty()) {
            listOf()
        } else {
            databaseValue.split(LIST_DELIMITER)
        }

    override fun encode(value: List<String>): String = value.joinToString(LIST_DELIMITER)
}
