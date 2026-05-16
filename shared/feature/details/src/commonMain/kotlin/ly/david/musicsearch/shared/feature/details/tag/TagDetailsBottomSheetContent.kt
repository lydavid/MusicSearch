package ly.david.musicsearch.shared.feature.details.tag

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ly.david.musicsearch.shared.domain.error.Feedback
import ly.david.musicsearch.shared.domain.tag.GenreChip
import ly.david.musicsearch.shared.domain.tag.TagChip
import ly.david.musicsearch.shared.domain.tag.TagRepository
import ly.david.musicsearch.shared.domain.tag.VoteType
import ly.david.musicsearch.ui.common.component.ClickableItem
import ly.david.musicsearch.ui.common.icons.Add
import ly.david.musicsearch.ui.common.icons.CustomIcons
import ly.david.musicsearch.ui.common.icons.Remove
import ly.david.musicsearch.ui.common.icons.Search
import ly.david.musicsearch.ui.common.icons.TheaterComedy
import ly.david.musicsearch.ui.common.text.SyncingText
import musicsearch.ui.common.generated.resources.Res
import musicsearch.ui.common.generated.resources.downvotedX
import musicsearch.ui.common.generated.resources.failedToVoteForX
import musicsearch.ui.common.generated.resources.goToGenre
import musicsearch.ui.common.generated.resources.loginAgainForTags
import musicsearch.ui.common.generated.resources.searchGenre
import musicsearch.ui.common.generated.resources.searchTag
import musicsearch.ui.common.generated.resources.syncingWithMusicBrainz
import musicsearch.ui.common.generated.resources.upvotedX
import musicsearch.ui.common.generated.resources.withdrawnVoteForX
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource

@Composable
fun TagDetailsBottomSheetContent(
    state: TagDetailsUiState,
) {
    val genreOrTag = state.genreOrTag
    val eventSink = state.eventSink

    var message: String? by remember { mutableStateOf(null) }
    state.feedback?.let { feedback ->
        LaunchedEffect(feedback) {
            message = feedback.getMessage()
        }
    }

    Column(
        modifier = Modifier.verticalScroll(state = rememberScrollState()),
    ) {
        Box(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(end = 144.dp, bottom = 4.dp),
            ) {
                GenreOrTagChip(
                    genreOrTag = genreOrTag,
                    filterText = "",
                )
                message?.let { message ->
                    SyncingText(
                        message = message,
                        modifier = Modifier.padding(top = 4.dp),
                    )
                }
            }

            Row(
                modifier = Modifier.align(Alignment.CenterEnd),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    modifier = Modifier.padding(end = 8.dp),
                    text = genreOrTag.count.toString(),
                    color = when (genreOrTag.voteType) {
                        VoteType.Upvote -> MaterialTheme.colorScheme.primary
                        VoteType.Downvote -> MaterialTheme.colorScheme.error
                        VoteType.Withdraw -> Color.Unspecified
                    },
                )

                CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides 0.dp) {
                    IconButton(
                        onClick = {
                            eventSink(TagDetailsUiEvent.Vote(voteType = VoteType.Upvote))
                        },
                    ) {
                        Icon(
                            imageVector = CustomIcons.Add,
                            tint = if (genreOrTag.voteType == VoteType.Upvote) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                LocalContentColor.current
                            },
                            contentDescription = null,
                        )
                    }
                    IconButton(
                        onClick = {
                            eventSink(TagDetailsUiEvent.Vote(voteType = VoteType.Downvote))
                        },
                    ) {
                        Icon(
                            imageVector = CustomIcons.Remove,
                            tint = if (genreOrTag.voteType == VoteType.Downvote) {
                                MaterialTheme.colorScheme.error
                            } else {
                                LocalContentColor.current
                            },
                            contentDescription = null,
                        )
                    }
                }
            }
        }

        // TODO: support filtering local database with genre/tag
        ClickableItem(
            title = stringResource(
                when (genreOrTag) {
                    is GenreChip -> Res.string.searchGenre
                    is TagChip -> Res.string.searchTag
                },
            ),
            startIcon = CustomIcons.Search,
            onClick = {
                eventSink(TagDetailsUiEvent.SearchGenreOrTag(genreOrTag.name))
            },
        )

        if (genreOrTag is GenreChip) {
            ClickableItem(
                title = stringResource(Res.string.goToGenre),
                startIcon = CustomIcons.TheaterComedy,
                onClick = {
                    eventSink(TagDetailsUiEvent.GoToGenre(genreOrTag.id))
                },
            )
        }
    }
}

internal suspend fun Feedback<TagRepository.TagFeedback>.getMessage(): String {
    return when (val data = this.data) {
        TagRepository.TagFeedback.Syncing -> {
            getString(Res.string.syncingWithMusicBrainz)
        }

        is TagRepository.TagFeedback.AuthMissingTagScope -> {
            "${getString(Res.string.failedToVoteForX, data.name)} ${getString(Res.string.loginAgainForTags)}"
        }

        is TagRepository.TagFeedback.FailedToVote -> {
            "${getString(Res.string.failedToVoteForX, data.name)} ${data.errorMessage}"
        }

        is TagRepository.TagFeedback.Voted -> {
            val tagName = data.name
            when (data.voteType) {
                VoteType.Upvote -> getString(Res.string.upvotedX, tagName)
                VoteType.Downvote -> getString(Res.string.downvotedX, tagName)
                VoteType.Withdraw -> getString(Res.string.withdrawnVoteForX, tagName)
            }
        }
    }
}
