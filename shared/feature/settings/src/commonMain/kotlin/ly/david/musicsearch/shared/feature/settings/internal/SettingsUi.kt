package ly.david.musicsearch.shared.feature.settings.internal

import MusicSearch.shared.feature.settings.BuildConfig
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import com.slack.circuit.runtime.screen.Screen
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch
import ly.david.musicsearch.shared.domain.preferences.AppPreferences
import ly.david.musicsearch.shared.feature.settings.internal.components.ProfileCard
import ly.david.musicsearch.shared.feature.settings.internal.components.SettingSwitch
import ly.david.musicsearch.shared.feature.settings.internal.components.SettingWithDialogChoices
import ly.david.musicsearch.shared.strings.AppStrings
import ly.david.musicsearch.ui.common.clipboard.clipEntryWith
import ly.david.musicsearch.ui.common.component.ClickableItem
import ly.david.musicsearch.ui.common.icons.ChevronRight
import ly.david.musicsearch.ui.common.icons.CustomIcons
import ly.david.musicsearch.ui.common.icons.Download
import ly.david.musicsearch.ui.common.listitem.ListSeparatorHeader
import ly.david.musicsearch.ui.common.musicbrainz.LoginUiEvent
import ly.david.musicsearch.ui.common.screen.LicensesScreen
import ly.david.musicsearch.ui.common.screen.NowPlayingHistoryScreen
import ly.david.musicsearch.ui.common.screen.SpotifyHistoryScreen
import ly.david.musicsearch.ui.common.topappbar.ScrollableTopAppBar
import ly.david.musicsearch.ui.core.LocalStrings
import ly.david.musicsearch.ui.core.theme.TextStyles

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
    isAndroid12: Boolean = false,
    isNotificationListenerEnabled: Boolean = false,
    onGoToNotificationListenerSettings: () -> Unit = {},
) {
    val strings = LocalStrings.current
    val eventSink = state.eventSink
    val loginEventSink = state.loginState.eventSink
    val snackbarHostState = remember { SnackbarHostState() }

    state.snackbarMessage?.let { message ->
        LaunchedEffect(message) {
            snackbarHostState.showSnackbar(message = message)
        }
    }
    state.loginState.errorMessage?.let { message ->
        LaunchedEffect(message) {
            snackbarHostState.showSnackbar(message = message)
            loginEventSink(LoginUiEvent.DismissError)
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
            modifier = Modifier.padding(innerPadding),
            username = state.username,
            showLogin = state.accessToken.isNullOrEmpty(),
            onLoginClick = {
                loginEventSink(LoginUiEvent.StartLogin)
            },
            onLogoutClick = {
                eventSink(SettingsUiEvent.Logout)
            },
            onDestinationClick = {
                eventSink(SettingsUiEvent.GoToScreen(it))
            },
            showAndroidSettings = showAndroidSettings,
            isAndroid12 = isAndroid12,
            theme = state.theme,
            onThemeChange = {
                eventSink(SettingsUiEvent.UpdateTheme(it))
            },
            useMaterialYou = state.useMaterialYou,
            onUseMaterialYouChange = {
                eventSink(SettingsUiEvent.UpdateUseMaterialYou(it))
            },
            showMoreInfoInReleaseListItem = state.showMoreInfoInReleaseListItem,
            onShowMoreInfoInReleaseListItemChange = {
                eventSink(SettingsUiEvent.UpdateShowMoreInfoInReleaseListItem(it))
            },
            sortReleaseGroupListItems = state.sortReleaseGroupListItems,
            onSortReleaseGroupListItemsChange = {
                eventSink(SettingsUiEvent.UpdateSortReleaseGroupListItems(it))
            },
            showCrashReporterSettings = state.showCrashReporterSettings,
            isCrashReportingEnabled = state.isCrashReportingEnabled,
            onCrashReportingEnabledChange = {
                eventSink(SettingsUiEvent.EnableCrashReporting(it))
            },
            isNotificationListenerEnabled = isNotificationListenerEnabled,
            onGoToNotificationListenerSettings = onGoToNotificationListenerSettings,
            versionName = BuildConfig.VERSION_NAME,
            versionCode = BuildConfig.VERSION_CODE.toIntOrNull() ?: 0,
            databaseVersion = state.databaseVersion,
            export = {
                eventSink(SettingsUiEvent.ExportDatabase)
            },
        )
    }
}

