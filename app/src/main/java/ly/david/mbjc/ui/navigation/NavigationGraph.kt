package ly.david.mbjc.ui.navigation

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
import androidx.compose.material3.rememberSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ly.david.data.domain.CollectionListItemModel
import ly.david.data.domain.toCollectionListItemModel
import ly.david.data.navigation.Destination
import ly.david.data.navigation.toLookupDestination
import ly.david.data.network.MusicBrainzResource
import ly.david.data.network.toMusicBrainzResource
import ly.david.data.paging.MusicBrainzPagingConfig
import ly.david.data.persistence.collection.CollectionDao
import ly.david.data.persistence.collection.CollectionEntityRoomModel
import ly.david.data.persistence.collection.CollectionRoomModel
import ly.david.data.persistence.collection.CollectionWithEntities
import ly.david.mbjc.R
import ly.david.mbjc.ui.area.AreaScaffold
import ly.david.mbjc.ui.artist.ArtistScaffold
import ly.david.mbjc.ui.collections.CollectionListItem
import ly.david.mbjc.ui.collections.CollectionListScaffold
import ly.david.mbjc.ui.collections.CollectionScaffold
import ly.david.mbjc.ui.common.dialog.CreateCollectionDialog
import ly.david.mbjc.ui.common.rememberFlowWithLifecycleStarted
import ly.david.mbjc.ui.event.EventScaffold
import ly.david.mbjc.ui.experimental.ExperimentalSettingsScaffold
import ly.david.mbjc.ui.experimental.SpotifyScreen
import ly.david.mbjc.ui.genre.GenreScaffold
import ly.david.mbjc.ui.history.HistoryScaffold
import ly.david.mbjc.ui.instrument.InstrumentScaffold
import ly.david.mbjc.ui.label.LabelScaffold
import ly.david.mbjc.ui.place.PlaceScaffold
import ly.david.mbjc.ui.recording.RecordingScaffold
import ly.david.mbjc.ui.release.ReleaseScaffold
import ly.david.mbjc.ui.releasegroup.ReleaseGroupScaffold
import ly.david.mbjc.ui.search.SearchScreenScaffold
import ly.david.mbjc.ui.series.SeriesScaffold
import ly.david.mbjc.ui.settings.SettingsScaffold
import ly.david.mbjc.ui.theme.TextStyles
import ly.david.mbjc.ui.work.WorkScaffold
import timber.log.Timber

private const val ID = "id"
private const val TITLE = "title"

internal fun NavHostController.goToResource(entity: MusicBrainzResource, id: String, title: String? = null) {
    var route = "${entity.toLookupDestination().route}/$id"
    if (!title.isNullOrEmpty()) route += "?$TITLE=${URLEncoder.encode(title, StandardCharsets.UTF_8.toString())}"
    this.navigate(route)
}

internal fun NavHostController.goTo(destination: Destination) {
    val route = destination.route
    this.navigate(route)
}

