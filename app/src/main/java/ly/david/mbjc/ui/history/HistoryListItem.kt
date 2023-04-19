package ly.david.mbjc.ui.history

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ListItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import ly.david.data.network.MusicBrainzResource
import ly.david.data.persistence.history.LookupHistoryRoomModel
import ly.david.mbjc.ui.common.ResourceIcon
import ly.david.mbjc.ui.common.SMALL_COVER_ART_SIZE
import ly.david.mbjc.ui.common.getDisplayTextRes
import ly.david.mbjc.ui.common.listitem.SwipeToDeleteListItem
import ly.david.mbjc.ui.common.preview.DefaultPreviews
import ly.david.mbjc.ui.theme.PreviewTheme
import ly.david.mbjc.ui.theme.TextStyles

@Composable
internal fun HistoryListItem(
    lookupHistory: LookupHistoryRoomModel,
    modifier: Modifier = Modifier,
    onItemClick: (entity: MusicBrainzResource, id: String, title: String?) -> Unit = { _, _, _ -> },
    onDeleteItem: (LookupHistoryRoomModel) -> Unit = {}
) {
    SwipeToDeleteListItem(
        dismissContent = {
            ListItem(
                headlineContent = {
                    Text(
                        text = lookupHistory.title,
                        style = TextStyles.getCardBodyTextStyle(),
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                modifier = modifier.clickable {
                    onItemClick(lookupHistory.resource, lookupHistory.id, lookupHistory.title)
                },
                supportingContent = {
                    Column {
                        val resource = stringResource(id = lookupHistory.resource.getDisplayTextRes())
                        Text(
                            text = resource,
                            style = TextStyles.getCardBodySubTextStyle(),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Text(
                            text = "Last visited: ${lookupHistory.lastAccessed.toDisplayDate()}",
                            style = TextStyles.getCardBodySubTextStyle(),
                        )
                    }
                },
                leadingContent = {
                    ResourceIcon(
                        resource = lookupHistory.resource,
                        modifier = Modifier.size(SMALL_COVER_ART_SIZE.dp)
                    )
                },
                trailingContent = {
                    Text(
                        text = lookupHistory.numberOfVisits.toString(),
                        style = TextStyles.getCardBodySubTextStyle()
                    )
                }
            )
        },
        onDelete = {
            onDeleteItem(lookupHistory)
        }
    )
}

private fun Date.toDisplayDate(): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    return dateFormat.format(this)
}

internal class LookupHistoryPreviewParameterProvider : PreviewParameterProvider<LookupHistoryRoomModel> {
    override val values: Sequence<LookupHistoryRoomModel> = sequenceOf(
        LookupHistoryRoomModel(
            title = "欠けた心象、世のよすが",
            resource = MusicBrainzResource.RELEASE_GROUP,
            id = "81d75493-78b6-4a37-b5ae-2a3918ee3756",
            numberOfVisits = 9999
        ),
        LookupHistoryRoomModel(
            title = "欠けた心象、世のよすが",
            resource = MusicBrainzResource.RELEASE,
            id = "165f6643-2edb-4795-9abe-26bd0533e59d"
        ),
        LookupHistoryRoomModel(
            title = "月詠み",
            resource = MusicBrainzResource.ARTIST,
            id = "6825ace2-3563-4ac5-8d85-c7bf1334bd2c"
        )
    )
}

@DefaultPreviews
@Composable
private fun Preview(
    @PreviewParameter(LookupHistoryPreviewParameterProvider::class) history: LookupHistoryRoomModel
) {
    PreviewTheme {
        Surface {
            HistoryListItem(history)
        }
    }
}
