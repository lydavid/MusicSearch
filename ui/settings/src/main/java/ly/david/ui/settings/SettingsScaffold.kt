package ly.david.ui.settings

import android.os.Build
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import ly.david.data.domain.Destination
import ly.david.ui.common.R
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
    viewModel: SettingsViewModel = hiltViewModel()
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
            onSortReleaseGroupListItemsChange = onSortReleaseGroupListItemsChange
        )
    }
}

@Composable
fun SettingsScreen(
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
    onSortReleaseGroupListItemsChange: (Boolean) -> Unit = {}
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

            ClickableItem(
                text = stringResource(id = R.string.open_source_licenses),
                onClick = {
                    onDestinationClick(Destination.SETTINGS_LICENSES)
                }
            )

            val versionKey = stringResource(id = R.string.app_version)
            val versionName = BuildConfig.VERSION_NAME
            val versionCode = BuildConfig.VERSION_CODE.toString()
            TextWithHeading(heading = versionKey, text = "$versionName ($versionCode)")

            // TODO: sharedpreference to use artist sort name throughout app
            //  helpful for non-Latin names
            //  other entities have a sort_name field in backend
            //  but doesn't seem to be exposed for editing/displaying

            if (BuildConfig.DEBUG) {
                DevSettingsSection(
                    onDestinationClick = onDestinationClick
                )
            }
        }
    }
}

@DefaultPreviews
@Composable
private fun Preview() {
    PreviewTheme {
        Surface {
            SettingsScreen()
        }
    }
}
