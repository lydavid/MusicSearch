package ly.david.mbjc.ui.settings

import android.os.Build
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import ly.david.data.navigation.Destination
import ly.david.mbjc.BuildConfig
import ly.david.mbjc.ExcludeFromJacocoGeneratedReport
import ly.david.mbjc.R
import ly.david.mbjc.ui.common.TextWithHeading
import ly.david.mbjc.ui.common.preview.DefaultPreviews
import ly.david.mbjc.ui.common.topappbar.ScrollableTopAppBar
import ly.david.mbjc.ui.settings.components.ProfileCard
import ly.david.mbjc.ui.settings.components.SettingSwitch
import ly.david.mbjc.ui.settings.components.SettingWithDialogChoices
import ly.david.mbjc.ui.theme.PreviewTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScaffold(
    modifier: Modifier = Modifier,
    onDestinationClick: (Destination) -> Unit = {},
    onLoginClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {},
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
        val showMoreInfoInReleaseListItem
            by viewModel.appPreferences.showMoreInfoInReleaseListItem.collectAsState(initial = true)
        val sortReleaseGroupListItems
            by viewModel.appPreferences.sortReleaseGroupListItems.collectAsState(initial = false)

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
            onShowMoreInfoInReleaseListItemChange = { viewModel.appPreferences.setShowMoreInfoInReleaseListItem(it) },
            sortReleaseGroupListItems = sortReleaseGroupListItems,
            onSortReleaseGroupListItemsChange = { viewModel.appPreferences.setSortReleaseGroupListItems(it) }
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

    Column(modifier = modifier) {

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

        val versionKey = stringResource(id = R.string.app_version)
        val versionValue = BuildConfig.VERSION_NAME
        TextWithHeading(heading = versionKey, text = versionValue)

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

@ExcludeFromJacocoGeneratedReport
@DefaultPreviews
@Composable
private fun Preview() {
    PreviewTheme {
        Surface {
            SettingsScreen()
        }
    }
}
