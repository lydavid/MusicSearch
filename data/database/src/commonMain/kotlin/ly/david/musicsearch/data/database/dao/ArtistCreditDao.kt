package ly.david.musicsearch.data.database.dao

import ly.david.data.core.getDisplayNames
import ly.david.data.core.logging.Logger
import ly.david.data.musicbrainz.ArtistCreditMusicBrainzModel
import ly.david.musicsearch.data.database.Database
import ly.david.musicsearch.data.database.INSERTION_FAILED_DUE_TO_CONFLICT
import lydavidmusicsearchdatadatabase.Artist_credit_entity
import lydavidmusicsearchdatadatabase.Artist_credit_name

class ArtistCreditDao(
    database: Database,
    private val logger: Logger,
) {
    private val artistCreditQueries = database.artist_creditQueries
    private val artistCreditNameQueries = database.artist_credit_nameQueries
    private val artistCreditEntityQueries = database.artist_credit_entityQueries

    fun insertArtistCredits(
        entityId: String,
        artistCredits: List<ArtistCreditMusicBrainzModel>?,
    ) {
        artistCreditQueries.transaction {
            val artistCreditName = artistCredits.getDisplayNames()
            var artistCreditId = insertArtistCredit(artistCreditName)
            logger.e(Exception("artistCreditId=$artistCreditId"))
            if (artistCreditId == INSERTION_FAILED_DUE_TO_CONFLICT) {
                artistCreditId = artistCreditQueries.getArtistCreditByName(artistCreditName).executeAsOne().id
            } else {
                insertAllArtistCreditNames(artistCredits.toArtistCreditNames(artistCreditId))
            }

            linkArtistCreditWithEntity(
                artistCreditId = artistCreditId,
                entityId = entityId,
            )
        }
    }

    fun getArtistCreditNamesForEntity(
        entityId: String,
    ): List<Artist_credit_name> =
        artistCreditNameQueries.getArtistCreditNamesForEntity(entityId).executeAsList()

    private fun insertArtistCredit(name: String): Long {
        return try {
            artistCreditQueries.insert(
                id = 0,
                name = name,
            )
            artistCreditQueries.lastInsertRowId().executeAsOne()
        } catch (ex: Exception) {
            INSERTION_FAILED_DUE_TO_CONFLICT
        }
    }

    private fun insertAllArtistCreditNames(artistCreditNames: List<Artist_credit_name>) {
        artistCreditNames.forEach { artistCreditName ->
            artistCreditNameQueries.insert(artistCreditName)
        }
    }

    private fun linkArtistCreditWithEntity(
        artistCreditId: Long,
        entityId: String,
    ) {
        artistCreditEntityQueries.insert(
            Artist_credit_entity(
                artist_credit_id = artistCreditId,
                entity_id = entityId,
            )
        )
    }
}

/**
 * Converts artist credits to an appropriate model to store.
 *
 * We take in [artistCreditId] so that we don't duplicate [ArtistCreditNameRoomModel].
 *
 * The receiver must be a list because we need its index.
 */
internal fun List<ArtistCreditMusicBrainzModel>?.toArtistCreditNames(
    artistCreditId: Long,
): List<Artist_credit_name> =
    this?.mapIndexed { index, artistCredit ->
        Artist_credit_name(
            artist_credit_id = artistCreditId,
            artist_id = artistCredit.artist.id,
            name = artistCredit.name,
            join_phrase = artistCredit.joinPhrase,
            position = index,
        )
    }.orEmpty()
