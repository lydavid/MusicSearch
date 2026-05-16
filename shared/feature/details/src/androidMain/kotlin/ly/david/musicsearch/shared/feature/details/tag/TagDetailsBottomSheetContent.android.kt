package ly.david.musicsearch.shared.feature.details.tag

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import ly.david.musicsearch.shared.domain.error.Feedback
import ly.david.musicsearch.shared.domain.tag.GenreChip
import ly.david.musicsearch.shared.domain.tag.TagChip
import ly.david.musicsearch.shared.domain.tag.TagRepository
import ly.david.musicsearch.shared.domain.tag.VoteType
import ly.david.musicsearch.ui.common.preview.PreviewTheme

@PreviewLightDark
@Composable
internal fun PreviewTagDetailsBottomSheetContentGenre() {
    PreviewTheme {
        Surface {
            TagDetailsBottomSheetContent(
                state = TagDetailsUiState(
                    genreOrTag = GenreChip(
                        id = "a",
                        name = "alternative rock",
                        count = 8,
                    ),
                ),
            )
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewTagDetailsBottomSheetContentTag() {
    PreviewTheme {
        Surface {
            TagDetailsBottomSheetContent(
                state = TagDetailsUiState(
                    genreOrTag = TagChip(
                        name = "special purpose label",
                        count = 8,
                    ),
                ),
            )
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewTagDetailsBottomSheetContentLongTag() {
    PreviewTheme {
        Surface {
            TagDetailsBottomSheetContent(
                state = TagDetailsUiState(
                    genreOrTag = TagChip(
                        name = "a very long tag is that is likely spam, but let's handle it anyways",
                        count = 98765,
                    ),
                ),
            )
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewTagDetailsBottomSheetContentGenreUpvote() {
    PreviewTheme {
        Surface {
            TagDetailsBottomSheetContent(
                state = TagDetailsUiState(
                    genreOrTag = GenreChip(
                        id = "a",
                        name = "alternative rock",
                        count = 8,
                        voteType = VoteType.Upvote,
                    ),
                ),
            )
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewTagDetailsBottomSheetContentGenreDownvote() {
    PreviewTheme {
        Surface {
            TagDetailsBottomSheetContent(
                state = TagDetailsUiState(
                    genreOrTag = GenreChip(
                        id = "a",
                        name = "alternative rock",
                        count = 8,
                        voteType = VoteType.Downvote,
                    ),
                ),
            )
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewTagDetailsBottomSheetContentTagSyncing() {
    PreviewTheme {
        Surface {
            TagDetailsBottomSheetContent(
                state = TagDetailsUiState(
                    genreOrTag = TagChip(
                        name = "a very long tag is that is likely spam, but let's handle it anyways",
                        count = 8,
                    ),
                    feedback = Feedback.Loading(
                        data = TagRepository.TagFeedback.Syncing,
                    ),
                ),
            )
        }
    }
}
