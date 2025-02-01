package ly.david.musicsearch.data.database

import app.cash.sqldelight.adapter.primitive.IntColumnAdapter
import app.cash.sqldelight.db.SqlDriver
import ly.david.musicsearch.data.database.adapter.ImmutableListStringColumnAdapter
import ly.david.musicsearch.data.database.adapter.InstantLongColumnAdapter
import ly.david.musicsearch.data.database.adapter.ListStringColumnAdapter
import ly.david.musicsearch.data.database.adapter.MusicBrainzEntityStringColumnAdapter
import lydavidmusicsearchdatadatabase.Artist
import lydavidmusicsearchdatadatabase.Artist_credit_name
import lydavidmusicsearchdatadatabase.Browse_entity_count
import lydavidmusicsearchdatadatabase.Collection
import lydavidmusicsearchdatadatabase.Label
import lydavidmusicsearchdatadatabase.Lookup_history
import lydavidmusicsearchdatadatabase.Mbid_image
import lydavidmusicsearchdatadatabase.Medium
import lydavidmusicsearchdatadatabase.Now_playing_history
import lydavidmusicsearchdatadatabase.Recording
import lydavidmusicsearchdatadatabase.Relation
import lydavidmusicsearchdatadatabase.Release
import lydavidmusicsearchdatadatabase.Release_group
import lydavidmusicsearchdatadatabase.Search_history
import lydavidmusicsearchdatadatabase.Search_result_metadata
import lydavidmusicsearchdatadatabase.Spotify_track
import lydavidmusicsearchdatadatabase.Spotify_track_listen
import lydavidmusicsearchdatadatabase.Track
import lydavidmusicsearchdatadatabase.Work

fun createDatabase(driver: SqlDriver): Database {
    return Database(
        driver = driver,
        relationAdapter = Relation.Adapter(
            linked_entityAdapter = MusicBrainzEntityStringColumnAdapter,
            orderAdapter = IntColumnAdapter,
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
            ipisAdapter = ListStringColumnAdapter,
            isnisAdapter = ListStringColumnAdapter,
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
        ),
        lookup_historyAdapter = Lookup_history.Adapter(
            entityAdapter = MusicBrainzEntityStringColumnAdapter,
            number_of_visitsAdapter = IntColumnAdapter,
            last_accessedAdapter = InstantLongColumnAdapter,
        ),
        now_playing_historyAdapter = Now_playing_history.Adapter(
            last_playedAdapter = InstantLongColumnAdapter,
        ),
        search_historyAdapter = Search_history.Adapter(
            entityAdapter = MusicBrainzEntityStringColumnAdapter,
            last_accessedAdapter = InstantLongColumnAdapter,
        ),
        spotify_trackAdapter = Spotify_track.Adapter(
            track_lengthAdapter = IntColumnAdapter,
        ),
        spotify_track_listenAdapter = Spotify_track_listen.Adapter(
            listenedAdapter = InstantLongColumnAdapter,
        ),
        artistAdapter = Artist.Adapter(
            ipisAdapter = ListStringColumnAdapter,
            isnisAdapter = ListStringColumnAdapter,
        ),
        search_result_metadataAdapter = Search_result_metadata.Adapter(
            entityAdapter = MusicBrainzEntityStringColumnAdapter,
            local_countAdapter = IntColumnAdapter,
            remote_countAdapter = IntColumnAdapter,
        ),
        mbid_imageAdapter = Mbid_image.Adapter(
            typesAdapter = ImmutableListStringColumnAdapter,
        ),
    )
}
