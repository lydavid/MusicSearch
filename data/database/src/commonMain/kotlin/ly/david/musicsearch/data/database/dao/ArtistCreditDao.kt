package ly.david.musicsearch.data.database.dao

import ly.david.musicsearch.shared.domain.artist.ArtistCreditUiModel
import ly.david.musicsearch.shared.domain.artist.getDisplayNames
import ly.david.musicsearch.data.database.Database
import ly.david.musicsearch.data.database.INSERTION_FAILED_DUE_TO_CONFLICT
import ly.david.musicsearch.data.musicbrainz.models.common.ArtistCreditMusicBrainzModel
import lydavidmusicsearchdatadatabase.Artist_credit_entity
import lydavidmusicsearchdatadatabase.Artist_credit_name

interface ArtistCreditDao : EntityDao {
    fun insertArtistCredits(
        entityId: String,
        artistCredits: List<ArtistCreditMusicBrainzModel>?,
    )

    fun getArtistCreditsForEntity(
        entityId: String,
    ): List<ArtistCreditUiModel>
}

class ArtistCreditDaoImpl(
    database: Database,
) : ArtistCreditDao {
    override val transacter = database.artist_creditQueries
    private val artistCreditNameQueries = database.artist_credit_nameQueries
    private val artistCreditEntityQueries = database.artist_credit_entityQueries

    override fun insertArtistCredits(
        entityId: String,
        artistCredits: List<ArtistCreditMusicBrainzModel>?,
    ) {
        if (artistCredits.isNullOrEmpty()) return

        withTransaction {
            val artistCreditName = artistCredits.getDisplayNames()
            insertArtistCredit(artistCreditName)
            val artistCreditId = transacter.getArtistCreditByName(artistCreditName).executeAsOne().id
            insertAllArtistCreditNames(artistCredits.toArtistCreditNames(artistCreditId))

            linkArtistCreditWithEntity(
                artistCreditId = artistCreditId,
                entityId = entityId,
            )
        }
    }

    private fun insertArtistCredit(name: String) {
        transacter.insertOrIgnore(
            id = 0,
            name = name,
        )
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
            ),
        )
    }

    override fun getArtistCreditsForEntity(
        entityId: String,
    ): List<ArtistCreditUiModel> =
        artistCreditNameQueries.getArtistCreditNamesForEntity(
            entityId,
            mapper = { artistId: String, name: String, joinPhrase: String? ->
                ArtistCreditUiModel(
                    artistId = artistId,
                    name = name,
                    joinPhrase = joinPhrase,
                )
            },
        ).executeAsList()
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
