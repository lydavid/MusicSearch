package ly.david.mbjc

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import ly.david.data.coverart.CoverArtDataModule
import ly.david.data.di.auth.AuthStoreModule
import ly.david.data.di.coroutines.coroutinesScopesModule
import ly.david.data.di.logging.loggingModule
import ly.david.data.di.musicbrainz.musicBrainzAuthModule
import ly.david.data.di.room.databaseDaoModule
import ly.david.data.domain.DomainDataModule
import ly.david.data.musicbrainz.auth.MusicBrainzDataModule
import ly.david.data.spotify.di.SpotifyDataModule
import ly.david.mbjc.di.appDataModule
import ly.david.mbjc.di.testCoroutineDispatchersModule
import ly.david.mbjc.di.testDatabaseModule
import ly.david.mbjc.di.testImageModule
import ly.david.mbjc.di.testNetworkModule
import ly.david.mbjc.di.testPreferencesDataStoreModule
import ly.david.mbjc.ui.MainActivity
import ly.david.ui.collections.CollectionUiModule
import ly.david.ui.common.CommonUiModule
import ly.david.ui.history.HistoryUiModule
import ly.david.ui.nowplaying.NowPlayingUiModule
import ly.david.ui.settings.SettingsUiModule
import org.junit.Rule
import org.koin.dsl.module
import org.koin.ksp.generated.module

val testAndroidAppModule = module {
    includes(
        ViewModelsModule().module,
        appDataModule,
//        testDispatcherModule,
        testCoroutineDispatchersModule,
        coroutinesScopesModule,
//        testCoroutinesScopesModule,
        loggingModule,
        musicBrainzAuthModule,
        testNetworkModule,
        testPreferencesDataStoreModule,
        testDatabaseModule,
        databaseDaoModule,
        testImageModule,
        CoverArtDataModule().module,
        DomainDataModule().module,
        MusicBrainzDataModule().module,
        SpotifyDataModule().module,
        AuthStoreModule().module,
        CollectionUiModule().module,
        CommonUiModule().module,
        HistoryUiModule().module,
        NowPlayingUiModule().module,
        SettingsUiModule().module,
    )
}

internal abstract class MainActivityTest {
//    @get:Rule(order = 0)
//    val koinTestRule = KoinTestRule(
//        modules = listOf(testAndroidAppModule)
//    )

    @get:Rule(order = 1)
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    // val composeTestRule = createComposeRule() if we don't need activity
    //  great for testing individual UI pieces
    @get:Rule(order = 2)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @OptIn(ExperimentalTestApi::class)
    fun waitForNodeToShow(matcher: SemanticsMatcher) {
        composeTestRule.waitUntilAtLeastOneExists(matcher, 10_000L)
    }

    private fun waitForTextToShow(text: String) {
        waitForNodeToShow(hasText(text))
    }

    fun waitForThenPerformClickOn(text: String) {
        waitForTextToShow(text)

        composeTestRule
            .onNodeWithText(text)
            .performClick()
    }

    fun waitForThenAssertIsDisplayed(text: String) {
        waitForTextToShow(text)

        composeTestRule
            .onNodeWithText(text)
            .assertIsDisplayed()
    }

    fun waitForThenAssertAtLeastOneIsDisplayed(text: String) {
        waitForTextToShow(text)

        composeTestRule
            .onAllNodesWithText(text)
            .onFirst()
            .assertIsDisplayed()
    }

    fun waitForThenAssertIsDisplayed(matcher: SemanticsMatcher) {
        waitForNodeToShow(matcher)

        composeTestRule
            .onNode(matcher)
            .assertIsDisplayed()
    }

    fun SemanticsNodeInteraction.assertIsNotDisplayedOrDoesNotExist() {
        try {
            assertIsNotDisplayed()
        } catch (e: AssertionError) {
            assertDoesNotExist()
        }
    }
}
