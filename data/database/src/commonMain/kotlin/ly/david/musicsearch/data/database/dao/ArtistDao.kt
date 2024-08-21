package ly.david.musicsearch.data.database.dao

import ly.david.musicsearch.data.database.Database
import ly.david.musicsearch.data.musicbrainz.models.core.ArtistMusicBrainzModel
import ly.david.musicsearch.shared.domain.LifeSpanUiModel
import ly.david.musicsearch.shared.domain.artist.ArtistDetailsModel
import lydavidmusicsearchdatadatabase.Artist

class ArtistDao(
    database: Database,
) : EntityDao {
    override val transacter = database.artistQueries

    fun insert(artist: ArtistMusicBrainzModel) {
        artist.run {
            transacter.insert(
                Artist(
                    id = id,
                    name = name,
                    sort_name = sortName,
                    disambiguation = disambiguation,
                    type = type,
                    type_id = typeId,
                    gender = gender,
                    country_code = countryCode,
                    begin = lifeSpan?.begin,
                    end = lifeSpan?.end,
                    ended = lifeSpan?.ended,
                ),
            )
        }
    }

    fun insertAll(artists: List<ArtistMusicBrainzModel>) {
        transacter.transaction {
            artists.forEach { artist ->
                insert(artist)
            }
        }
    }

    fun getArtistForDetails(artistId: String): ArtistDetailsModel? {
        return transacter.getArtistForDetails(
            artistId,
            mapper = ::toDetailsModel,
        ).executeAsOneOrNull()
    }

    fun delete(artistId: String) {
        transacter.delete(artistId)
    }

    private fun toDetailsModel(
        id: String,
        name: String,
        sortName: String,
        disambiguation: String?,
        type: String?,
        gender: String?,
        countryCode: String?,
        begin: String?,
        end: String?,
        ended: Boolean?,
    ) = ArtistDetailsModel(
        id = id,
        name = name,
        sortName = sortName,
        disambiguation = disambiguation,
        type = type,
        gender = gender,
        countryCode = countryCode,
        lifeSpan = LifeSpanUiModel(
            begin = begin,
            end = end,
            ended = ended,
        ),
    )
}
