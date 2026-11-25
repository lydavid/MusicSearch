package ly.david.musicsearch.shared.feature.settings.internal.behaviour

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import ly.david.musicsearch.shared.feature.settings.internal.components.SettingSwitch
import ly.david.musicsearch.ui.common.scaffold.AppScaffold
import ly.david.musicsearch.ui.common.topappbar.ScrollableTopAppBar
import musicsearch.ui.common.generated.resources.Res
import musicsearch.ui.common.generated.resources.behaviour
import musicsearch.ui.common.generated.resources.confirmChanges
import musicsearch.ui.common.generated.resources.pullToRefresh
import musicsearch.ui.common.generated.resources.scrollToHideTopAppBar
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun BehaviourSettingsUi(
    state: BehaviourSettingsUiState,
    modifier: Modifier = Modifier,
) {
    val eventSink = state.eventSink

    AppScaffold(
        modifier = modifier,
        scrollToHideTopAppBar = state.scrollToHideTopAppBar,
        snackbarHostState = SnackbarHostState(),
        topBar = { scrollBehavior ->
            ScrollableTopAppBar(
                showBackButton = true,
                title = stringResource(Res.string.behaviour),
                scrollBehavior = scrollBehavior,
                onBack = {
                    eventSink(BehaviourSettingsUiEvent.NavigateUp)
                },
            )
        },
    ) { innerPadding, scrollBehavior ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .verticalScroll(rememberScrollState()),
        ) {
            SettingSwitch(
                header = stringResource(Res.string.scrollToHideTopAppBar),
                checked = state.scrollToHideTopAppBar,
                onCheckedChange = {
                    eventSink(BehaviourSettingsUiEvent.UpdateScrollToHideTopAppBar(it))
                },
            )

            SettingSwitch(
                header = stringResource(Res.string.pullToRefresh),
                checked = state.isPullToRefreshEnabled,
                onCheckedChange = {
                    eventSink(BehaviourSettingsUiEvent.UpdatePullToRefresh(it))
                },
            )

            SettingSwitch(
                header = stringResource(Res.string.confirmChanges),
//                subtitle = stringResource(Res.string.boldUnvisitedDescription),
                checked = state.confirmChanges,
                onCheckedChange = {
                    eventSink(BehaviourSettingsUiEvent.UpdateConfirmChanges(it))
                },
            )
        }
    }
}
