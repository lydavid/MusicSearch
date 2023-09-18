package ly.david.musicsearch.data.database.adapter

import app.cash.sqldelight.ColumnAdapter
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

private const val DELIMITER = ","

internal object ImmutableListStringColumnAdapter : ColumnAdapter<ImmutableList<String>, String> {
    override fun decode(databaseValue: String): ImmutableList<String> =
        if (databaseValue.isEmpty()) {
            persistentListOf()
        } else {
            databaseValue.split(DELIMITER).toImmutableList()
        }

    override fun encode(value: ImmutableList<String>): String = value.joinToString(DELIMITER)
}
