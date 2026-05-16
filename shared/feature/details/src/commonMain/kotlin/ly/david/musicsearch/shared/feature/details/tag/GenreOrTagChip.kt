package ly.david.musicsearch.shared.feature.details.tag

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import ly.david.musicsearch.shared.domain.tag.GenreOrTag
import ly.david.musicsearch.ui.common.listitem.HighlightableText
import ly.david.musicsearch.ui.common.theme.TAG_BACKGROUND_ALPHA

@Composable
internal fun GenreOrTagChip(
    genreOrTag: GenreOrTag,
    filterText: String,
    modifier: Modifier = Modifier,
    onClick: ((GenreOrTag) -> Unit)? = null,
) {
    val defaultModifier = modifier
        .padding(end = 4.dp, top = 4.dp)
        .clip(RoundedCornerShape(percent = 40))
    val finalModifier = if (onClick == null) {
        defaultModifier
    } else {
        defaultModifier.clickable {
            onClick(genreOrTag)
        }
    }

    Surface(
        color = MaterialTheme.colorScheme.primary.copy(alpha = TAG_BACKGROUND_ALPHA),
        modifier = finalModifier,
    ) {
        HighlightableText(
            modifier = Modifier.padding(
                horizontal = 8.dp,
                vertical = 4.dp,
            ),
            text = buildAnnotatedString {
                withStyle(SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                    append(genreOrTag.fullName)
                }
            },
            highlightedText = filterText,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}
