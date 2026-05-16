package ly.david.musicsearch.data.repository.tag

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ly.david.musicsearch.data.database.dao.TagDao
import ly.david.musicsearch.data.musicbrainz.api.TagApi
import ly.david.musicsearch.shared.domain.auth.MusicBrainzAuthStore
import ly.david.musicsearch.shared.domain.error.Action
import ly.david.musicsearch.shared.domain.error.ErrorResolution
import ly.david.musicsearch.shared.domain.error.ErrorType
import ly.david.musicsearch.shared.domain.error.Feedback
import ly.david.musicsearch.shared.domain.error.HandledException
import ly.david.musicsearch.shared.domain.musicbrainz.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.network.resourceUri
import ly.david.musicsearch.shared.domain.tag.GenreOrTag
import ly.david.musicsearch.shared.domain.tag.TagRepository
import ly.david.musicsearch.shared.domain.tag.TagVote
import ly.david.musicsearch.shared.domain.tag.VoteType
import kotlin.time.Clock

class TagRepositoryImpl(
    private val tagApi: TagApi,
    private val tagDao: TagDao,
    private val musicBrainzAuthStore: MusicBrainzAuthStore,
    private val clock: Clock,
) : TagRepository {
    override suspend fun voteOnTagForEntity(
        genreOrTag: GenreOrTag,
        musicBrainzEntity: MusicBrainzEntity,
        voteType: VoteType,
    ): Flow<Feedback<TagRepository.TagFeedback>> = flow {
        emit(
            Feedback.Loading(
                data = TagRepository.TagFeedback.Syncing,
                time = clock.now(),
            ),
        )

        try {
            tagApi.postUserTags(
                resourceUriSingular = musicBrainzEntity.type.resourceUri,
                mbid = musicBrainzEntity.id,
                tags = listOf(
                    TagVote(
                        name = genreOrTag.name,
                        voteType = voteType,
                    ),
                ),
            )
        } catch (ex: HandledException) {
            val authMissingTagScope = !musicBrainzAuthStore.getAccessToken().isNullOrEmpty() &&
                ex.errorType == ErrorType.Unauthorized
            val tagFeedback = if (authMissingTagScope) {
                TagRepository.TagFeedback.AuthMissingTagScope(
                    name = genreOrTag.name,
                )
            } else {
                TagRepository.TagFeedback.FailedToVote(
                    name = genreOrTag.name,
                    errorMessage = ex.userMessage,
                )
            }
            emit(
                Feedback.Error(
                    data = tagFeedback,
                    action = Action.Login.takeIf { ex.errorResolution == ErrorResolution.Login },
                    errorResolution = ex.errorResolution,
                    time = clock.now(),
                ),
            )

            return@flow
        }

        // We don't expect this to fail except during a system failure beyond our control.
        tagDao.voteOnTagForEntity(
            genreOrTag = genreOrTag,
            entityId = musicBrainzEntity.id,
            newVoteType = voteType,
        )
        emit(
            Feedback.Success(
                data = TagRepository.TagFeedback.Voted(
                    name = genreOrTag.name,
                    voteType = voteType,
                ),
                time = clock.now(),
            ),
        )
    }
}
