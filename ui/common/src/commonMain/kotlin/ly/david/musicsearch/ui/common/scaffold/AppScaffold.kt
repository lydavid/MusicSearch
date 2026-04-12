package ly.david.musicsearch.ui.common.scaffold

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ly.david.musicsearch.ui.common.snackbar.FeedbackSnackbarHost

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScaffold(
    scrollToHideTopAppBar: Boolean,
    topBar: @Composable (TopAppBarScrollBehavior) -> Unit,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    content: @Composable (PaddingValues, TopAppBarScrollBehavior) -> Unit,
) {
    val scrollBehavior = if (scrollToHideTopAppBar) {
        TopAppBarDefaults.enterAlwaysScrollBehavior()
    } else {
        TopAppBarDefaults.pinnedScrollBehavior()
    }

    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets(0),
        topBar = { topBar(scrollBehavior) },
        snackbarHost = {
            FeedbackSnackbarHost(snackbarHostState)
        },
    ) { innerPadding ->
        content(innerPadding, scrollBehavior)
    }
}
