package ly.david.musicsearch.shared.feature.licenses

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import ly.david.musicsearch.strings.LocalStrings
import ly.david.ui.common.topappbar.ScrollableTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun LicensesScaffold(
    state: LicensesUiState,
    modifier: Modifier = Modifier,
//    onBack: () -> Unit = {},
) {
    val strings = LocalStrings.current
    val eventSink = state.eventSink

    Scaffold(
        modifier = modifier,
        topBar = {
            ScrollableTopAppBar(
                onBack = { eventSink(LicensesUiEvent.NavigateUp) },
                title = strings.openSourceLicenses,
            )
        },
    ) { innerPadding ->
        Licenses(modifier = modifier.padding(innerPadding))
    }
}

internal class LicensesPresenter(
    private val navigator: Navigator,
) : Presenter<LicensesUiState> {
    @Composable
    override fun present(): LicensesUiState {

        fun eventSink(event: LicensesUiEvent) {
            when (event) {
                LicensesUiEvent.NavigateUp -> navigator.pop()
            }
        }

        return LicensesUiState(
            eventSink = ::eventSink
        )
    }
}
