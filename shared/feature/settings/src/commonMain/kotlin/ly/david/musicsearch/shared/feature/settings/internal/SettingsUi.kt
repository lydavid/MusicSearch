package ly.david.musicsearch.shared.feature.settings.internal

import MusicSearch.shared.feature.settings.BuildConfig
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ly.david.musicsearch.shared.domain.listen.ListenBrainzLoginState
import ly.david.musicsearch.shared.feature.settings.internal.components.ListenBrainzProfileCard
import ly.david.musicsearch.shared.feature.settings.internal.components.MusicBrainzProfileCard
import ly.david.musicsearch.shared.feature.settings.internal.components.SettingSwitch
import ly.david.musicsearch.ui.common.clipboard.clipEntryWith
import ly.david.musicsearch.ui.common.component.ClickableItem
import ly.david.musicsearch.ui.common.icons.ChevronRight
import ly.david.musicsearch.ui.common.icons.CustomIcons
import ly.david.musicsearch.ui.common.icons.Download
import ly.david.musicsearch.ui.common.listitem.ListSeparatorHeader
import ly.david.musicsearch.ui.common.musicbrainz.MusicBrainzLoginUiEvent
import ly.david.musicsearch.ui.common.screen.AppearanceSettingsScreen
import ly.david.musicsearch.ui.common.screen.ImagesSettingsScreen
import ly.david.musicsearch.ui.common.screen.LicensesScreen
import ly.david.musicsearch.ui.common.screen.ListensScreen
import ly.david.musicsearch.ui.common.screen.NowPlayingHistoryScreen
import ly.david.musicsearch.ui.common.screen.SpotifyHistoryScreen
import ly.david.musicsearch.ui.common.text.TextWithIcon
import ly.david.musicsearch.ui.common.theme.LocalStrings
import ly.david.musicsearch.ui.common.topappbar.ScrollableTopAppBar

