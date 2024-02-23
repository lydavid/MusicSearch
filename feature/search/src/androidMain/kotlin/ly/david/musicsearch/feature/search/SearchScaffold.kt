package ly.david.musicsearch.feature.search

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//actual fun SearchScaffold(
//    modifier: Modifier,
//    onItemClick: (entity: MusicBrainzEntity, id: String, title: String?) -> Unit,
//    initialQuery: String?,
//    initialEntity: MusicBrainzEntity?,
//) {
//    val strings = LocalStrings.current
//    val snackbarHostState = remember { SnackbarHostState() }
//
//    Scaffold(
//        modifier = modifier,
//        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
//        topBar = {
//            ScrollableTopAppBar(
//                showBackButton = false,
//                title = strings.searchMusicbrainz,
//            )
//        },
//    ) { innerPadding ->
//        SearchScreen(
//            modifier = Modifier.padding(innerPadding),
//            snackbarHostState = snackbarHostState,
//            onItemClick = onItemClick,
//            initialQuery = initialQuery,
//            initialEntity = initialEntity,
//        )
//    }
//}
