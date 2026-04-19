package ly.david.musicsearch.shared.feature.search.url

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import ly.david.musicsearch.ui.common.button.CheckboxWithText
import ly.david.musicsearch.ui.common.relation.RelationListItem
import ly.david.musicsearch.ui.common.scaffold.AppScaffold
import ly.david.musicsearch.ui.common.text.LimitedLinesTextField
import ly.david.musicsearch.ui.common.topappbar.ScrollableTopAppBar
import musicsearch.ui.common.generated.resources.Res
import musicsearch.ui.common.generated.resources.foundXResults
import musicsearch.ui.common.generated.resources.lookupUrl
import musicsearch.ui.common.generated.resources.url
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun LookupUrlUi(
    state: LookupUrlUiState,
    modifier: Modifier = Modifier,
) {
    AppScaffold(
        modifier = modifier,
        scrollToHideTopAppBar = state.scrollToHideTopAppBar,
        snackbarHostState = SnackbarHostState(),
        topBar = { scrollBehavior ->
            ScrollableTopAppBar(
                showBackButton = true,
                onBack = {
                    state.eventSink(LookupUrlUiEvent.NavigateUp)
                },
                title = stringResource(Res.string.lookupUrl),
                scrollBehavior = scrollBehavior,
            )
        },
    ) { innerPadding, scrollBehavior ->
        LookupUrlUiContent(
            state = state,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            eventSink = state.eventSink,
        )
    }
}

@Composable
private fun LookupUrlUiContent(
    state: LookupUrlUiState,
    modifier: Modifier = Modifier,
    eventSink: (LookupUrlUiEvent) -> Unit = {},
) {
    LazyColumn(
        modifier = modifier,
    ) {
        item {
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = "Paste a URL to check whether it exists in MusicBrainz.",
            )
        }

        item {
            LimitedLinesTextField(
                modifier = Modifier.padding(top = 16.dp),
                maxLines = 3,
                text = state.urlToLookup,
                textLabel = stringResource(Res.string.url),
                textHint = stringResource(Res.string.url),
                onTextChange = { eventSink(LookupUrlUiEvent.UpdateQuery(it)) },
            )
        }

        val result = state.result

        when (result) {
            is LookupUrlUiState.Result.Error -> {
                item {
                    Text(
                        modifier = Modifier.padding(top = 8.dp, start = 16.dp),
                        text = when (result) {
                            LookupUrlUiState.Result.Error.CannotBeEmpty -> "Query cannot be empty."
                            is LookupUrlUiState.Result.Error.BadRequest -> "The URL (${result.url}) is invalid."
                            is LookupUrlUiState.Result.Error.NotFound -> "Nothing found with the URL (${result.url})."
                            is LookupUrlUiState.Result.Error.Other -> result.message
                        },
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }

            else -> {
                // no-op
            }
        }

        // TODO: Persist options in Lookup URL screen
        item {
            CheckboxWithText(
                text = "Exclude parameters",
                modifier = Modifier.padding(top = 8.dp),
                checked = state.excludeParameters,
                onClick = { eventSink(LookupUrlUiEvent.ToggleExcludeParameters) },
            )
        }

        // TODO: add ability to search for a url in the local database
        //  consider SingleChoiceSegmentedButtonRow instead with the options: MusicBrainz or local database
//        item {
//            CheckboxWithText(
//                text = "Only search local database",
//                modifier = Modifier.padding(top = 8.dp),
//                checked = state.searchLocalOnly,
//                onClick = { eventSink(LookupUrlUiEvent.ToggleLocalOnly) },
//            )
//        }

        item {
            val loading = result is LookupUrlUiState.Result.Loading
            Column(modifier = Modifier.fillMaxWidth()) {
                Button(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 16.dp),
                    onClick = { eventSink(LookupUrlUiEvent.LookupUrl) },
                    enabled = !loading,
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        if (loading) {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .padding(end = 8.dp)
                                    .size(16.dp),
                                color = ButtonDefaults.buttonColors().disabledContentColor,
                            )
                        }

                        Text("Lookup")
                    }
                }
            }
        }

        when (result) {
            is LookupUrlUiState.Result.Success -> {
                val listItemModels = result.listItemModels
                if (listItemModels.isNotEmpty()) {
                    item {
                        Text(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            style = MaterialTheme.typography.bodyMedium,
                            text = pluralStringResource(
                                resource = Res.plurals.foundXResults,
                                quantity = listItemModels.size,
                                listItemModels.size,
                            ),
                        )
                    }
                    items(listItemModels) { listItemModel ->
                        RelationListItem(
                            relation = listItemModel,
                            filterText = "",
                            onItemClick = { entity, id ->
                                eventSink(
                                    LookupUrlUiEvent.ClickItem(
                                        entityType = entity,
                                        id = id,
                                    ),
                                )
                            },
                        )
                    }
                }
            }

            else -> {
                // no-op
            }
        }
    }
}
