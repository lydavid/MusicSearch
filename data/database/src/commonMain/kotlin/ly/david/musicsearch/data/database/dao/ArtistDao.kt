package ly.david.musicsearch.data.database.dao

import ly.david.data.musicbrainz.ArtistMusicBrainzModel
import ly.david.musicsearch.core.models.LifeSpanUiModel
import ly.david.musicsearch.core.models.artist.ArtistScaffoldModel
import ly.david.musicsearch.data.database.Database
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

    fun getArtistForDetails(artistId: String): ArtistScaffoldModel? {
        return transacter.getArtistForDetails(
            artistId,
            mapper = ::toArtistScaffoldModel,
        ).executeAsOneOrNull()
    }

    private fun toArtistScaffoldModel(
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
        largeUrl: String?,
    ) = ArtistScaffoldModel(
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
        imageUrl = largeUrl,
    )
}
