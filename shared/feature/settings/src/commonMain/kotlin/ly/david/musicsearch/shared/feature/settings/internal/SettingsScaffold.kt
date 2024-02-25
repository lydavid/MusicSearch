package ly.david.musicsearch.shared.feature.settings.internal

import MusicSearch.shared.feature.settings.BuildConfig
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ly.david.musicsearch.core.models.navigation.Destination
import ly.david.musicsearch.core.preferences.AppPreferences
import ly.david.musicsearch.shared.feature.settings.internal.components.ProfileCard
import ly.david.musicsearch.shared.feature.settings.internal.components.SettingSwitch
import ly.david.musicsearch.shared.feature.settings.internal.components.SettingWithDialogChoices
import ly.david.musicsearch.strings.AppStrings
import ly.david.musicsearch.strings.LocalStrings
import ly.david.ui.common.component.ClickableItem
import ly.david.ui.common.listitem.ListSeparatorHeader
import ly.david.ui.common.text.TextWithHeading
import ly.david.ui.common.topappbar.ScrollableTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun Settings(
    state: SettingsUiState,
    modifier: Modifier = Modifier,
    onDestinationClick: (Destination) -> Unit = {},
    onLoginClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {},
) {
    val strings = LocalStrings.current
    val eventSink = state.eventSink

    Scaffold(
        modifier = modifier,
        topBar = {
            ScrollableTopAppBar(
                showBackButton = false,
                title = strings.settings,
            )
        },
    ) { innerPadding ->

//        val context = LocalContext.current

        Settings(
            modifier = Modifier.padding(innerPadding),
            username = state.username,
            showLogin = state.accessToken.isNullOrEmpty(),
            onLoginClick = onLoginClick,
            onLogoutClick = onLogoutClick,
            onDestinationClick = onDestinationClick,
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
            //            isNotificationListenerEnabled = context.isNotificationListenerEnabled(),
            onGoToNotificationListenerSettings = {
                //                context.startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
            },
            versionName = BuildConfig.VERSION_NAME,
            versionCode = BuildConfig.VERSION_CODE.toIntOrNull() ?: 0,
            onLicenseClick = {
                eventSink(SettingsUiEvent.GoToLicensesScreen)
            }
        )
    }
}

// private fun Context.isNotificationListenerEnabled(): Boolean {
//    return NotificationManagerCompat.getEnabledListenerPackages(this).any { it == this.packageName }
// }

private fun AppPreferences.Theme.getText(strings: AppStrings): String =
    when (this) {
        AppPreferences.Theme.LIGHT -> strings.light
        AppPreferences.Theme.DARK -> strings.dark
        AppPreferences.Theme.SYSTEM -> strings.system
    }

@Composable
internal fun Settings(
    modifier: Modifier = Modifier,
    username: String = "",
    showLogin: Boolean = true,
    onLoginClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {},
    onDestinationClick: (Destination) -> Unit = {},
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
    onLicenseClick: () -> Unit = {},
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
                choices = AppPreferences.Theme.values().map { it.getText(strings) },
                selectedChoiceIndex = theme.ordinal,
                onSelectChoiceIndex = { onThemeChange(AppPreferences.Theme.values()[it]) },
            )

            val isAndroid12 = true // TODO: handle Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
            if (isAndroid12) {
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

            ListSeparatorHeader(text = strings.experimentalSearch)

            if (isNotificationListenerEnabled) {
                ClickableItem(
                    title = strings.nowPlayingHistory,
                    subtitle = strings.nowPlayingHistorySubtitle,
                    endIcon = Icons.Default.ChevronRight,
                    onClick = {
                        onDestinationClick(Destination.SETTINGS_NOWPLAYING)
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
                    onDestinationClick(Destination.EXPERIMENTAL_SPOTIFY)
                },
            )

            ListSeparatorHeader(text = strings.about)

            ClickableItem(
                title = strings.openSourceLicenses,
                endIcon = Icons.Default.ChevronRight,
                onClick = onLicenseClick,
            )

            val versionKey = strings.appVersion
            TextWithHeading(
                heading = versionKey,
                text = "$versionName ($versionCode)",
            )

            if (true) { // TODO: handle BuildConfig.DEBUG
                DevSettingsSection()
            }
        }
    }
}
