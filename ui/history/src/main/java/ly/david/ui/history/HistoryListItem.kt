package ly.david.ui.history

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ListItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import ly.david.data.domain.listitem.LookupHistoryListItemModel
import ly.david.data.network.MusicBrainzResource
import ly.david.ui.common.getDisplayTextRes
import ly.david.ui.common.getIcon
import ly.david.ui.common.listitem.SwipeToDeleteListItem
import ly.david.ui.core.preview.DefaultPreviews
import ly.david.ui.core.theme.PreviewTheme
import ly.david.ui.core.theme.TextStyles
import ly.david.ui.image.ThumbnailImage

@Composable
internal fun HistoryListItem(
    lookupHistory: LookupHistoryListItemModel,
    modifier: Modifier = Modifier,
    onItemClick: (entity: MusicBrainzResource, id: String, title: String?) -> Unit = { _, _, _ -> },
    onDeleteItem: (LookupHistoryListItemModel) -> Unit = {}
) {
    SwipeToDeleteListItem(
        content = {
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
                    val clipModifier = if (lookupHistory.resource == MusicBrainzResource.ARTIST) {
                        Modifier.clip(CircleShape)
                    } else {
                        Modifier
                    }
                    ThumbnailImage(
                        url = lookupHistory.imageUrl.orEmpty(),
                        mbid = lookupHistory.id,
                        placeholderIcon = lookupHistory.resource.getIcon(),
                        modifier = clipModifier
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

// region Previews
@DefaultPreviews
@Composable
internal fun PreviewLookupHistoryReleaseGroup(
    imageUrl: String = "https://www.example.com/image.jpg"
) {
    PreviewTheme {
        Surface {
            HistoryListItem(
                LookupHistoryListItemModel(
                    title = "欠けた心象、世のよすが",
                    resource = MusicBrainzResource.RELEASE_GROUP,
                    id = "81d75493-78b6-4a37-b5ae-2a3918ee3756",
                    numberOfVisits = 9999,
                    imageUrl = imageUrl,
                    lastAccessed = Date(2023, 5, 2)
                )
            )
        }
    }
}

@DefaultPreviews
@Composable
internal fun PreviewLookupHistoryRelease(
    imageUrl: String = "https://www.example.com/image.jpg"
) {
    PreviewTheme {
        Surface {
            HistoryListItem(
                LookupHistoryListItemModel(
                    title = "欠けた心象、世のよすが",
                    resource = MusicBrainzResource.RELEASE,
                    id = "165f6643-2edb-4795-9abe-26bd0533e59d",
                    imageUrl = imageUrl,
                    lastAccessed = Date(2023, 5, 2)
                )
            )
        }
    }
}

@DefaultPreviews
@Composable
internal fun PreviewLookupHistoryArtist(
    imageUrl: String = "https://www.example.com/image.jpg"
) {
    PreviewTheme {
        Surface {
            HistoryListItem(
                LookupHistoryListItemModel(
                    title = "月詠み",
                    resource = MusicBrainzResource.ARTIST,
                    id = "6825ace2-3563-4ac5-8d85-c7bf1334bd2c",
                    imageUrl = imageUrl,
                    lastAccessed = Date(2023, 5, 2)
                )
            )
        }
    }
}
// endregion
