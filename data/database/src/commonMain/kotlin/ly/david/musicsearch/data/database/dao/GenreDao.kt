package ly.david.musicsearch.data.database.dao

import ly.david.musicsearch.data.database.Database
import ly.david.musicsearch.data.musicbrainz.models.core.GenreMusicBrainzModel
import lydavidmusicsearchdatadatabase.Genre

class GenreDao(
    database: Database,
) : EntityDao {
    override val transacter = database.genreQueries

    fun insert(genre: GenreMusicBrainzModel) {
        genre.run {
            transacter.insert(
                Genre(
                    id = id,
                    name = name,
                    disambiguation = disambiguation,
                ),
            )
        }
    }

    fun insertAll(genres: List<GenreMusicBrainzModel>?) {
        transacter.transaction {
            genres?.forEach { genre ->
                insert(genre)
            }
        }
    }
}
