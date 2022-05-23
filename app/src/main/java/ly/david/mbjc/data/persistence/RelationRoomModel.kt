package ly.david.mbjc.data.persistence

// TODO: we could put recording id here, but then each of these relationships will be 1-to-many from each recording/work/etc
//  this could work if we don't plan to store rels for anything else
//  but even then, that would mean the same relations rows will be repeated for every recording using the same ones
//  so we should NOT store recording_id here

// TODO: one advantage of using a model like this separate from querying all artists/places/labels/etc related to a recording
//  is that it returns the exact info needed. Well, technically we could query artists and map to a ui object with only the info we want.
//  The biggest advantage is that some artists are creditd under a different name for different recordings, this will allow us to store/get that.

// TODO: if we're using an auto id for relation, how will we know to not insert a relation that already exists?
//@Entity(tableName = "relations")
//internal data class RelationRoomModel(
////    @PrimaryKey(autoGenerate = true)
////    @ColumnInfo(name = "id")
////    val id: Long = 0,
//
//    @ColumnInfo(name = "description")
//    val description: String,
//
//    /**
//     * A soon-to-be superset of [MusicBrainzResource].
//     * Use [toDestination] to get corresponding destination for a MB resource.
//     */
//    @ColumnInfo(name = "destination")
//    val destination: Destination,
//
//    /**
//     * This assumes Music Brainz id are unique between resources.
//     * We know for certain they are unique within a resource (eg. artist), otherwise lookups wouldn't work.
//     */
//    @PrimaryKey
//    @ColumnInfo(name = "resource_id")
//    val resourceId: String,
//)
// TODO: could this be in the same model as recordings_relations?
//  doing it this way reduces the amount of duplicates we have.
//  however the description for a relation may not be the same all the time. They are release-dependent...
//  Idea: a relation linking table with 2 music brainz id, then lookup the resource in their respective tables
//  so artists for any artist relationship
//  if they are not in table, then we will query API for them on click
//  when we first init recordings screen, should add new artists to artists table with what data we have
//  on click, we can query for rest
//  For this method, we still need linking table to hold a description (name+disam/attributes).
//  In that case, just make relations primary key [description, resource_id] -> how do we know which to use?
