package ly.david.musicsearch.data.database

import app.cash.sqldelight.adapter.primitive.IntColumnAdapter
import app.cash.sqldelight.db.SqlDriver
import ly.david.musicsearch.data.database.adapter.MusicBrainzEntityStringColumnAdapter
import lydavidmusicsearchdatadatabase.Browse_entity_count
import lydavidmusicsearchdatadatabase.Collection
import lydavidmusicsearchdatadatabase.Mb_relation

fun createDatabase(driver: SqlDriver): Database {
    return Database(
        driver = driver,
        mb_relationAdapter = Mb_relation.Adapter(
            linked_entityAdapter = MusicBrainzEntityStringColumnAdapter,
            orderAdapter = IntColumnAdapter
        ),
        browse_entity_countAdapter = Browse_entity_count.Adapter(
            browse_entityAdapter = MusicBrainzEntityStringColumnAdapter,
            local_countAdapter = IntColumnAdapter,
            remote_countAdapter = IntColumnAdapter,
        ),
        collectionAdapter = Collection.Adapter(
            entity_countAdapter = IntColumnAdapter,
            entityAdapter = MusicBrainzEntityStringColumnAdapter,
        )
    )
}