@Composable
internal fun SettingsUi(
    modifier: Modifier = Modifier,
    username: String = "",
    showLogin: Boolean = true,
    onLoginClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {},
    onDestinationClick: (Screen) -> Unit = {},
    showAndroidSettings: Boolean = true,
    isAndroid12: Boolean = false,
    theme: AppPreferences.Theme = AppPreferences.Theme.SYSTEM,
    onThemeChange: (AppPreferences.Theme) -> Unit = {},
    useMaterialYou: Boolean = true,
    onUseMaterialYouChange: (Boolean) -> Unit = {},
    showMoreInfoInReleaseListItem: Boolean = true,
    onShowMoreInfoInReleaseListItemChange: (Boolean) -> Unit = {},
    sortReleaseGroupListItems: Boolean = false,
    onSortReleaseGroupListItemsChange: (Boolean) -> Unit = {},
    isNotificationListenerEnabled: Boolean = false,
    onGoToNotificationListenerSettings: () -> Unit = {},
    showCrashReporterSettings: Boolean = false,
    isCrashReportingEnabled: Boolean = false,
    onCrashReportingEnabledChange: (Boolean) -> Unit = {},
    export: () -> Unit = {},
    versionName: String = "",
    versionCode: Int = 0,
    databaseVersion: String = "",
) {
    val strings = LocalStrings.current
    val clipboard = LocalClipboard.current
    val coroutineScope = rememberCoroutineScope()
    val haptics = LocalHapticFeedback.current

    LazyColumn(modifier = modifier) {
        item {
            ProfileCard(
                username = username,
                showLogin = showLogin,
                onLoginClick = onLoginClick,
                onLogoutClick = onLogoutClick,
            )

            SettingWithDialogChoices(
                title = strings.theme,
                choices = AppPreferences.Theme.entries.map { it.getText(strings) }.toImmutableList(),
                selectedChoiceIndex = theme.ordinal,
                onSelectChoiceIndex = { onThemeChange(AppPreferences.Theme.entries[it]) },
            )

            if (showAndroidSettings && isAndroid12) {
                SettingSwitch(
                    header = "Use Material You",
                    checked = useMaterialYou,
                    onCheckedChange = onUseMaterialYouChange,
                )
            }

            SettingSwitch(
                header = "Show more info in release list items",
                checked = showMoreInfoInReleaseListItem,
                onCheckedChange = onShowMoreInfoInReleaseListItemChange,
            )

            SettingSwitch(
                header = "Sort release groups by type",
                checked = sortReleaseGroupListItems,
                onCheckedChange = onSortReleaseGroupListItemsChange,
            )

            if (showCrashReporterSettings) {
                SettingSwitch(
                    header = "Enable crash reporting",
                    subtitle = "App restart is required when switching off",
                    checked = isCrashReportingEnabled,
                    onCheckedChange = onCrashReportingEnabledChange,
                )
            }

            ListSeparatorHeader(text = strings.experimentalSearch)
            if (showAndroidSettings) {
                if (isNotificationListenerEnabled) {
                    ClickableItem(
                        title = strings.nowPlayingHistory,
                        subtitle = strings.nowPlayingHistorySubtitle,
                        endIcon = CustomIcons.ChevronRight,
                        onClick = {
                            onDestinationClick(NowPlayingHistoryScreen)
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
                        onDestinationClick(SpotifyHistoryScreen)
                    },
                )
            }

            ListSeparatorHeader(text = "Database")

            TextButton(onClick = export) {
                Icon(imageVector = CustomIcons.Download, contentDescription = null)
                Text(
                    text = "Save database to Downloads",
                    modifier = Modifier.padding(start = 8.dp),
                    style = TextStyles.getCardBodyTextStyle(),
                )
            }

            ListSeparatorHeader(text = strings.about)

            ClickableItem(
                title = strings.openSourceLicenses,
                endIcon = CustomIcons.ChevronRight,
                onClick = {
                    onDestinationClick(LicensesScreen)
                },
            )

            val appVersionText = "${strings.appVersion}: $versionName ($versionCode)"
            val databaseVersionText = "${strings.databaseVersion}: $databaseVersion"
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
                    }
                },
            )

            if (false) { // TODO: handle BuildConfig.DEBUG
                DevSettingsSection()
            }

            Spacer(modifier = Modifier.padding(top = 16.dp))
        }
    }
}

private fun AppPreferences.Theme.getText(strings: AppStrings): String =
    when (this) {
        AppPreferences.Theme.LIGHT -> strings.light
        AppPreferences.Theme.DARK -> strings.dark
        AppPreferences.Theme.SYSTEM -> strings.system
    }
