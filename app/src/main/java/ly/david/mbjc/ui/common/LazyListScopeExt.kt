package ly.david.mbjc.ui.common

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

internal fun LazyListScope.addSpacer(spacing: Dp = 16.dp) {
    item {
        Spacer(modifier = Modifier.padding(top = spacing))
    }
}
