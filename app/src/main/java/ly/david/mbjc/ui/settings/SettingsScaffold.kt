package ly.david.mbjc.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import ly.david.mbjc.BuildConfig
import ly.david.mbjc.ExcludeFromJacocoGeneratedReport
import ly.david.mbjc.R
import ly.david.mbjc.ui.common.TextWithHeading
import ly.david.mbjc.ui.common.preview.DefaultPreviews
import ly.david.mbjc.ui.common.topappbar.ScrollableTopAppBar
import ly.david.mbjc.ui.settings.components.SettingWithDialogChoices
import ly.david.mbjc.ui.theme.PreviewTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScaffold(
    openDrawer: () -> Unit = {},
    viewModel: SettingsViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = {
            ScrollableTopAppBar(
                openDrawer = openDrawer,
                title = stringResource(id = R.string.settings),
            )
        },
    ) {

        val theme by viewModel.appPreferences.themeFlow.collectAsState(initial = AppPreferences.Theme.SYSTEM)

        SettingsScreen(
            theme = theme,
            onThemeChange = { viewModel.appPreferences.setTheme(it) },
        )
    }
}

@Composable
fun SettingsScreen(
    theme: AppPreferences.Theme,
    onThemeChange: (AppPreferences.Theme) -> Unit = {}
) {

    Column {

        SettingWithDialogChoices(
            titleRes = R.string.theme,
            choices = AppPreferences.Theme.values().map { stringResource(id = it.textRes) },
            selectedChoiceIndex = theme.ordinal,
            onSelectChoiceIndex = { onThemeChange(AppPreferences.Theme.values()[it]) },
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
                theme = AppPreferences.Theme.SYSTEM
            )
        }
    }
}
