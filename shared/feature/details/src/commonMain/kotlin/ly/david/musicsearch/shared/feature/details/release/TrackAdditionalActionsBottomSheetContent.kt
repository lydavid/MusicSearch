package ly.david.musicsearch.shared.feature.details.release

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ly.david.musicsearch.shared.domain.artist.getDisplayNames
import ly.david.musicsearch.shared.domain.listitem.TrackListItemModel
import ly.david.musicsearch.ui.common.component.ClickableItem
import ly.david.musicsearch.ui.common.icon.CollectionIcon
import ly.david.musicsearch.ui.common.icons.CustomIcons
import ly.david.musicsearch.ui.common.icons.HeadphonesAdd
import ly.david.musicsearch.ui.common.theme.TextStyles
import ly.david.musicsearch.ui.common.track.TrackNumber
import ly.david.musicsearch.ui.common.track.TrackTitleWithLength
import musicsearch.ui.common.generated.resources.Res
import musicsearch.ui.common.generated.resources.addToCollection
import musicsearch.ui.common.generated.resources.submitListen
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun TrackAdditionalActionsBottomSheetContent(
    track: TrackListItemModel,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit = {},
    onAddToCollectionClick: (recordingId: String) -> Unit = {},
    onSubmitListenClick: (track: TrackListItemModel) -> Unit = {},
) {
    Column(
        modifier = modifier.verticalScroll(state = rememberScrollState()),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            TrackNumber(track = track)

            SelectionContainer {
                Column(
                    modifier = Modifier.padding(start = 16.dp),
                ) {
                    TrackTitleWithLength(track = track)

                    Text(
                        text = track.artists.getDisplayNames(),
                        modifier = Modifier
                            .padding(top = 4.dp)
                            .fillMaxWidth(),
                        style = TextStyles.getCardBodySubTextStyle(),
                    )
                }
            }
        }
        HorizontalDivider(modifier = Modifier.padding(top = 16.dp))

        ClickableItem(
            title = stringResource(Res.string.addToCollection),
            startIcon = {
                CollectionIcon(
                    collected = track.collected,
                )
            },
            onClick = {
                onAddToCollectionClick(track.recordingId)
                onDismiss()
            },
        )
        ClickableItem(
            title = stringResource(Res.string.submitListen),
            startIcon = CustomIcons.HeadphonesAdd,
            onClick = {
                onSubmitListenClick(track)
                onDismiss()
            },
        )

        Spacer(modifier = Modifier.padding(bottom = 32.dp))
    }
}
