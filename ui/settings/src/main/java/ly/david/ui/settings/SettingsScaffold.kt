package ly.david.ui.settings

import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.navigation.compose.hiltViewModel
import ly.david.data.domain.Destination
import ly.david.ui.common.R
import ly.david.ui.common.listitem.ListSeparatorHeader
import ly.david.ui.common.text.TextWithHeading
import ly.david.ui.common.topappbar.ScrollableTopAppBar
import ly.david.ui.core.preview.DefaultPreviews
import ly.david.ui.core.theme.PreviewTheme
import ly.david.ui.settings.components.ClickableItem
import ly.david.ui.settings.components.ProfileCard
import ly.david.ui.settings.components.SettingSwitch
import ly.david.ui.settings.components.SettingWithDialogChoices

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScaffold(
    modifier: Modifier = Modifier,
    onDestinationClick: (Destination) -> Unit = {},
    onLoginClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {},
    showMoreInfoInReleaseListItem: Boolean = true,
    onShowMoreInfoInReleaseListItemChange: (Boolean) -> Unit = {},
    sortReleaseGroupListItems: Boolean = false,
    onSortReleaseGroupListItemsChange: (Boolean) -> Unit = {},
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            ScrollableTopAppBar(
                showBackButton = false,
                title = stringResource(id = R.string.settings),
            )
        },
    ) { innerPadding ->

        val context = LocalContext.current

        val username by viewModel.musicBrainzAuthState.username.collectAsState(initial = "")
        val authState by viewModel.musicBrainzAuthState.authStateFlow.collectAsState(initial = null)
        val theme by viewModel.appPreferences.theme.collectAsState(initial = AppPreferences.Theme.SYSTEM)
        val useMaterialYou by viewModel.appPreferences.useMaterialYou.collectAsState(initial = true)

        SettingsScreen(
            modifier = Modifier.padding(innerPadding),
            username = username,
            showLogin = authState == null,
            onLoginClick = onLoginClick,
            onLogoutClick = onLogoutClick,
            onDestinationClick = onDestinationClick,
            theme = theme,
            onThemeChange = { viewModel.appPreferences.setTheme(it) },
            useMaterialYou = useMaterialYou,
            onUseMaterialYouChange = { viewModel.appPreferences.setUseMaterialYou(it) },
            showMoreInfoInReleaseListItem = showMoreInfoInReleaseListItem,
            onShowMoreInfoInReleaseListItemChange = onShowMoreInfoInReleaseListItemChange,
            sortReleaseGroupListItems = sortReleaseGroupListItems,
            onSortReleaseGroupListItemsChange = onSortReleaseGroupListItemsChange,
            isNotificationListenerEnabled = context.isNotificationListenerEnabled(),
            onGoToNotificationListenerSettings = {
                context.startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
            },
        )
    }
}

@Composable
internal fun SettingsScreen(
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
) {
    LazyColumn(modifier = modifier) {
        item {
            ProfileCard(
                username = username,
                showLogin = showLogin,
                onLoginClick = onLoginClick,
                onLogoutClick = onLogoutClick
            )

            SettingWithDialogChoices(
                titleRes = R.string.theme,
                choices = AppPreferences.Theme.values().map { stringResource(id = it.textRes) },
                selectedChoiceIndex = theme.ordinal,
                onSelectChoiceIndex = { onThemeChange(AppPreferences.Theme.values()[it]) },
            )

            val isAndroid12 = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
            if (isAndroid12) {
                SettingSwitch(
                    header = "Use Material You",
                    checked = useMaterialYou,
                    onCheckedChange = onUseMaterialYouChange
                )
            }

            SettingSwitch(
                header = "Show more info in release list items",
                checked = showMoreInfoInReleaseListItem,
                onCheckedChange = onShowMoreInfoInReleaseListItemChange
            )

            SettingSwitch(
                header = "Sort release groups by type",
                checked = sortReleaseGroupListItems,
                onCheckedChange = onSortReleaseGroupListItemsChange
            )

            if (isNotificationListenerEnabled) {
                ClickableItem(
                    title = stringResource(id = R.string.now_playing_history),
                    subtitle = stringResource(id = R.string.now_playing_history_subtitle),
                    endIcon = Icons.Default.ChevronRight,
                    onClick = {
                        onDestinationClick(Destination.SETTINGS_NOWPLAYING)
                    },
                )
            } else {
                ClickableItem(
                    title = stringResource(id = R.string.enable_notification_listener),
                    subtitle = stringResource(id = R.string.enable_notification_listener_subtitle),
                    onClick = onGoToNotificationListenerSettings,
                )
            }

            ListSeparatorHeader(text = stringResource(id = R.string.about))

            ClickableItem(
                title = stringResource(id = R.string.open_source_licenses),
                endIcon = Icons.Default.ChevronRight,
                onClick = {
                    onDestinationClick(Destination.SETTINGS_LICENSES)
                },
            )

            val versionKey = stringResource(id = R.string.app_version)
            val versionName = BuildConfig.VERSION_NAME
            val versionCode = BuildConfig.VERSION_CODE.toString()
            TextWithHeading(heading = versionKey, text = "$versionName ($versionCode)")

            if (BuildConfig.DEBUG) {
                DevSettingsSection(
                    onDestinationClick = onDestinationClick
                )
            }
        }
    }
}

private fun Context.isNotificationListenerEnabled(): Boolean {
    return NotificationManagerCompat.getEnabledListenerPackages(this).any { it == this.packageName }
}

// region Previews
@DefaultPreviews
@Composable
internal fun PreviewSettingsScreen() {
    PreviewTheme {
        Surface {
            SettingsScreen()
        }
    }
}

@DefaultPreviews
@Composable
internal fun PreviewSettingsScreenNotificationListenerEnable() {
    PreviewTheme {
        Surface {
            SettingsScreen(
                isNotificationListenerEnabled = true,
            )
        }
    }
}
// endregion
