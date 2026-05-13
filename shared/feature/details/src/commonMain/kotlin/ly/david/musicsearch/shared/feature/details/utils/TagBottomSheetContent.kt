package ly.david.musicsearch.shared.feature.details.utils

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ly.david.musicsearch.shared.domain.tag.GenreChip
import ly.david.musicsearch.shared.domain.tag.GenreOrTag
import ly.david.musicsearch.shared.domain.tag.TagChip
import ly.david.musicsearch.ui.common.component.ClickableItem
import ly.david.musicsearch.ui.common.icons.CustomIcons
import ly.david.musicsearch.ui.common.icons.Search
import ly.david.musicsearch.ui.common.icons.TheaterComedy
import musicsearch.ui.common.generated.resources.Res
import musicsearch.ui.common.generated.resources.goToGenre
import musicsearch.ui.common.generated.resources.searchGenre
import musicsearch.ui.common.generated.resources.searchTag
import org.jetbrains.compose.resources.stringResource

@Composable
fun TagBottomSheetContent(
    genreOrTag: GenreOrTag,
    onSearchGenreOrTag: (String) -> Unit = {},
    onGoToGenre: (id: String) -> Unit = {},
    onDismiss: () -> Unit = {},
) {
    Column(
        modifier = Modifier.verticalScroll(state = rememberScrollState()),
    ) {
        Box(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
        ) {
            GenreOrTagChip(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(end = 144.dp, bottom = 4.dp),
                genreOrTag = genreOrTag,
                filterText = "",
            )

            Row(
                modifier = Modifier.align(Alignment.CenterEnd),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    modifier = Modifier.padding(end = 8.dp),
                    text = genreOrTag.count.toString(),
                )

                // TODO: support voting on tags
//                CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides 0.dp) {
//                    IconButton(
//                        onClick = {},
//                    ) {
//                        Icon(
//                            imageVector = CustomIcons.ChevronRight,
//                            modifier = Modifier.rotate(-90f),
//                            contentDescription = null,
//                        )
//                    }
//                    IconButton(
//                        onClick = {},
//                    ) {
//                        Icon(
//                            imageVector = CustomIcons.ChevronRight,
//                            modifier = Modifier.rotate(90f),
//                            contentDescription = null,
//                        )
//                    }
//                }
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
                onSearchGenreOrTag("tag:\"${genreOrTag.name}\"")
                onDismiss()
            },
        )

        if (genreOrTag is GenreChip) {
            ClickableItem(
                title = stringResource(Res.string.goToGenre),
                startIcon = CustomIcons.TheaterComedy,
                onClick = {
                    onGoToGenre(genreOrTag.id)
                    onDismiss()
                },
            )
        }
    }
}
