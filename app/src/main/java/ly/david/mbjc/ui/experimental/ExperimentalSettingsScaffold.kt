package ly.david.mbjc.ui.experimental

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import ly.david.mbjc.BuildConfig
import ly.david.mbjc.ExcludeFromJacocoGeneratedReport
import ly.david.ui.common.text.TextWithHeading
import ly.david.mbjc.ui.common.topappbar.TopAppBarWithFilter
import ly.david.mbjc.ui.settings.AppPreferences
import ly.david.mbjc.ui.settings.DevSettingsSection
import ly.david.mbjc.ui.settings.components.SettingWithDialogChoices
import ly.david.ui.common.R
import ly.david.ui.common.preview.DefaultPreviews
import ly.david.ui.common.theme.PreviewTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExperimentalSettingsScaffold(
    viewModel: ExperimentalSettingsViewModel = hiltViewModel()
) {

    var filterText by rememberSaveable { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBarWithFilter(
                title = stringResource(id = R.string.settings),
                filterText = filterText,
                onFilterTextChange = {
                    filterText = it
                    viewModel.setFilter(filterText)
                },
            )
        },
    ) { innerPadding ->

        val showTheme by viewModel.showTheme.collectAsState(initial = true)
        val theme by viewModel.appPreferences.theme.collectAsState(initial = AppPreferences.Theme.SYSTEM)
        val showAppVersion by viewModel.showAppVersion.collectAsState(initial = true)

        SettingsScreen(
            showTheme = showTheme,
            theme = theme,
            modifier = Modifier.padding(innerPadding),
            showAppVersion = showAppVersion,
            onThemeChange = { viewModel.appPreferences.setTheme(it) },
        )
    }
}

@Composable
fun SettingsScreen(
    showTheme: Boolean = true,
    theme: AppPreferences.Theme,
    modifier: Modifier = Modifier,
    showAppVersion: Boolean = true,
    onThemeChange: (AppPreferences.Theme) -> Unit = {}
) {

    Column(modifier = modifier) {

        if (showTheme) {
            SettingWithDialogChoices(
                titleRes = R.string.theme,
                choices = AppPreferences.Theme.values().map { stringResource(id = it.textRes) },
                selectedChoiceIndex = theme.ordinal,
                onSelectChoiceIndex = { onThemeChange(AppPreferences.Theme.values()[it]) },
            )
        }

        val versionKey = stringResource(id = R.string.app_version)
        val versionValue = BuildConfig.VERSION_NAME
        if (showAppVersion) {
            TextWithHeading(heading = versionKey, text = versionValue)
        }

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
