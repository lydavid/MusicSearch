package ly.david.musicsearch.ui.common.topappbar

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp

private val DEFAULT_DOT_SIZE = 12.dp
private const val ANIMATION_DELAY_MS = 300
private const val DURATION = 4
private const val MIN_ALPHA = 0.2f

/**
 * Based off of: [https://gist.github.com/EugeneTheDev/a27664cb7e7899f964348b05883cbccd]
 */
@Composable
fun DotsFlashing(
    modifier: Modifier = Modifier,
) {
    val minAlpha = MIN_ALPHA

    @Composable
    fun Dot(alpha: Float) = Spacer(
        Modifier
            .size(DEFAULT_DOT_SIZE)
            .alpha(alpha)
            .background(
                color = MaterialTheme.colorScheme.onSurface,
                shape = CircleShape,
            ),
    )

    val infiniteTransition = rememberInfiniteTransition()

    @Composable
    fun animateAlphaWithDelay(delay: Int) = infiniteTransition.animateFloat(
        initialValue = minAlpha,
        targetValue = minAlpha,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = ANIMATION_DELAY_MS * DURATION
                minAlpha at delay
                1f at delay + ANIMATION_DELAY_MS
                minAlpha at delay + ANIMATION_DELAY_MS * 2
            },
        ),
    )

    val alpha1 by animateAlphaWithDelay(0)
    val alpha2 by animateAlphaWithDelay(ANIMATION_DELAY_MS)
    val alpha3 by animateAlphaWithDelay(ANIMATION_DELAY_MS * 2)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier,
    ) {
        val spaceSize = 4.dp

        Dot(alpha1)
        Spacer(Modifier.width(spaceSize))
        Dot(alpha2)
        Spacer(Modifier.width(spaceSize))
        Dot(alpha3)
    }
}
