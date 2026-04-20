package ly.david.musicsearch.shared.feature.search.url

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import kotlinx.collections.immutable.persistentListOf
import ly.david.musicsearch.shared.domain.alias.BasicAlias
import ly.david.musicsearch.shared.domain.listitem.RelationListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.ui.common.preview.PreviewTheme
import ly.david.musicsearch.ui.common.preview.PreviewWithTransitionAndOverlays

@PreviewLightDark
@Composable
internal fun PreviewLookupUrlUi() {
    PreviewTheme {
        LookupUrlUi(
            state = LookupUrlUiState(
                urlToLookup = "",
                result = null,
            ),
        )
    }
}

@PreviewLightDark
@Composable
internal fun PreviewLookupUrlTrimParametersChecked() {
    PreviewTheme {
        LookupUrlUi(
            state = LookupUrlUiState(
                urlToLookup = "",
                excludeParameters = true,
                result = null,
            ),
        )
    }
}

@PreviewLightDark
@Composable
internal fun PreviewLookupUrlUiLoading() {
    PreviewTheme {
        LookupUrlUi(
            state = LookupUrlUiState(
                urlToLookup = "https://open.spotify.com/track/59hVbgr8rfYkDbHfr8RcGI",
                excludeParameters = true,
                result = LookupUrlUiState.Result.Loading,
            ),
        )
    }
}

@PreviewLightDark
@Composable
internal fun PreviewLookupUrlUiNoResults() {
    PreviewWithTransitionAndOverlays {
        LookupUrlUi(
            state = LookupUrlUiState(
                urlToLookup = "https://something",
                searchLocalDatabase = true,
                result = LookupUrlUiState.Result.Success(
                    listItemModels = persistentListOf(),
                ),
            ),
        )
    }
}

@PreviewLightDark
@Composable
internal fun PreviewLookupUrlUiSingleResult() {
    PreviewWithTransitionAndOverlays {
        LookupUrlUi(
            state = LookupUrlUiState(
                urlToLookup = "https://open.spotify.com/artist/1snhtMLeb2DYoMOcVbb8iB",
                result = LookupUrlUiState.Result.Success(
                    listItemModels = persistentListOf(
                        RelationListItemModel(
                            id = "09d4a85c-4916-4b4e-bc96-c4cfcf371046",
                            linkedEntity = MusicBrainzEntityType.ARTIST,
                            linkedEntityId = "09d4a85c-4916-4b4e-bc96-c4cfcf371046",
                            type = "",
                            name = "米津玄師",
                            aliases = persistentListOf(
                                BasicAlias(
                                    name = "Kenshi Yonezu",
                                    locale = "en",
                                    isPrimary = true,
                                ),
                            ),
                        ),
                    ),
                ),
            ),
        )
    }
}

// This example happens to have the same name, so we can't distinguish at the moment
// They have different release events, but we don't show those on a relation list item
@PreviewLightDark
@Composable
internal fun PreviewLookupUrlUiMultipleResults() {
    PreviewWithTransitionAndOverlays {
        LookupUrlUi(
            state = LookupUrlUiState(
                urlToLookup = "https://open.spotify.com/album/6pZ0SrZCP8Bm28L6JhMtBy",
                result = LookupUrlUiState.Result.Success(
                    listItemModels = persistentListOf(
                        RelationListItemModel(
                            id = "db0c3753-9394-4e15-ae35-dcdecd108132",
                            linkedEntity = MusicBrainzEntityType.RELEASE,
                            linkedEntityId = "db0c3753-9394-4e15-ae35-dcdecd108132",
                            type = "",
                            name = "盗作",
                        ),
                        RelationListItemModel(
                            id = "ad46ead9-e62b-43fe-bb73-767f40881113",
                            linkedEntity = MusicBrainzEntityType.RELEASE,
                            linkedEntityId = "ad46ead9-e62b-43fe-bb73-767f40881113",
                            type = "",
                            name = "盗作",
                        ),
                    ),
                ),
            ),
        )
    }
}

@PreviewLightDark
@Composable
internal fun PreviewLookupUrlUiCannotBeEmpty() {
    PreviewTheme {
        LookupUrlUi(
            state = LookupUrlUiState(
                urlToLookup = "",
                result = LookupUrlUiState.Result.Error.CannotBeEmpty,
            ),
        )
    }
}

@PreviewLightDark
@Composable
internal fun PreviewLookupUrlUiBadRequest() {
    PreviewTheme {
        LookupUrlUi(
            state = LookupUrlUiState(
                urlToLookup = "https://newurl",
                result = LookupUrlUiState.Result.Error.BadRequest(
                    url = "oldurl",
                ),
            ),
        )
    }
}

@PreviewLightDark
@Composable
internal fun PreviewLookupUrlUiOtherError() {
    PreviewTheme {
        LookupUrlUi(
            state = LookupUrlUiState(
                urlToLookup = "https://newurl",
                result = LookupUrlUiState.Result.Error.Other(
                    message = "Some other error",
                ),
            ),
        )
    }
}
