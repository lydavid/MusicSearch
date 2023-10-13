package ly.david.ui.collections

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ly.david.musicsearch.data.core.listitem.CollectionListItemModel
import ly.david.musicsearch.strings.LocalStrings
import ly.david.ui.core.theme.TextStyles

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun CollectionBottomSheet(
    bottomSheetState: SheetState,
    scope: CoroutineScope,
    collections: LazyPagingItems<CollectionListItemModel>,
    onDismiss: () -> Unit = {},
    onCreateCollectionClick: () -> Unit = {},
    onAddToCollection: suspend (collectionId: String) -> Unit = {},
) {
    val strings = LocalStrings.current

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = bottomSheetState,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = strings.addToCollection,
                modifier = Modifier
                    .padding(horizontal = 16.dp),
                style = TextStyles.getCardBodyTextStyle()
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
                                scope.launch {
                                    onAddToCollection(collection.id)
                                    bottomSheetState.hide()
                                }.invokeOnCompletion {
                                    if (!bottomSheetState.isVisible) {
                                        onDismiss()
                                    }
                                }
                            }
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
}
