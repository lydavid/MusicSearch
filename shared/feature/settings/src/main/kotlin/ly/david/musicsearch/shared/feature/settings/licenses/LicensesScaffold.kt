package ly.david.musicsearch.shared.feature.settings.licenses

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mikepenz.aboutlibraries.ui.compose.LibrariesContainer
import com.mikepenz.aboutlibraries.ui.compose.LibraryDefaults
import ly.david.musicsearch.strings.LocalStrings
import ly.david.ui.common.topappbar.ScrollableTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LicensesScaffold(
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
) {
    val strings = LocalStrings.current

    Scaffold(
        modifier = modifier,
        topBar = {
            ScrollableTopAppBar(
                onBack = onBack,
                title = strings.openSourceLicenses,
            )
        },
    ) { innerPadding ->

        LibrariesContainer(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            colors = LibraryDefaults.libraryColors(
                backgroundColor = MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.onBackground,
                badgeBackgroundColor = MaterialTheme.colorScheme.primary,
                badgeContentColor = MaterialTheme.colorScheme.onPrimary,
            ),
        )
    }
}
