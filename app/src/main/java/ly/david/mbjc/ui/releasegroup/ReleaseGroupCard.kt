package ly.david.mbjc.ui.releasegroup

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ly.david.mbjc.data.domain.ReleaseGroupUiModel
import ly.david.mbjc.data.getNameWithDisambiguation
import ly.david.mbjc.ui.common.ClickableListItem
import ly.david.mbjc.ui.common.getYear
import ly.david.mbjc.ui.theme.PreviewTheme
import ly.david.mbjc.ui.theme.TextStyles

// TODO: have 2 modes: query and browse where some data is displayed differently
@Composable
fun ReleaseGroupCard(
    releaseGroup: ReleaseGroupUiModel,
    onClick: (ReleaseGroupUiModel) -> Unit = {}
) {
    ClickableListItem(
        onClick = { onClick(releaseGroup) },
    ) {
        Column(
            modifier = Modifier.padding(vertical = 16.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = releaseGroup.getNameWithDisambiguation(),
                    style = TextStyles.getCardTitleTextStyle(),
                    modifier = Modifier.weight(3f)
                )
                Spacer(modifier = Modifier.padding(4.dp))
                Text(
                    text = releaseGroup.firstReleaseDate.getYear(),
                    modifier = Modifier.weight(1f),
                    style = TextStyles.getCardBodyTextStyle(),
                    textAlign = TextAlign.End
                )
            }

            if (releaseGroup.artistCredits.isNotEmpty()) {
                Text(
                    text = releaseGroup.artistCredits,
                    modifier = Modifier.fillMaxWidth(),
                    style = TextStyles.getCardBodyTextStyle()
                )
            }
        }
    }
}

private val testReleaseGroup = ReleaseGroupUiModel(
    id = "6825ace2-3563-4ac5-8d85-c7bf1334bd2c",
    name = "欠けた心象、世のよすが",
    primaryType = "EP",
    firstReleaseDate = "2021-09-08",
    artistCredits = "Some artist feat. some other artist"
)

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
internal fun ReleaseGroupCardPreview() {
    PreviewTheme {
        Surface {
            ReleaseGroupCard(testReleaseGroup)
        }
    }
}
