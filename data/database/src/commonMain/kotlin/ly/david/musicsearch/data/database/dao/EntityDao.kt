package ly.david.musicsearch.data.database.dao

import app.cash.sqldelight.TransacterImpl

interface EntityDao {
    val transacter: TransacterImpl

    fun withTransaction(block: () -> Unit) {
        transacter.transaction { block() }
    }
}
