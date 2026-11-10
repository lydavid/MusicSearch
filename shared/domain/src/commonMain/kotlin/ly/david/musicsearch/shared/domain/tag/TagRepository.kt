package ly.david.musicsearch.shared.domain.tag

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.shared.domain.error.Feedback
import ly.david.musicsearch.shared.domain.listitem.TaggedEntityListItemModel
import ly.david.musicsearch.shared.domain.musicbrainz.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.parcelize.CommonParcelable
import ly.david.musicsearch.shared.domain.parcelize.Parcelize

interface TagRepository {
    suspend fun voteOnTagForEntity(
        genreOrTag: GenreOrTag,
        musicBrainzEntity: MusicBrainzEntity,
        voteType: VoteType,
    ): Flow<Feedback<TagFeedback>>

    /**
     * All locally cached entities carrying a tag or genre named [name], in a single paged list
     * across every entity type.
     */
    fun observeEntitiesByTagOrGenre(
        name: String,
        query: String = "%",
    ): Flow<PagingData<TaggedEntityListItemModel>>

    @Parcelize
    sealed interface TagFeedback : CommonParcelable {
        data object Syncing : TagFeedback

        /**
         * This state exists because the app did not request for `tag` scope with MusicBrainz OAuth until now.
         */
        data class AuthMissingTagScope(
            val name: String,
        ) : TagFeedback

        data class FailedToVote(
            val name: String,
            val errorMessage: String,
        ) : TagFeedback

        data class Voted(
            val name: String,
            val voteType: VoteType,
        ) : TagFeedback
    }
}
