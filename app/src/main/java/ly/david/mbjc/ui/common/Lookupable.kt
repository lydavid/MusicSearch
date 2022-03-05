package ly.david.mbjc.ui.common

//interface Lookupable<E: MusicBrainzLookupableEntity> {
//
//    val lookupHistoryDao: LookupHistoryDao
//
//    /**
//     * Lookup request for specific MusicBrainz entity.
//     */
//    suspend fun lookup(mbid: String): E
//
//    /**
//     * Add a record of looking up this entity to our database.
//     */
//    suspend fun incrementOrInsertLookupHistory(mbid: String, summary: String) {
//        lookupHistoryDao.incrementOrInsertLookupHistory(
//            LookupHistory(
//                summary = summary,
//                destination = Destination.LOOKUP_ARTIST, // TODO: how to tie a destination with lookupable?
//                mbid = mbid,
//                numberOfVisits = 1
//            )
//        )
//    }
//}
