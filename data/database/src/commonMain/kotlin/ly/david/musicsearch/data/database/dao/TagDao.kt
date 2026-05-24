package ly.david.musicsearch.data.database.dao

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import ly.david.musicsearch.data.database.Database
import ly.david.musicsearch.data.musicbrainz.models.core.GenreMusicBrainzNetworkModel
import ly.david.musicsearch.data.musicbrainz.models.core.TagMusicBrainzNetworkModel
import ly.david.musicsearch.shared.domain.tag.GenreChip
import ly.david.musicsearch.shared.domain.tag.GenreOrTag
import ly.david.musicsearch.shared.domain.tag.TagChip
import ly.david.musicsearch.shared.domain.tag.VoteType
import ly.david.musicsearch.shared.domain.tag.getNewVoteCount

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

    /**
     * [userGenres] and [userTags] are the genres/tags the user has upvoted.
     * Downvotes are not returned through the WS API.
     */
    fun insertAll(
        entityId: String,
        genres: List<GenreMusicBrainzNetworkModel>?,
        userGenres: List<GenreMusicBrainzNetworkModel>?,
        tags: List<TagMusicBrainzNetworkModel>?,
        userTags: List<TagMusicBrainzNetworkModel>?,
    ) {
        val genreNames = genres?.map { it.name }.orEmpty().toSet()
        val isNotGenre: (TagMusicBrainzNetworkModel) -> Boolean = { it.name !in genreNames }

        transacter.transaction {
            genres?.forEach { genre ->
                genreDao.insert(genre)
                insertGenreLink(entityId = entityId, genre = genre)
            }

            tags
                ?.filter(isNotGenre)
                ?.forEach { tag ->
                    insertTag(entityId = entityId, tag = tag)
                }

            val namesToUpvote = userGenres?.map { it.name }.orEmpty() +
                userTags?.filter(isNotGenre)?.map { it.name }.orEmpty()
            namesToUpvote.forEach { name ->
                transacter.upsertVote(
                    entityId = entityId,
                    tagName = name,
                    isUpvote = true,
                )
            }
        }
    }

    fun deleteByEntity(
        entityId: String,
    ) {
        transacter.deleteByEntity(entityId = entityId)
    }

    fun voteOnTagForEntity(
        genreOrTag: GenreOrTag,
        entityId: String,
        newVoteType: VoteType,
    ) {
        withTransaction {
            val newVoteCount = genreOrTag.getNewVoteCount(newVoteType = newVoteType).toLong()
            when (genreOrTag) {
                is GenreChip -> {
                    transacter.updateGenreVoteCount(
                        newCount = newVoteCount,
                        entityId = entityId,
                        genreId = genreOrTag.id,
                    )
                }

                is TagChip -> {
                    transacter.updateTagVoteCount(
                        newCount = newVoteCount,
                        entityId = entityId,
                        tagName = genreOrTag.name,
                    )
                }
            }

            when (newVoteType) {
                VoteType.Withdraw -> {
                    transacter.deleteVote(
                        entityId = entityId,
                        tagName = genreOrTag.name,
                    )
                }

                else -> {
                    transacter.upsertVote(
                        entityId = entityId,
                        tagName = genreOrTag.name,
                        isUpvote = newVoteType == VoteType.Upvote,
                    )
                }
            }
        }
    }

    fun getTags(
        entityId: String,
    ): ImmutableList<TagChip> {
        return transacter.getTagsByEntity(
            entityId = entityId,
            mapper = { name, count, isUpvote ->
                TagChip(
                    name = name,
                    count = count.toInt(),
                    voteType = when (isUpvote) {
                        true -> VoteType.Upvote
                        false -> VoteType.Downvote
                        null -> VoteType.Withdraw
                    },
                )
            },
        ).executeAsList().toPersistentList()
    }

    fun getGenres(
        entityId: String,
    ): ImmutableList<GenreChip> {
        return transacter.getGenresByEntity(
            entityId = entityId,
            mapper = { id, name, disambiguation, count, isUpvote ->
                GenreChip(
                    id = id,
                    name = name,
                    disambiguation = disambiguation,
                    count = count.toInt(),
                    voteType = when (isUpvote) {
                        true -> VoteType.Upvote
                        false -> VoteType.Downvote
                        null -> VoteType.Withdraw
                    },
                )
            },
        ).executeAsList().toPersistentList()
    }
}