@Composable
internal expect fun SettingsUi(
    state: SettingsUiState,
    modifier: Modifier = Modifier,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SettingsUi(
    state: SettingsUiState,
    showAndroidSettings: Boolean,
    modifier: Modifier = Modifier,
    isNotificationListenerEnabled: Boolean = false,
    versionName: String = BuildConfig.VERSION_NAME,
    versionCode: Int = BuildConfig.VERSION_CODE.toIntOrNull() ?: 0,
    onGoToNotificationListenerSettings: () -> Unit = {},
) {
    val strings = LocalStrings.current
    val eventSink = state.eventSink
    val loginEventSink = state.musicBrainzLoginUiState.eventSink
    val snackbarHostState = remember { SnackbarHostState() }

    state.snackbarMessage?.let { message ->
        LaunchedEffect(message) {
            snackbarHostState.showSnackbar(message = message)
            eventSink(SettingsUiEvent.DismissSnackbar)
        }
    }
    state.listenBrainzLoginState?.let { state ->
        LaunchedEffect(state) {
            val message = when (state) {
                ListenBrainzLoginState.InvalidToken -> strings.invalidToken
                ListenBrainzLoginState.LoggedIn -> strings.youAreLoggedIn
                ListenBrainzLoginState.LoggedOut -> strings.youAreLoggedOut
                is ListenBrainzLoginState.OtherError -> state.message
            }
            snackbarHostState.showSnackbar(message = message)
            eventSink(SettingsUiEvent.DismissSnackbar)
        }
    }
    state.musicBrainzLoginUiState.errorMessage?.let { message ->
        LaunchedEffect(message) {
            snackbarHostState.showSnackbar(message = message)
            loginEventSink(MusicBrainzLoginUiEvent.DismissError)
        }
    }

    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets(0),
        topBar = {
            ScrollableTopAppBar(
                showBackButton = false,
                title = strings.settings,
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { snackbarData ->
                SwipeToDismissBox(
                    state = rememberSwipeToDismissBoxState(),
                    backgroundContent = {},
                    content = { Snackbar(snackbarData) },
                )
            }
        },
    ) { innerPadding ->
        SettingsUi(
            state = state,
            modifier = Modifier.padding(innerPadding),
            showAndroidSettings = showAndroidSettings,
            isNotificationListenerEnabled = isNotificationListenerEnabled,
            onGoToNotificationListenerSettings = onGoToNotificationListenerSettings,
            versionName = versionName,
            versionCode = versionCode,
            eventSink = eventSink,
            loginEventSink = loginEventSink,
        )
    }
}

private const val DEVELOPER_MODE_CLICK_COUNT_TO_ENABLE = 7

@Composable
internal fun SettingsUi(
    state: SettingsUiState,
    showAndroidSettings: Boolean,
    modifier: Modifier = Modifier,
    isNotificationListenerEnabled: Boolean = false,
    versionName: String = BuildConfig.VERSION_NAME,
    versionCode: Int = BuildConfig.VERSION_CODE.toIntOrNull() ?: 0,
    onGoToNotificationListenerSettings: () -> Unit = {},
    eventSink: (SettingsUiEvent) -> Unit = {},
    loginEventSink: (MusicBrainzLoginUiEvent) -> Unit = {},
) {
    val strings = LocalStrings.current
    val clipboard = LocalClipboard.current
    val coroutineScope = rememberCoroutineScope()
    val haptics = LocalHapticFeedback.current

    var developerModeClickCount by remember { mutableIntStateOf(0) }

    LazyColumn(modifier = modifier) {
        item {
            MusicBrainzProfileCard(
                username = state.musicBrainzUsername,
                showLogin = state.musicBrainzAccessToken.isNullOrEmpty(),
                onLoginClick = { loginEventSink(MusicBrainzLoginUiEvent.StartLogin) },
                onLogoutClick = { eventSink(SettingsUiEvent.MusicBrainzLogout) },
            )

            ListenBrainzProfileCard(
                modifier = Modifier.padding(top = 4.dp),
                listenBrainzUrl = state.listenBrainzUrl,
                username = state.listenBrainzUsername,
                showLogin = state.listenBrainzUserToken.isEmpty(),
                text = state.listenBrainzText,
                onTextChange = { eventSink(SettingsUiEvent.UpdateListenBrainzUserToken(it)) },
                onLoginClick = { eventSink(SettingsUiEvent.ListenBrainzLogin) },
                onLogoutClick = { eventSink(SettingsUiEvent.ListenBrainzLogout) },
            )

            ClickableItem(
                title = strings.appearance,
                endIcon = CustomIcons.ChevronRight,
                onClick = {
                    eventSink(SettingsUiEvent.GoToScreen(AppearanceSettingsScreen))
                },
            )

            ClickableItem(
                title = strings.images,
                endIcon = CustomIcons.ChevronRight,
                onClick = {
                    eventSink(SettingsUiEvent.GoToScreen(ImagesSettingsScreen))
                },
            )

            SettingSwitch(
                header = "Show more info in release list items",
                checked = state.showMoreInfoInReleaseListItem,
                onCheckedChange = { eventSink(SettingsUiEvent.UpdateShowMoreInfoInReleaseListItem(it)) },
            )

            if (state.showCrashReporterSettings) {
                SettingSwitch(
                    header = "Enable crash reporting",
                    subtitle = "App restart is required when switching off",
                    checked = state.isCrashReportingEnabled,
                    onCheckedChange = { eventSink(SettingsUiEvent.EnableCrashReporting(it)) },
                )
            }

            ListSeparatorHeader(text = strings.experimentalSearch)

            ClickableItem(
                title = strings.listens,
                endIcon = CustomIcons.ChevronRight,
                onClick = {
                    eventSink(SettingsUiEvent.GoToScreen(ListensScreen()))
                },
            )

            if (showAndroidSettings) {
                if (isNotificationListenerEnabled) {
                    ClickableItem(
                        title = strings.nowPlayingHistory,
                        subtitle = strings.nowPlayingHistorySubtitle,
                        endIcon = CustomIcons.ChevronRight,
                        onClick = {
                            eventSink(SettingsUiEvent.GoToScreen(NowPlayingHistoryScreen))
                        },
                    )
                } else {
                    ClickableItem(
                        title = strings.enableNotificationListener,
                        subtitle = strings.enableNotificationListenerSubtitle,
                        onClick = onGoToNotificationListenerSettings,
                    )
                }

                ClickableItem(
                    title = strings.spotify,
                    subtitle = strings.spotifySubtitle,
                    endIcon = CustomIcons.ChevronRight,
                    onClick = {
                        eventSink(SettingsUiEvent.GoToScreen(SpotifyHistoryScreen))
                    },
                )
            }

            ListSeparatorHeader(text = "Database")

            TextButton(onClick = { eventSink(SettingsUiEvent.ExportDatabase) }) {
                TextWithIcon(
                    imageVector = CustomIcons.Download,
                    text = "Save database to Downloads",
                )
            }

            ListSeparatorHeader(text = strings.about)

            ClickableItem(
                title = strings.openSourceLicenses,
                endIcon = CustomIcons.ChevronRight,
                onClick = {
                    eventSink(SettingsUiEvent.GoToScreen(LicensesScreen))
                },
            )

            val appVersionText = "${strings.appVersion}: $versionName ($versionCode)"
            val databaseVersionText = "${strings.databaseVersion}: ${state.appDatabaseVersion}"
            ClickableItem(
                title = appVersionText,
                subtitle = databaseVersionText,
                onClick = {
                    coroutineScope.launch {
                        haptics.performHapticFeedback(HapticFeedbackType.ContextClick)
                        clipboard.setClipEntry(
                            clipEntryWith(
                                "$appVersionText, $databaseVersionText",
                            ),
                        )
                        developerModeClickCount++
                        if (developerModeClickCount >= DEVELOPER_MODE_CLICK_COUNT_TO_ENABLE) {
                            eventSink(SettingsUiEvent.EnableDeveloperMode(true))
                        }
                    }
                },
            )

            if (state.isDeveloperMode) {
                DevSettingsSection(
                    isDeveloperMode = state.isDeveloperMode,
                    onDeveloperModeChange = { eventSink(SettingsUiEvent.EnableDeveloperMode(it)) },
                )
                ClickableItem(
                    title = "SQLite version: ${state.sqliteVersion}",
                )
            }

            Spacer(modifier = Modifier.padding(top = 16.dp))
        }
    }
}
