package ly.david.musicsearch.data.database.dao

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import ly.david.musicsearch.data.database.Database
import ly.david.musicsearch.data.musicbrainz.models.core.GenreMusicBrainzNetworkModel
import ly.david.musicsearch.data.musicbrainz.models.core.TagMusicBrainzNetworkModel
import ly.david.musicsearch.shared.domain.tag.GenreChip
import ly.david.musicsearch.shared.domain.tag.TagChip

class TagDao(
    database: Database,
    private val genreDao: GenreDao,
) : EntityDao {
    override val transacter = database.tagQueries

    private fun insertGenreLink(
        entityId: String,
        genre: GenreMusicBrainzNetworkModel,
    ) {
        transacter.insertGenreLink(
            mbid = entityId,
            genre_id = genre.id,
            count = genre.count?.toLong() ?: 0,
        )
    }

    private fun insertTag(
        entityId: String,
        tag: TagMusicBrainzNetworkModel,
    ) {
        transacter.insertTag(
            mbid = entityId,
            tag = tag.name,
            count = tag.count?.toLong() ?: 0,
        )
    }

    fun insertAll(
        entityId: String,
        genres: List<GenreMusicBrainzNetworkModel>?,
        tags: List<TagMusicBrainzNetworkModel>?,
    ) {
        transacter.transaction {
            genres?.forEach { genre ->
                genreDao.insert(genre)
                insertGenreLink(entityId = entityId, genre = genre)
            }
            val genreNames = genres?.map { it.name }
            tags
                ?.filter { genreNames?.contains(it.name) == false }
                ?.forEach { tag ->
                    insertTag(entityId = entityId, tag = tag)
                }
        }
    }

    fun deleteByEntity(
        entityId: String,
    ) {
        transacter.deleteByEntity(entityId = entityId)
    }

    fun getTags(
        entityId: String,
    ): ImmutableList<TagChip> {
        return transacter.getTagsByEntity(
            entityId = entityId,
            mapper = { name, count ->
                TagChip(
                    name = name,
                    count = count.toInt(),
                )
            },
        ).executeAsList().toPersistentList()
    }

    fun getGenres(
        entityId: String,
    ): ImmutableList<GenreChip> {
        return transacter.getGenresByEntity(
            entityId = entityId,
            mapper = { id, name, disambiguation, count ->
                GenreChip(
                    id = id,
                    name = name,
                    disambiguation = disambiguation,
                    count = count.toInt(),
                )
            },
        ).executeAsList().toPersistentList()
    }
}
