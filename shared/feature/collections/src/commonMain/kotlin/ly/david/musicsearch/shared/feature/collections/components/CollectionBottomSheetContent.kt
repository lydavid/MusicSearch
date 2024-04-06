package ly.david.musicsearch.shared.feature.collections.components

import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.cash.paging.compose.LazyPagingItems
import ly.david.musicsearch.core.models.listitem.CollectionListItemModel
import ly.david.ui.core.LocalStrings
import ly.david.ui.core.theme.TextStyles

@Suppress("UnusedReceiverParameter")
@OptIn(
    ExperimentalFoundationApi::class,
)
@Composable
internal fun ColumnScope.CollectionBottomSheetContent(
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
