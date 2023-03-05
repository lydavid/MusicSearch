package ly.david.mbjc.ui.settings

import android.os.Build
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import ly.david.data.navigation.Destination
import ly.david.mbjc.BuildConfig
import ly.david.mbjc.ExcludeFromJacocoGeneratedReport
import ly.david.mbjc.R
import ly.david.mbjc.ui.common.TextWithHeading
import ly.david.mbjc.ui.common.preview.DefaultPreviews
import ly.david.mbjc.ui.common.topappbar.ScrollableTopAppBar
import ly.david.mbjc.ui.settings.components.SettingSwitch
import ly.david.mbjc.ui.settings.components.SettingWithDialogChoices
import ly.david.mbjc.ui.theme.PreviewTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScaffold(
    modifier: Modifier = Modifier,
    onDestinationClick: (Destination) -> Unit = {},
    viewModel: SettingsViewModel = hiltViewModel()
) {

    val context = LocalContext.current

    Scaffold(
        modifier = modifier,
        topBar = {
            ScrollableTopAppBar(
                showBackButton = false,
                title = stringResource(id = R.string.settings),
            )
        },
    ) { innerPadding ->

        val theme by viewModel.appPreferences.theme.collectAsState(initial = AppPreferences.Theme.SYSTEM)
        val useMaterialYou by viewModel.appPreferences.useMaterialYou.collectAsState(initial = true)
        val showMoreInfoInReleaseListItem by viewModel.appPreferences.showMoreInfoInReleaseListItem.collectAsState(
            initial = true
        )

        SettingsScreen(
            modifier = Modifier.padding(innerPadding),
            onLoginClick = { viewModel.login(context) },
            onDestinationClick = onDestinationClick,
            theme = theme,
            onThemeChange = { viewModel.appPreferences.setTheme(it) },
            useMaterialYou = useMaterialYou,
            onUseMaterialYouChange = { viewModel.appPreferences.setUseMaterialYou(it) },
            showMoreInfoInReleaseListItem = showMoreInfoInReleaseListItem,
            onShowMoreInfoInReleaseListItemChange = { viewModel.appPreferences.setShowMoreInfoInReleaseListItem(it) }
        )
    }
}

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    onLoginClick: () -> Unit = {},
    onDestinationClick: (Destination) -> Unit = {},
    theme: AppPreferences.Theme,
    onThemeChange: (AppPreferences.Theme) -> Unit = {},
    useMaterialYou: Boolean,
    onUseMaterialYouChange: (Boolean) -> Unit = {},
    showMoreInfoInReleaseListItem: Boolean,
    onShowMoreInfoInReleaseListItemChange: (Boolean) -> Unit = {}
) {

    Column(modifier = modifier) {

        Text(
            text = "login",
            modifier = Modifier.clickable {
                onLoginClick()
            }
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
            SettingsScreen(
                theme = AppPreferences.Theme.SYSTEM,
                useMaterialYou = true,
                showMoreInfoInReleaseListItem = true
            )
        }
    }
}
