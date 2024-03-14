package ly.david.musicsearch.shared.feature.collections.add

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.paging.compose.LazyPagingItems
import ly.david.musicsearch.core.models.listitem.CollectionListItemModel
import ly.david.musicsearch.shared.feature.collections.components.CollectionListItem
import ly.david.musicsearch.shared.feature.collections.components.CreateCollectionDialogContent
import ly.david.musicsearch.shared.feature.collections.list.NewCollection
import ly.david.musicsearch.strings.LocalStrings
import ly.david.ui.core.theme.TextStyles

@Composable
internal fun AddToCollectionUi(
    state: AddToCollectionUiState,
    modifier: Modifier,
) {
    val eventSink = state.eventSink
    var showDialog by rememberSaveable { mutableStateOf(false) }

    if (showDialog) {
        Dialog(
            onDismissRequest = {
                showDialog = false
            },
        ) {
            Surface {
                CreateCollectionDialogContent(
                    onDismiss = { showDialog = false },
                    onSubmit = { name, entity ->
                        eventSink(
                            AddToCollectionUiEvent.CreateCollection(
                                NewCollection(
                                    name,
                                    entity,
                                ),
                            ),
                        )
                    },
                )
            }
        }
    }

    Column(modifier = modifier) {
        CollectionBottomSheetContent(
            collections = state.lazyPagingItems,
            onCreateCollectionClick = {
                showDialog = true
            },
            onAddToCollection = { collectionId ->
                eventSink(AddToCollectionUiEvent.AddToCollection(collectionId))
            },
        )
    }
}

@Suppress("UnusedReceiverParameter")
@OptIn(
    ExperimentalFoundationApi::class,
)
@Composable
private fun ColumnScope.CollectionBottomSheetContent(
    collections: LazyPagingItems<CollectionListItemModel>,
    onCreateCollectionClick: () -> Unit = {},
    onAddToCollection: (collectionId: String) -> Unit = {},
) {
    val strings = LocalStrings.current

    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = strings.addToCollection,
            modifier = Modifier
                .padding(horizontal = 16.dp),
            style = TextStyles.getCardBodyTextStyle(),
        )

        Spacer(modifier = Modifier.weight(1f))

        IconButton(onClick = onCreateCollectionClick) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = strings.createCollection,
            )
        }
    }

    Divider(modifier = Modifier.padding(top = 16.dp))

    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
    ) {
        items(
            count = collections.itemCount,
        ) { index ->
            when (val collection = collections[index]) {
                is CollectionListItemModel -> {
                    CollectionListItem(
                        collection = collection,
                        modifier = Modifier.animateItemPlacement(),
                        onClick = {
                            onAddToCollection(collection.id)
                        },
                    )
                }

                else -> {
                    // Do nothing.
                }
            }
        }
        item {
            Spacer(modifier = Modifier.padding(bottom = 16.dp))
        }
    }
}
