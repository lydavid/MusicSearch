package ly.david.musicsearch.data.database.dao

import ly.david.musicsearch.data.database.Database
import ly.david.musicsearch.data.musicbrainz.models.core.ArtistMusicBrainzModel
import ly.david.musicsearch.shared.domain.LifeSpanUiModel
import ly.david.musicsearch.shared.domain.artist.ArtistDetailsModel
import ly.david.musicsearch.shared.domain.listitem.AreaListItemModel
import lydavidmusicsearchdatadatabase.Artist

class ArtistDao(
    database: Database,
) : EntityDao {
    override val transacter = database.artistQueries

    fun insertReplace(artist: ArtistMusicBrainzModel) {
        artist.run {
            transacter.insertReplace(
                Artist(
                    id = id,
                    name = name,
                    sort_name = sortName,
                    disambiguation = disambiguation,
                    type = type,
                    type_id = typeId,
                    gender = gender,
                    ipis = ipis,
                    isnis = isnis,
                    country_code = countryCode,
                    begin = lifeSpan?.begin,
                    end = lifeSpan?.end,
                    ended = lifeSpan?.ended,
                    area_id = area?.id,
                ),
            )
        }
    }

    fun insertAll(artists: List<ArtistMusicBrainzModel>) {
        transacter.transaction {
            artists.forEach { artist ->
                insertReplace(artist)
            }
        }
    }

    fun getArtistForDetails(artistId: String): ArtistDetailsModel? {
        return transacter.getArtistForDetails(
            artistId,
            mapper = ::toDetailsModel,
        ).executeAsOneOrNull()
    }

    private fun toDetailsModel(
        id: String,
        name: String,
        sortName: String,
        disambiguation: String?,
        type: String?,
        gender: String?,
        ipis: List<String>?,
        isnis: List<String>?,
        begin: String?,
        end: String?,
        ended: Boolean?,
        areaId: String?,
        areaName: String?,
        countryCode: String?,
        visited: Boolean?,
    ): ArtistDetailsModel {
        val area = if (areaId != null && areaName != null) {
            AreaListItemModel(
                id = areaId,
                name = areaName,
                countryCodes = listOfNotNull(countryCode),
                visited = visited == true,
            )
        } else {
            null
        }
        return ArtistDetailsModel(
            id = id,
            name = name,
            sortName = sortName,
            disambiguation = disambiguation,
            type = type,
            gender = gender,
            ipis = ipis,
            isnis = isnis,
            lifeSpan = LifeSpanUiModel(
                begin = begin,
                end = end,
                ended = ended,
            ),
            areaListItemModel = area,
        )
    }

    fun delete(artistId: String) {
        transacter.delete(artistId)
    }
}
