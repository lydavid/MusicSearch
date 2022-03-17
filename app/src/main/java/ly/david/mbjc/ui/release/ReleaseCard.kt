package ly.david.mbjc.ui.release

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ly.david.mbjc.data.domain.UiRelease
import ly.david.mbjc.ui.common.ClickableListItem
import ly.david.mbjc.ui.theme.MusicBrainzJetpackComposeTheme
import ly.david.mbjc.ui.theme.getSubTextColor

// TODO: needs: format/tracks/country/
//  some of these will only be present if we've already previously clicked into this card
@Composable
fun ReleaseCard(
    uiRelease: UiRelease,
    onClick: (UiRelease) -> Unit = {}
) {
    ClickableListItem(
        onClick = { onClick(uiRelease) },
    ) {
        Row(
            modifier = Modifier.padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(
                modifier = Modifier.weight(2f),
            ) {
                Row {
                    Text(
                        text = uiRelease.name,
                        style = MaterialTheme.typography.h6,
                    )
                }

                val disambiguation = uiRelease.disambiguation
                if (disambiguation.isNotEmpty()) {
                    Row {
                        Text(
                            text = "($disambiguation)",
                            color = getSubTextColor(),
                            style = MaterialTheme.typography.body1,
                        )
                    }
                }
            }

            val date = uiRelease.date
            if (!date.isNullOrEmpty()) {
                Spacer(modifier = Modifier.padding(start = 8.dp))
                Text(
                    // TODO: Is there a way to ensure a composable that comes after another
                    //  is given enough space to fit its text?
                    //  ConstraintLayout
                    modifier = Modifier.weight(1f),
                    text = date,
                    style = MaterialTheme.typography.body1,
                    textAlign = TextAlign.End
                )
            }
        }
    }
}

private val testRelease = UiRelease(
    id = "1",
    name = "Release title that is long and wraps",
    disambiguation = "Disambiguation text that is also long",
    date = "2021-09-08",
    country = "JP"
)

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
internal fun ReleaseCardPreview() {
    MusicBrainzJetpackComposeTheme {
        Surface {
            ReleaseCard(testRelease)
        }
    }
}

private val testRelease2 = UiRelease(
    id = "1",
    name = "Release title",
    disambiguation = "Disambiguation text",
    country = "JP"
)

@Preview
@Composable
internal fun ReleaseCardWithoutYearPreview() {
    MusicBrainzJetpackComposeTheme {
        Surface {
            ReleaseCard(testRelease2)
        }
    }
}
