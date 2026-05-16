package ly.david.musicsearch.shared.feature.details.tag

import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import ly.david.musicsearch.shared.domain.common.ifNotEmpty
import ly.david.musicsearch.shared.domain.tag.GenreChip
import ly.david.musicsearch.shared.domain.tag.GenreOrTag
import ly.david.musicsearch.shared.domain.tag.TagChip
import ly.david.musicsearch.shared.feature.details.utils.CollapsibleSection
import ly.david.musicsearch.shared.feature.details.utils.getNumberOfFilteredItems
import ly.david.musicsearch.ui.common.listitem.CollapsibleListSeparatorHeader
import musicsearch.ui.common.generated.resources.Res
import musicsearch.ui.common.generated.resources.genres
import musicsearch.ui.common.generated.resources.tags
import org.jetbrains.compose.resources.stringResource

internal fun LazyListScope.genresAndTagsSection(
    genres: ImmutableList<GenreChip>,
    tags: ImmutableList<TagChip>,
    filterText: String,
    isGenresCollapsed: Boolean = false,
    isTagsCollapsed: Boolean = false,
    onCollapseExpand: (CollapsibleSection) -> Unit,
    onChipClick: (GenreOrTag) -> Unit,
) {
    genresOrTagsSection(
        genresOrTags = genres,
        filterText = filterText,
        isCollapsed = isGenresCollapsed,
        onCollapseExpand = { onCollapseExpand(CollapsibleSection.Genres) },
        onChipClick = onChipClick,
    )

    genresOrTagsSection(
        genresOrTags = tags,
        filterText = filterText,
        isCollapsed = isTagsCollapsed,
        onCollapseExpand = { onCollapseExpand(CollapsibleSection.Tags) },
        onChipClick = onChipClick,
    )
}

private fun LazyListScope.genresOrTagsSection(
    genresOrTags: ImmutableList<GenreOrTag>,
    filterText: String,
    isCollapsed: Boolean,
    onCollapseExpand: () -> Unit,
    onChipClick: (GenreOrTag) -> Unit,
) {
    val filteredGenresOrTags = genresOrTags.filter { genreOrTag ->
        genreOrTag.fullName.contains(filterText)
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
                // TODO: hide downvoted tags, with option to show them again
//                endContent = {
//                    Icon(
//                        imageVector = CustomIcons.MoreVert,
//                        modifier = Modifier
//                            .clip(CircleShape)
//                            .clickable {
//
//                            },
//                        contentDescription = stringResource(Res.string.moreActions),
//                    )
//                },
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
