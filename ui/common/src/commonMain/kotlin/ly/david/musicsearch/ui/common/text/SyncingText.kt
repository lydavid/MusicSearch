package ly.david.musicsearch.ui.common.text

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.unit.dp
import ly.david.musicsearch.ui.common.theme.TextStyles

@Composable
fun SyncingText(
    message: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
    ) {
        // TODO: Any screenshot tests with animations such as CircularProgressIndicator may flake
        //  until https://github.com/cashapp/paparazzi/issues/678 or https://github.com/cashapp/paparazzi/issues/627 are resolved
        if (!LocalInspectionMode.current) {
            CircularProgressIndicator(
                modifier = modifier.size(16.dp),
            )
        }
        Text(
            text = message,
            modifier = modifier.padding(start = 4.dp),
            style = TextStyles.getCardBodySubTextStyle(),
        )
    }
}
