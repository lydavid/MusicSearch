package ly.david.mbjc.ui.releasegroup

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import ly.david.data.common.getYear
import ly.david.data.common.ifNotNull
import ly.david.data.domain.ArtistCreditUiModel
import ly.david.data.domain.ReleaseGroupListItemModel
import ly.david.data.getDisplayNames
import ly.david.data.getNameWithDisambiguation
import ly.david.mbjc.ExcludeFromJacocoGeneratedReport
import ly.david.mbjc.ui.common.listitem.ClickableListItem
import ly.david.mbjc.ui.common.preview.DefaultPreviews
import ly.david.mbjc.ui.theme.PreviewTheme
import ly.david.mbjc.ui.theme.TextStyles

// TODO: have 2 modes: query and browse where some data is displayed differently
/**
 * A release group.
 *
 * Type is not displayed because it's displayed as a separator.
 */
@Composable
internal fun ReleaseGroupListItem(
    releaseGroup: ReleaseGroupListItemModel,
    onClick: ReleaseGroupListItemModel.() -> Unit = {}
) {
    ClickableListItem(
        onClick = { onClick(releaseGroup) },
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
        ) {
            val (mainSection, endSection, bottomSection) = createRefs()

            Text(
                text = releaseGroup.getNameWithDisambiguation(),
                style = TextStyles.getCardTitleTextStyle(),
                modifier = Modifier.constrainAs(mainSection) {
                    width = Dimension.fillToConstraints
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    end.linkTo(endSection.start)
                }
            )

            Text(
                text = releaseGroup.firstReleaseDate.getYear(),
                style = TextStyles.getCardBodyTextStyle(),
                textAlign = TextAlign.End,
                modifier = Modifier.constrainAs(endSection) {
                    width = Dimension.wrapContent
                    start.linkTo(mainSection.end, margin = 4.dp)
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                }
            )

            releaseGroup.artistCredits.ifNotNull {
                Text(
                    text = it.getDisplayNames(),
                    style = TextStyles.getCardBodyTextStyle(),
                    modifier = Modifier.constrainAs(bottomSection) {
                        width = Dimension.matchParent
                        top.linkTo(mainSection.bottom, margin = 4.dp)
                    }
                )
            }
        }
    }
}

@ExcludeFromJacocoGeneratedReport
private val testReleaseGroup = ReleaseGroupListItemModel(
    id = "6825ace2-3563-4ac5-8d85-c7bf1334bd2c",
    name = "欠けた心象、世のよすが",
    primaryType = "EP",
    firstReleaseDate = "2021-09-08",
    artistCredits = listOf(
        ArtistCreditUiModel(
            artistId = "2",
            name = "Some artist",
            joinPhrase = " feat. "
        ),
        ArtistCreditUiModel(
            artistId = "3",
            name = "some other artist"
        )
    )
)

@ExcludeFromJacocoGeneratedReport
@DefaultPreviews
@Composable
internal fun ReleaseGroupCardPreview() {
    PreviewTheme {
        Surface {
            ReleaseGroupListItem(testReleaseGroup)
        }
    }
}
