package ly.david.musicsearch.shared.feature.collections.edit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import ly.david.musicsearch.shared.domain.collection.EditACollectionFeedback
import ly.david.musicsearch.shared.domain.error.Feedback
import ly.david.musicsearch.shared.domain.listitem.CollectionListItemModel
import ly.david.musicsearch.shared.feature.collections.components.CollectionListItem
import ly.david.musicsearch.ui.common.collection.getMessage
import ly.david.musicsearch.ui.common.icons.Add
import ly.david.musicsearch.ui.common.icons.CustomIcons
import ly.david.musicsearch.ui.common.theme.TextStyles
import musicsearch.ui.common.generated.resources.Res
import musicsearch.ui.common.generated.resources.addXCountToCollection
import musicsearch.ui.common.generated.resources.createCollection
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun CollectionBottomSheetContent(
    collections: LazyPagingItems<CollectionListItemModel>,
    numberOfItemsToAddToCollection: Int,
    modifier: Modifier = Modifier,
    feedback: Feedback<EditACollectionFeedback>? = null,
    onCreateCollectionClick: () -> Unit = {},
    onAddToCollection: (collectionId: String) -> Unit = {},
) {
    var message: String? by remember { mutableStateOf(null) }

    feedback?.let { feedback ->
        LaunchedEffect(feedback) {
            message = feedback.getMessage()
        }
    }

    LazyColumn(
        modifier = modifier.fillMaxWidth(),
    ) {
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = 16.dp),
            ) {
                Column {
                    Text(
                        text = stringResource(Res.string.addXCountToCollection, numberOfItemsToAddToCollection),
                        style = TextStyles.getCardBodyTextStyle(),
                    )
                    message?.let { message ->
                        Row(
                            modifier = Modifier.padding(top = 4.dp),
                        ) {
                            // TODO: Any screenshot tests with animations such as CircularProgressIndicator may flake
                            //  until https://github.com/cashapp/paparazzi/issues/678 or https://github.com/cashapp/paparazzi/issues/627 are resolved
                            if (!LocalInspectionMode.current) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                )
                            }
                            Text(
                                text = message,
                                modifier = Modifier.padding(start = 4.dp),
                                style = TextStyles.getCardBodySubTextStyle(),
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                IconButton(onClick = onCreateCollectionClick) {
                    Icon(
                        imageVector = CustomIcons.Add,
                        contentDescription = stringResource(Res.string.createCollection),
                    )
                }
            }
        }

        item {
            HorizontalDivider(Modifier.padding(top = if (message == null) 16.dp else 8.dp))
        }

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
