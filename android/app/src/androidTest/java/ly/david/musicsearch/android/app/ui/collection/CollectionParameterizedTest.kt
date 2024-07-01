package ly.david.musicsearch.android.app.ui.collection

/**
 * Tests interacting with [CollectionList] and [CollectionScaffold].
 */
// @RunWith(Parameterized::class)
// internal class CollectionParameterizedTest(
//    private val entity: MusicBrainzEntity,
// ) : MainActivityTest() {
//
//    private val strings: AppStrings by inject()
//
//    companion object {
//        @JvmStatic
//        @Parameterized.Parameters(name = "{0}")
//        fun data(): Collection<MusicBrainzEntity> {
//            return collectableEntities
//        }
//    }
//
//    private val collectionDao: CollectionDao by inject()
//    private val imageLoaderFactory: ImageLoaderFactory by inject()
//    private val circuit: Circuit by inject()
//    private lateinit var navController: NavHostController
//
//    @Before
//    fun setupApp() {
//        Coil.setImageLoader(imageLoaderFactory)
//        composeTestRule.activity.setContent {
//            navController = rememberNavController()
//            PreviewTheme {
//                CircuitCompositionLocals(circuit) {
//                    TopLevelScaffold(navController)
//                }
//            }
//        }
//    }
//
//    @Test
//    fun onlyLocalCollections() = runTest(timeout = 15.seconds) {
//        composeTestRule
//            .onNodeWithText(strings.collections)
//            .performClick()
//
//        val collectionName = "local $entity collection"
//
//        collectionDao.insertLocal(
//            CollectionListItemModel(
//                id = entity.name,
//                name = collectionName,
//                entity = entity,
//                isRemote = false,
//                entityCount = 0,
//            )
//        )
//
//        composeTestRule
//            .onNodeWithText(collectionName) // list item
//            .performClick()
//
//        composeTestRule
//            .onNodeWithText(collectionName) // title
//            .assertIsDisplayed()
//
//        val entityId = entity.toFakeMusicBrainzModel().id
//        navController.goToEntityScreen(entity = entity, id = entityId)
//
//        composeTestRule
//            .onNodeWithContentDescription(strings.moreActions)
//            .performClick()
//        composeTestRule
//            .onNodeWithText(strings.addToCollection)
//            .performClick()
//        composeTestRule
//            .onNodeWithText(collectionName)
//            .performClick()
//
//        composeTestRule
//            .onNodeWithText(strings.collections)
//            .performClick()
//        composeTestRule
//            .onNodeWithText(collectionName)
//            .performClick()
//
//        val entityName = entity.toFakeMusicBrainzModel().name!!
//        composeTestRule
//            .onNodeWithText(entityName) // list item
//            .assertIsDisplayed()
//        composeTestRule
//            .onNodeWithContentDescription(strings.filter)
//            .performClick()
//        composeTestRule
//            .onNodeWithTag(TopAppBarWithFilterTestTag.FILTER_TEXT_FIELD.name)
//            .performTextInput("something")
//        composeTestRule
//            .onNodeWithText(entityName)
//            .assertDoesNotExist()
//        composeTestRule
//            .onNodeWithContentDescription(strings.clearFilter)
//            .performClick()
//        composeTestRule
//            .onNodeWithText(entityName)
//            .performClick()
//
//        composeTestRule
//            .onNodeWithText(entityName) // title
//            .assertIsDisplayed()
//
//        composeTestRule
//            .onNodeWithContentDescription(strings.back)
//            .performClick()
//        composeTestRule
//            .onNodeWithText(entityName)
//            .performTouchInput { swipeRight() }
//        composeTestRule
//            .onNodeWithText(entityName)
//            .assertDoesNotExist()
//    }
//
//    // We're currently able to skirt around the need to fake auth state but inserting data ourselves
//    // since the app saves remote collection's data
//    @Test
//    fun onlyRemoteCollections() = runTest {
//        composeTestRule
//            .onNodeWithText(strings.collections)
//            .performClick()
//
//        val name = "remote $entity collection"
//
//        collectionDao.insertRemote(
//            CollectionMusicBrainzModel(
//                id = entity.name,
//                name = name,
//                entity = entity,
//            )
//        )
//
//        // Name is in list item
//        waitForThenAssertIsDisplayed(name)
//        waitForThenPerformClickOn(name)
//
//        // Name is in title bar
//        waitForThenAssertIsDisplayed(name)
//    }
// }
