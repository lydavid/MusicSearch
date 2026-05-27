package ly.david.musicsearch.shared.feature.details.tag

import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import ly.david.musicsearch.shared.domain.common.ifNotEmpty
import ly.david.musicsearch.shared.domain.tag.GenreChip
import ly.david.musicsearch.shared.domain.tag.GenreOrTag
import ly.david.musicsearch.shared.domain.tag.TagChip
import ly.david.musicsearch.shared.domain.tag.VoteType
import ly.david.musicsearch.shared.feature.details.utils.CollapsibleSection
import ly.david.musicsearch.shared.feature.details.utils.getNumberOfFilteredItems
import ly.david.musicsearch.ui.common.button.OverflowMenu
import ly.david.musicsearch.ui.common.icons.CustomIcons
import ly.david.musicsearch.ui.common.icons.Visibility
import ly.david.musicsearch.ui.common.icons.VisibilityOff
import ly.david.musicsearch.ui.common.listitem.CollapsibleListSeparatorHeader
import ly.david.musicsearch.ui.common.topappbar.ToggleMenuItem
import musicsearch.ui.common.generated.resources.Res
import musicsearch.ui.common.generated.resources.genres
import musicsearch.ui.common.generated.resources.hideDownvotedGenresTags
import musicsearch.ui.common.generated.resources.moreActionsFor
import musicsearch.ui.common.generated.resources.showDownvotedGenresTags
import musicsearch.ui.common.generated.resources.tags
import org.jetbrains.compose.resources.stringResource

internal fun LazyListScope.genresAndTagsSection(
    genres: ImmutableList<GenreChip>,
    tags: ImmutableList<TagChip>,
    filterText: String,
    isGenresCollapsed: Boolean = false,
    isTagsCollapsed: Boolean = false,
    onCollapseExpand: (CollapsibleSection) -> Unit,
    showDownvotedTags: Boolean = false,
    onToggleShowDownvotedTag: () -> Unit,
    onChipClick: (GenreOrTag) -> Unit,
) {
    genresOrTagsSection(
        genresOrTags = genres,
        filterText = filterText,
        isCollapsed = isGenresCollapsed,
        onCollapseExpand = { onCollapseExpand(CollapsibleSection.Genres) },
        showDownvotedTags = showDownvotedTags,
        onToggleShowDownvotedTag = onToggleShowDownvotedTag,
        onChipClick = onChipClick,
    )

    genresOrTagsSection(
        genresOrTags = tags,
        filterText = filterText,
        isCollapsed = isTagsCollapsed,
        onCollapseExpand = { onCollapseExpand(CollapsibleSection.Tags) },
        showDownvotedTags = showDownvotedTags,
        onToggleShowDownvotedTag = onToggleShowDownvotedTag,
        onChipClick = onChipClick,
    )
}

private fun LazyListScope.genresOrTagsSection(
    genresOrTags: ImmutableList<GenreOrTag>,
    filterText: String,
    isCollapsed: Boolean,
    onCollapseExpand: () -> Unit,
    showDownvotedTags: Boolean,
    onToggleShowDownvotedTag: () -> Unit,
    onChipClick: (GenreOrTag) -> Unit,
) {
    val filteredGenresOrTags = genresOrTags
        .filter { genreOrTag ->
            val show = genreOrTag.voteType != VoteType.Downvote || showDownvotedTags
            show && genreOrTag.fullName.contains(filterText)
        }
    genresOrTags.ifNotEmpty {
        stickyHeader {
            val numberOfFilteredItems = getNumberOfFilteredItems(
                filteredCount = filteredGenresOrTags.size,
                total = genresOrTags.size,
            )
            val label = when (genresOrTags.first()) {
                is GenreChip -> stringResource(Res.string.genres)
                is TagChip -> stringResource(Res.string.tags)
            }
            CollapsibleListSeparatorHeader(
                text = "$label $numberOfFilteredItems",
                collapsed = isCollapsed,
                onClick = { onCollapseExpand() },
                endContent = {
                    MoreActionsOverflowMenu(
                        showDownvotedTags = showDownvotedTags,
                        onToggleShowDownvotedTag = onToggleShowDownvotedTag,
                        label = label,
                    )
                },
                verticalPadding = 0.dp,
            )
        }
        if (!isCollapsed) {
            item {
                FlowRow(
                    modifier = Modifier.padding(horizontal = 16.dp),
                ) {
                    filteredGenresOrTags.forEach { genreOrTag ->
                        GenreOrTagChip(
                            genreOrTag = genreOrTag,
                            filterText = filterText,
                            onClick = onChipClick,
                        )
                    }
                }
            }
            if (filteredGenresOrTags.isNotEmpty()) {
                item {
                    Spacer(modifier = Modifier.padding(top = 4.dp))
                }
            }
        }
    }
}

@Composable
private fun MoreActionsOverflowMenu(
    showDownvotedTags: Boolean,
    onToggleShowDownvotedTag: () -> Unit,
    label: String,
) {
    OverflowMenu(
        overflowDropdownMenuItems = {
            ToggleMenuItem(
                toggleOnText = stringResource(Res.string.showDownvotedGenresTags),
                toggleOffText = stringResource(Res.string.hideDownvotedGenresTags),
                leadingIcon = {
                    Icon(
                        imageVector = if (showDownvotedTags) {
                            CustomIcons.VisibilityOff
                        } else {
                            CustomIcons.Visibility
                        },
                        contentDescription = null,
                    )
                },
                toggled = showDownvotedTags,
                onToggle = {
                    onToggleShowDownvotedTag()
                },
            )
        },
        contentDescription = stringResource(Res.string.moreActionsFor, label),
        minimumInteractiveComponentSize = 0,
    )
}
