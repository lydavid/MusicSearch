package ly.david.musicsearch.shared.feature.settings.internal

import MusicSearch.shared.feature.settings.BuildConfig
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.slack.circuit.runtime.screen.Screen
import kotlinx.collections.immutable.toImmutableList
import ly.david.musicsearch.core.preferences.AppPreferences
import ly.david.musicsearch.shared.feature.settings.internal.components.ProfileCard
import ly.david.musicsearch.shared.feature.settings.internal.components.SettingSwitch
import ly.david.musicsearch.shared.feature.settings.internal.components.SettingWithDialogChoices
import ly.david.musicsearch.ui.common.screen.LicensesScreen
import ly.david.musicsearch.ui.common.screen.NowPlayingHistoryScreen
import ly.david.musicsearch.ui.common.screen.SpotifyHistoryScreen
import ly.david.musicsearch.shared.strings.AppStrings
import ly.david.musicsearch.ui.core.LocalStrings
import ly.david.musicsearch.ui.common.component.ClickableItem
import ly.david.musicsearch.ui.common.listitem.ListSeparatorHeader
import ly.david.musicsearch.ui.common.musicbrainz.LoginUiEvent
import ly.david.musicsearch.ui.common.text.TextWithHeading
import ly.david.musicsearch.ui.common.topappbar.ScrollableTopAppBar

@Composable
internal expect fun Settings(
    state: SettingsUiState,
    modifier: Modifier = Modifier,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun Settings(
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

    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets(0),
        topBar = {
            ScrollableTopAppBar(
                showBackButton = false,
                title = strings.settings,
            )
        },
    ) { innerPadding ->
        Settings(
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
            isNotificationListenerEnabled = isNotificationListenerEnabled,
            onGoToNotificationListenerSettings = onGoToNotificationListenerSettings,
            versionName = BuildConfig.VERSION_NAME,
            versionCode = BuildConfig.VERSION_CODE.toIntOrNull() ?: 0,
        )
    }
}

@Composable
internal fun Settings(
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
    versionName: String = "",
    versionCode: Int = 0,
) {
    val strings = LocalStrings.current

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

            if (showAndroidSettings) {
                ListSeparatorHeader(text = strings.experimentalSearch)

                if (isNotificationListenerEnabled) {
                    ClickableItem(
                        title = strings.nowPlayingHistory,
                        subtitle = strings.nowPlayingHistorySubtitle,
                        endIcon = Icons.Default.ChevronRight,
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
                    endIcon = Icons.Default.ChevronRight,
                    onClick = {
                        onDestinationClick(SpotifyHistoryScreen)
                    },
                )
            }

            ListSeparatorHeader(text = strings.about)

            ClickableItem(
                title = strings.openSourceLicenses,
                endIcon = Icons.Default.ChevronRight,
                onClick = {
                    onDestinationClick(LicensesScreen)
                },
            )

            val versionKey = strings.appVersion
            TextWithHeading(
                heading = versionKey,
                text = "$versionName ($versionCode)",
            )

            if (false) { // TODO: handle BuildConfig.DEBUG
                DevSettingsSection()
            }
        }
    }
}

private fun AppPreferences.Theme.getText(strings: AppStrings): String =
    when (this) {
        AppPreferences.Theme.LIGHT -> strings.light
        AppPreferences.Theme.DARK -> strings.dark
        AppPreferences.Theme.SYSTEM -> strings.system
    }
