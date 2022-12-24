package ly.david.mbjc.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import java.util.regex.Pattern
import ly.david.mbjc.BuildConfig
import ly.david.mbjc.ExcludeFromJacocoGeneratedReport
import ly.david.mbjc.R
import ly.david.mbjc.ui.common.TextWithHeading
import ly.david.mbjc.ui.common.preview.DefaultPreviews
import ly.david.mbjc.ui.common.topappbar.TopAppBarWithFilter
import ly.david.mbjc.ui.settings.dev.DevSettingsSection
import ly.david.mbjc.ui.theme.PreviewTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SettingsScaffold(
    openDrawer: () -> Unit = {},
) {

    var filterText by rememberSaveable { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBarWithFilter(
                openDrawer = openDrawer,
                title = stringResource(id = R.string.settings),
                filterText = filterText,
                onFilterTextChange = {
                    filterText = it
                },
            )
        },
    ) {
        SettingsScreen(filterText)
    }
}

@Composable
internal fun SettingsScreen(filterText: String) {

    // TODO: can we filter through viewmodel? pass filter data to viewmodel,
    //  then it decides which to show
    val filterRegex = Regex(".*(?i)(${Pattern.quote(filterText)}).*")

    Column {

        val versionKey = stringResource(id = R.string.app_version)
        val versionValue = "${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})"
        if (filterRegex.matches(versionKey) || filterRegex.matches(versionValue)) {
            TextWithHeading(heading = versionKey, text = versionValue)
        }

        // TODO: sharedpreference to use artist sort name throughout app
        //  helpful for foreign names
        //  other entities have a sort_name field in backend
        //  but doesn't seem to be exposed for editing/displaying

        if (BuildConfig.DEBUG) {
            DevSettingsSection()
        }
    }
}

// TODO: broken because of regex?
@ExcludeFromJacocoGeneratedReport
@DefaultPreviews
@Composable
private fun Preview() {
    PreviewTheme {
        Surface {
            SettingsScaffold()
        }
    }
}
