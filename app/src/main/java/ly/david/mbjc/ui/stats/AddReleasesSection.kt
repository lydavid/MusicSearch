package ly.david.mbjc.ui.stats

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ly.david.mbjc.R
import ly.david.mbjc.ui.common.listitem.ListSeparatorHeader
import ly.david.mbjc.ui.common.preview.DefaultPreviews
import ly.david.mbjc.ui.theme.PreviewTheme
import ly.david.mbjc.ui.theme.TextStyles

internal fun LazyListScope.addReleasesSection(
    totalRemote: Int,
    totalLocal: Int,
) {
    item {
        ListSeparatorHeader(text = stringResource(id = R.string.releases))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        ) {
            // TODO: "cached" is misleading here
            //  since the moment they click a release, it will require downloading details
            Text(
                style = TextStyles.getCardBodyTextStyle(),
                text = "Cached releases"
            )
            Text(
                style = TextStyles.getCardBodyTextStyle(),
                text = "$totalLocal / $totalRemote"
            )

            if (totalRemote != 0) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .height(8.dp)
                        .fillMaxWidth(),
                    progress = totalLocal / totalRemote.toFloat(),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                )
            }
        }

        Spacer(modifier = Modifier.padding(top = 16.dp))
    }
}

@DefaultPreviews
@Composable
private fun Preview() {
    PreviewTheme {
        Surface {
            LazyColumn {
                addReleasesSection(
                    totalRemote = 10,
                    totalLocal = 7
                )
            }
        }
    }
}
