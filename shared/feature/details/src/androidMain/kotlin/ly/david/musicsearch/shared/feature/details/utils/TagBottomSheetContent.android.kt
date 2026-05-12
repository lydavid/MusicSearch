package ly.david.musicsearch.shared.feature.details.utils

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import ly.david.musicsearch.shared.domain.tag.GenreChip
import ly.david.musicsearch.shared.domain.tag.TagChip
import ly.david.musicsearch.ui.common.preview.PreviewTheme

@PreviewLightDark
@Composable
internal fun PreviewTagBottomSheetContentGenre() {
    PreviewTheme {
        Surface {
            TagBottomSheetContent(
                genreOrTag = GenreChip(
                    id = "a",
                    name = "alternative rock",
                    count = 8,
                ),
            )
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewTagBottomSheetContentTag() {
    PreviewTheme {
        Surface {
            TagBottomSheetContent(
                genreOrTag = TagChip(
                    name = "special purpose label",
                    count = 8,
                ),
            )
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewTagBottomSheetContentLongTag() {
    PreviewTheme {
        Surface {
            TagBottomSheetContent(
                genreOrTag = TagChip(
                    name = "a very long tag is that is likely spam, but let's handle it anyways",
                    count = 98765,
                ),
            )
        }
    }
}