// TODO: move these out of navigation to top-level
@HiltViewModel
internal class AppTopLevelViewModel @Inject constructor(
    private val collectionDao: CollectionDao,
) : ViewModel() {

    val resource: MutableStateFlow<MusicBrainzResource> = MutableStateFlow(MusicBrainzResource.ARTIST)

    @OptIn(ExperimentalCoroutinesApi::class)
    val collections: Flow<PagingData<CollectionListItemModel>> =
        resource.flatMapLatest {
            Pager(
                config = MusicBrainzPagingConfig.pagingConfig,
                pagingSourceFactory = {
                    collectionDao.getAllCollectionsOfType(it)
                }
            ).flow.map { pagingData ->
                pagingData.map { collection: CollectionWithEntities ->
                    collection.toCollectionListItemModel()
                }
            }
        }
            .distinctUntilChanged()
            .cachedIn(viewModelScope)

    fun addToCollection(collectionId: Long, artistId: String) {
        viewModelScope.launch {
            collectionDao.insertEntityIntoCollection(
                CollectionEntityRoomModel(collectionId, artistId)
            )
        }
    }

    suspend fun createNewCollection(name: String, entity: MusicBrainzResource) {
        collectionDao.insert(
            CollectionRoomModel(
                name = name,
                entity = entity
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CollectionBottomSheet(
    bottomSheetState: SheetState,
    scope: CoroutineScope,
    collections: LazyPagingItems<CollectionListItemModel>,
    onDismiss: () -> Unit,
    onCreateCollectionButtonClick: () -> Unit,
    onAddToCollection: suspend (collectionId: Long) -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = bottomSheetState,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = stringResource(id = R.string.add_to_collection),
                modifier = Modifier
                    .padding(horizontal = 16.dp),
                style = TextStyles.getCardBodyTextStyle()
            )

            Spacer(modifier = Modifier.weight(1f))

            IconButton(onClick = onCreateCollectionButtonClick) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(id = R.string.create_collection)
                )
            }
        }

        Divider(modifier = Modifier.padding(top = 16.dp))

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
        ) {
            items(collections) { collection ->
                when (collection) {
                    is CollectionListItemModel -> {
                        CollectionListItem(
                            collection = collection,
                            onClick = {
                                scope.launch {
                                    onAddToCollection(collection.id.toLong())
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun NavigationGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    viewModel: AppTopLevelViewModel = hiltViewModel()
) {
    val deeplinkSchema = stringResource(id = R.string.deeplink_schema)

    val scope = rememberCoroutineScope()
    var openBottomSheet by rememberSaveable { mutableStateOf(false) }
    val bottomSheetState = rememberSheetState()
    var showDialog by rememberSaveable { mutableStateOf(false) }
    var selectedEntityId by rememberSaveable { mutableStateOf("") }
    val collections: LazyPagingItems<CollectionListItemModel> = rememberFlowWithLifecycleStarted(viewModel.collections)
        .collectAsLazyPagingItems()

    if (showDialog) {
        CreateCollectionDialog(
            onDismiss = { showDialog = false },
            onSubmit = { name, entity ->
                scope.launch {
                    viewModel.createNewCollection(name, entity)
                }
            }
        )
    }

    if (openBottomSheet) {
        CollectionBottomSheet(
            bottomSheetState = bottomSheetState,
            scope = scope,
            collections = collections,
            onDismiss = { openBottomSheet = false },
            onCreateCollectionButtonClick = { showDialog = true },
            onAddToCollection = { collectionId ->
                if (selectedEntityId.isNotEmpty()) {
                    viewModel.addToCollection(collectionId, selectedEntityId)
                }
            }
        )
    }

    NavHost(
        navController = navController,
        startDestination = Destination.LOOKUP.route,
    ) {

        val onLookupEntityClick: (MusicBrainzResource, String, String?) -> Unit = { entity, id, title ->
            viewModel.resource.value = entity
            selectedEntityId = id
            navController.goToResource(entity, id, title)
        }

        val onCollectionClick: (String) -> Unit = { collectionId ->
            navController.navigate("${Destination.COLLECTIONS.route}/$collectionId")
        }

        val searchMusicBrainz: (String, MusicBrainzResource) -> Unit = { query, type ->
            val route = Destination.LOOKUP.route + "?query=${query}&type=${type.resourceName}"
            Timber.d("1. $query")
            navController.navigate(route)
        }

        composable(Destination.LOOKUP.route) {
            SearchScreenScaffold(
                modifier = modifier,
                onItemClick = onLookupEntityClick
            )
        }

        composable(
            route = "${Destination.LOOKUP.route}?query={query}&type={type}",
            arguments = listOf(
                navArgument("query") {
                    type = NavType.StringType
                },
                navArgument("type") {
                    type = NavType.StringType
                }
            ),
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "$deeplinkSchema://${Destination.LOOKUP.route}?query={query}&type={type}"
                }
            )
        ) { entry ->
            val query = entry.arguments?.getString("query")
            val type = entry.arguments?.getString("type")?.toMusicBrainzResource()

            Timber.d("2. $query")

            SearchScreenScaffold(
                modifier = modifier,
                onItemClick = onLookupEntityClick,
                searchQuery = query,
                searchOption = type
            )
        }

        addLookupResourceScreen(
            resource = MusicBrainzResource.AREA,
            deeplinkSchema = deeplinkSchema
        ) { resourceId, title ->
            AreaScaffold(
                areaId = resourceId,
                modifier = modifier,
                titleWithDisambiguation = title,
                onBack = navController::navigateUp,
                onItemClick = onLookupEntityClick,
                onAddToCollectionMenuClick = {

                    openBottomSheet = true
                }
            )
        }

        addLookupResourceScreen(
            resource = MusicBrainzResource.ARTIST,
            deeplinkSchema = deeplinkSchema
        ) { resourceId, title ->
            ArtistScaffold(
                artistId = resourceId,
                modifier = modifier,
                titleWithDisambiguation = title,
                onItemClick = onLookupEntityClick,
                onBack = navController::navigateUp,
                onAddToCollectionMenuClick = {
                    viewModel.resource.value = MusicBrainzResource.ARTIST
                    selectedEntityId = resourceId
                    openBottomSheet = true
                }
            )
        }

        addLookupResourceScreen(
            resource = MusicBrainzResource.EVENT,
            deeplinkSchema = deeplinkSchema
        ) { resourceId, title ->
            EventScaffold(
                eventId = resourceId,
                modifier = modifier,
                titleWithDisambiguation = title,
                onBack = navController::navigateUp,
                onItemClick = onLookupEntityClick
            )
        }

        addLookupResourceScreen(
            resource = MusicBrainzResource.GENRE,
            deeplinkSchema = deeplinkSchema
        ) { resourceId, title ->
            GenreScaffold(
                genreId = resourceId,
                modifier = modifier,
                titleWithDisambiguation = title,
                onBack = navController::navigateUp
            )
        }

        addLookupResourceScreen(
            resource = MusicBrainzResource.INSTRUMENT,
            deeplinkSchema = deeplinkSchema
        ) { resourceId, title ->
            InstrumentScaffold(
                instrumentId = resourceId,
                modifier = modifier,
                titleWithDisambiguation = title,
                onBack = navController::navigateUp,
                onItemClick = onLookupEntityClick
            )
        }

        addLookupResourceScreen(
            resource = MusicBrainzResource.LABEL,
            deeplinkSchema = deeplinkSchema
        ) { resourceId, title ->
            LabelScaffold(
                labelId = resourceId,
                modifier = modifier,
                titleWithDisambiguation = title,
                onBack = navController::navigateUp,
                onItemClick = onLookupEntityClick
            )
        }

        addLookupResourceScreen(
            resource = MusicBrainzResource.PLACE,
            deeplinkSchema = deeplinkSchema
        ) { resourceId, title ->
            PlaceScaffold(
                placeId = resourceId,
                modifier = modifier,
                titleWithDisambiguation = title,
                onBack = navController::navigateUp,
                onItemClick = onLookupEntityClick
            )
        }

        addLookupResourceScreen(
            resource = MusicBrainzResource.RECORDING,
            deeplinkSchema = deeplinkSchema
        ) { resourceId, title ->
            RecordingScaffold(
                recordingId = resourceId,
                modifier = modifier,
                titleWithDisambiguation = title,
                onBack = navController::navigateUp,
                onItemClick = onLookupEntityClick,
            )
        }

        addLookupResourceScreen(
            resource = MusicBrainzResource.RELEASE,
            deeplinkSchema = deeplinkSchema
        ) { resourceId, title ->
            ReleaseScaffold(
                releaseId = resourceId,
                modifier = modifier,
                titleWithDisambiguation = title,
                onBack = navController::navigateUp,
                onItemClick = onLookupEntityClick,
            )
        }
        addLookupResourceScreen(
            resource = MusicBrainzResource.RELEASE_GROUP,
            deeplinkSchema = deeplinkSchema
        ) { resourceId, title ->
            ReleaseGroupScaffold(
                releaseGroupId = resourceId,
                modifier = modifier,
                titleWithDisambiguation = title,
                onBack = navController::navigateUp,
                onItemClick = onLookupEntityClick
            )
        }

        addLookupResourceScreen(
            resource = MusicBrainzResource.SERIES,
            deeplinkSchema = deeplinkSchema
        ) { resourceId, title ->
            SeriesScaffold(
                seriesId = resourceId,
                modifier = modifier,
                titleWithDisambiguation = title,
                onBack = navController::navigateUp,
                onItemClick = onLookupEntityClick
            )
        }

        addLookupResourceScreen(
            resource = MusicBrainzResource.WORK,
            deeplinkSchema = deeplinkSchema
        ) { resourceId, title ->
            WorkScaffold(
                workId = resourceId,
                modifier = modifier,
                titleWithDisambiguation = title,
                onBack = navController::navigateUp,
                onItemClick = onLookupEntityClick
            )
        }

        composable(
            Destination.HISTORY.route
        ) {
            HistoryScaffold(
                modifier = modifier,
                onItemClick = onLookupEntityClick
            )
        }


        composable(
            Destination.COLLECTIONS.route
        ) {
            CollectionListScaffold(
                modifier = modifier,
                onCollectionClick = onCollectionClick
            )
        }

        composable(
            route = "${Destination.COLLECTIONS.route}/{$ID}",
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "$deeplinkSchema://${Destination.COLLECTIONS.route}/{$ID}"
                }
            )
        ) { entry ->
            val collectionId = entry.arguments?.getString(ID) ?: return@composable

            CollectionScaffold(
                collectionId = collectionId,
                modifier = modifier,
                onEntityClick = { entity, id ->
                    onLookupEntityClick(entity, id, null)
                }
            )
        }

        val onSettingsClick: (Destination) -> Unit = { destination ->
            when (destination) {
                Destination.EXPERIMENTAL_SETTINGS,
                Destination.EXPERIMENTAL_SPOTIFY -> {
                    navController.goTo(destination)
                }
                else -> {
                    // Nothing.
                }
            }
        }

        composable(
            Destination.SETTINGS.route
        ) {
            SettingsScaffold(
                modifier = modifier,
                onDestinationClick = { destination ->
                    onSettingsClick(destination)
                }
            )
        }

        composable(
            Destination.EXPERIMENTAL_SETTINGS.route
        ) {
            ExperimentalSettingsScaffold()
        }

        composable(
            Destination.EXPERIMENTAL_SPOTIFY.route
        ) {
            SpotifyScreen(
                searchMusicBrainz = searchMusicBrainz
            )
        }
    }
}

private fun NavGraphBuilder.addLookupResourceScreen(
    resource: MusicBrainzResource,
    deeplinkSchema: String,
    scaffold: @Composable (resourceId: String, titleWithDisambiguation: String?) -> Unit
) {
    composable(
        route = "${resource.toLookupDestination().route}/{$ID}?$TITLE={$TITLE}",
        arguments = listOf(
            navArgument(ID) {
                type = NavType.StringType // Make argument type safe
            },
            navArgument(TITLE) {
                nullable = true
                defaultValue = null
                type = NavType.StringType
            },
        ),
        deepLinks = listOf(
            navDeepLink {
                uriPattern = "$deeplinkSchema://${resource.resourceName}/{$ID}?$TITLE={$TITLE}"
            }
        )
    ) { entry: NavBackStackEntry ->
        val resourceId = entry.arguments?.getString(ID) ?: return@composable
        val title = entry.arguments?.getString(TITLE)
        scaffold(resourceId, title)
    }
}
