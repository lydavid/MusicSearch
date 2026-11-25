package ly.david.musicsearch.shared.feature.collections.edit

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import ly.david.musicsearch.shared.domain.common.ifNotNullOrEmpty
import ly.david.musicsearch.shared.domain.error.Feedback
import ly.david.musicsearch.shared.feature.collections.components.CollectionListItem
import ly.david.musicsearch.ui.common.collection.getMessage
import ly.david.musicsearch.ui.common.icons.Add
import ly.david.musicsearch.ui.common.icons.CustomIcons
import ly.david.musicsearch.ui.common.text.SyncingText
import ly.david.musicsearch.ui.common.topappbar.TopAppBarWithFilter
import musicsearch.ui.common.generated.resources.Res
import musicsearch.ui.common.generated.resources.apply
import musicsearch.ui.common.generated.resources.createCollection
import musicsearch.ui.common.generated.resources.editingX
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CollectionBottomSheetContent(
    state: EditCollectionUiState,
    modifier: Modifier = Modifier,
    onCreateCollectionClick: () -> Unit = {},
) {
    val eventSink = state.eventSink

    val feedback = state.feedback
    val topAppBarFilterState = state.filterState
    val pendingEdits = state.pendingEdits

    var message: String? by remember { mutableStateOf(null) }
    feedback?.let { feedback ->
        LaunchedEffect(feedback) {
            message = feedback.getMessage()
        }
    }

    Box(
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            TopAppBarWithFilter(
                title = stringResource(Res.string.editingX, state.numberOfItemsToAddToCollection),
                onBack = {
                    eventSink(EditCollectionUiEvent.Dismiss)
                },
                topAppBarFilterState = topAppBarFilterState,
                windowInsets = WindowInsets(0),
                additionalActions = {
                    IconButton(onClick = onCreateCollectionClick) {
                        Icon(
                            imageVector = CustomIcons.Add,
                            contentDescription = stringResource(Res.string.createCollection),
                        )
                    }
                },
                additionalBar = {
                    message.ifNotNullOrEmpty { message ->
                        SyncingText(
                            message = message,
                            modifier = Modifier.padding(start = 8.dp, top = 4.dp, bottom = 4.dp),
                        )
                    }
                    HorizontalDivider()
                },
            )

            val collections = state.lazyPagingItems
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
            ) {
                items(
                    count = collections.itemCount,
                    key = { index -> collections[index]?.id.orEmpty() },
                ) {
                    val collection = collections[it] ?: return@items
                    val pendingEdit = pendingEdits[collection.id]?.pendingEdit

                    CollectionListItem(
                        collection = collection.copy(
                            pendingEdit = pendingEdit,
                        ),
                        boldUnvisited = state.boldUnvisited,
                        query = topAppBarFilterState.filterText,
                        onClick = { collection ->
                            eventSink(EditCollectionUiEvent.ClickCollection(collection))
                        },
                    )
                }

                item {
                    Spacer(modifier = Modifier.padding(bottom = 64.dp))
                }
            }
        }

        if (state.confirmChanges) {
            Button(
                enabled = pendingEdits.isNotEmpty() && feedback !is Feedback.Loading,
                onClick = {
                    eventSink(EditCollectionUiEvent.ApplyChanges)
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 16.dp),
            ) {
                if (feedback is Feedback.Loading) {
                    if (!LocalInspectionMode.current) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            color = MaterialTheme.colorScheme.primaryFixedDim,
                        )
                    }
                }
                Text(
                    text = stringResource(Res.string.apply),
                    modifier = Modifier.padding(start = 4.dp),
                )
            }
        }
    }
}
