package ly.david.musicsearch.shared.feature.details.area.places


//@Composable
//internal fun PlacesByAreaScreen(
//    areaId: String,
//    filterText: String,
//    snackbarHostState: SnackbarHostState,
//    placesLazyListState: LazyListState,
//    placesLazyPagingItems: LazyPagingItems<PlaceListItemModel>,
//    modifier: Modifier = Modifier,
//    onPlaceClick: (entity: MusicBrainzEntity, String, String) -> Unit = { _, _, _ -> },
//    onPagedPlacesFlowChange: (Flow<PagingData<PlaceListItemModel>>) -> Unit = {},
////    viewModel: PlacesByAreaViewModel = koinViewModel(),
//) {
//    LaunchedEffect(key1 = areaId) {
//        viewModel.loadPagedEntities(areaId)
//        onPagedPlacesFlowChange(viewModel.pagedEntities)
//    }
//
//    viewModel.updateQuery(filterText)
//
//    PlacesListScreen(
//        snackbarHostState = snackbarHostState,
//        lazyListState = placesLazyListState,
//        lazyPagingItems = placesLazyPagingItems,
//        modifier = modifier,
//        onPlaceClick = onPlaceClick,
//    )
//}
