package ly.david.musicsearch.data.database.adapter

import app.cash.sqldelight.ColumnAdapter
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList

internal object ImmutableListStringColumnAdapter : ColumnAdapter<ImmutableList<String>, String> {
    override fun decode(databaseValue: String): ImmutableList<String> =
        if (databaseValue.isEmpty()) {
            persistentListOf()
        } else {
            databaseValue.split(LIST_DELIMITER).toPersistentList()
        }

    override fun encode(value: ImmutableList<String>): String = value.joinToString(LIST_DELIMITER)
}
