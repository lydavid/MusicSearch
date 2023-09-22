package ly.david.musicsearch.data.database

import app.cash.sqldelight.adapter.primitive.IntColumnAdapter
import app.cash.sqldelight.db.SqlDriver
import ly.david.musicsearch.data.database.adapter.ListStringColumnAdapter
import ly.david.musicsearch.data.database.adapter.MusicBrainzEntityStringColumnAdapter
import lydavidmusicsearchdatadatabase.Artist_credit_name
import lydavidmusicsearchdatadatabase.Browse_entity_count
import lydavidmusicsearchdatadatabase.Collection
import lydavidmusicsearchdatadatabase.Label
import lydavidmusicsearchdatadatabase.Mb_relation
import lydavidmusicsearchdatadatabase.Medium
import lydavidmusicsearchdatadatabase.Recording
import lydavidmusicsearchdatadatabase.Release
import lydavidmusicsearchdatadatabase.Release_group
import lydavidmusicsearchdatadatabase.Track
import lydavidmusicsearchdatadatabase.Work

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
        ),
        labelAdapter = Label.Adapter(
            label_codeAdapter = IntColumnAdapter,
        ),
        recordingAdapter = Recording.Adapter(
            lengthAdapter = IntColumnAdapter,
            isrcsAdapter = ListStringColumnAdapter,
        ),
        artist_credit_nameAdapter = Artist_credit_name.Adapter(
            positionAdapter = IntColumnAdapter,
        ),
        release_groupAdapter = Release_group.Adapter(
            secondary_typesAdapter = ListStringColumnAdapter,
            secondary_type_idsAdapter = ListStringColumnAdapter,
        ),
        workAdapter = Work.Adapter(
            iswcsAdapter = ListStringColumnAdapter,
        ),
        releaseAdapter = Release.Adapter(
            cover_art_countAdapter = IntColumnAdapter,
        ),
        mediumAdapter = Medium.Adapter(
            positionAdapter = IntColumnAdapter,
            track_countAdapter = IntColumnAdapter,
        ),
        trackAdapter = Track.Adapter(
            positionAdapter = IntColumnAdapter,
            lengthAdapter = IntColumnAdapter,
        )
    )
}
