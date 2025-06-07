package ly.david.musicsearch.shared.feature.collections.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.cash.paging.compose.LazyPagingItems
import ly.david.musicsearch.shared.domain.listitem.CollectionListItemModel
import ly.david.musicsearch.ui.common.icons.Add
import ly.david.musicsearch.ui.common.icons.CustomIcons
import ly.david.musicsearch.ui.common.theme.LocalStrings
import ly.david.musicsearch.ui.common.theme.TextStyles

@Composable
internal fun CollectionBottomSheetContent(
    collections: LazyPagingItems<CollectionListItemModel>,
    modifier: Modifier = Modifier,
    onCreateCollectionClick: () -> Unit = {},
    onAddToCollection: (collectionId: String) -> Unit = {},
) {
    Column(modifier = modifier) {
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
                    imageVector = CustomIcons.Add,
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
}
