package ly.david.ui.settings

import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.navigation.compose.hiltViewModel
import ly.david.data.domain.Destination
import ly.david.data.room.DATABASE_VERSION
import ly.david.ui.common.R
import ly.david.ui.common.topappbar.ScrollableTopAppBar

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
        val accessToken by viewModel.musicBrainzAuthState.accessToken.collectAsState(initial = null)
        val theme by viewModel.appPreferences.theme.collectAsState(initial = AppPreferences.Theme.SYSTEM)
        val useMaterialYou by viewModel.appPreferences.useMaterialYou.collectAsState(initial = true)

        SettingsScreen(
            modifier = Modifier.padding(innerPadding),
            username = username,
            showLogin = accessToken.isNullOrEmpty(),
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
            versionName = BuildConfig.VERSION_NAME,
            versionCode = BuildConfig.VERSION_CODE,
            databaseVersion = DATABASE_VERSION,
        )
    }
}

private fun Context.isNotificationListenerEnabled(): Boolean {
    return NotificationManagerCompat.getEnabledListenerPackages(this).any { it == this.packageName }
}
