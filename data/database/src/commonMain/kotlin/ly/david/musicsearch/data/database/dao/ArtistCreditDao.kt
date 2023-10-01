package ly.david.musicsearch.data.database.dao

import ly.david.data.core.getDisplayNames
import ly.david.data.musicbrainz.ArtistCreditMusicBrainzModel
import ly.david.musicsearch.data.database.Database
import ly.david.musicsearch.data.database.INSERTION_FAILED_DUE_TO_CONFLICT
import lydavidmusicsearchdatadatabase.Artist_credit_entity
import lydavidmusicsearchdatadatabase.Artist_credit_name

class ArtistCreditDao(
    database: Database,
) : EntityDao {
    override val transacter = database.artist_creditQueries
    private val artistCreditNameQueries = database.artist_credit_nameQueries
    private val artistCreditEntityQueries = database.artist_credit_entityQueries

    internal fun insertArtistCredits(
        entityId: String,
        artistCredits: List<ArtistCreditMusicBrainzModel>?,
    ) {
        if (artistCredits.isNullOrEmpty()) return

        withTransaction {
            val artistCreditName = artistCredits.getDisplayNames()
            var artistCreditId = insertArtistCredit(artistCreditName)
            if (artistCreditId == INSERTION_FAILED_DUE_TO_CONFLICT) {
                artistCreditId = transacter.getArtistCreditByName(artistCreditName).executeAsOne().id
            } else {
                insertAllArtistCreditNames(artistCredits.toArtistCreditNames(artistCreditId))
            }

            linkArtistCreditWithEntity(
                artistCreditId = artistCreditId,
                entityId = entityId,
            )
        }
    }

    private fun insertArtistCredit(name: String): Long {
        return try {
            transacter.insertOrFail(
                id = 0,
                name = name,
            )
            transacter.lastInsertRowId().executeAsOne()
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

    fun getArtistCreditNamesForEntity(
        entityId: String,
    ): List<Artist_credit_name> =
        artistCreditNameQueries.getArtistCreditNamesForEntity(entityId).executeAsList()
}

/**
 * Converts artist credits to an appropriate model to store.
 *
 * We take in [artistCreditId] so that we don't duplicate [ArtistCreditNameRoomModel].
 *
 * The receiver must be a list because we need its index.
 */
private fun List<ArtistCreditMusicBrainzModel>?.toArtistCreditNames(
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
