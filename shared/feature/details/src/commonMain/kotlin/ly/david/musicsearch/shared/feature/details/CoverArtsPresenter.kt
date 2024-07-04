package ly.david.musicsearch.shared.feature.details

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import ly.david.musicsearch.core.models.image.ImageUrls
import ly.david.musicsearch.shared.domain.release.ReleaseImageRepository
import ly.david.musicsearch.ui.common.screen.CoverArtsScreen
import ly.david.musicsearch.ui.common.topappbar.ScrollableTopAppBar
import ly.david.musicsearch.ui.image.LargeImage

@OptIn(
    ExperimentalFoundationApi::class,
    ExperimentalMaterial3Api::class,
)
@Composable
internal fun CoverArtsUi(
    state: CoverArtsUiState,
    modifier: Modifier = Modifier,
) {
    val eventSink = state.eventSink
    val pagerState = rememberPagerState(pageCount = { state.imageUrls.size })
//    val scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets(0),
        topBar = {
            ScrollableTopAppBar(
                showBackButton = true,
                onBack = { eventSink(CoverArtsUiEvent.NavigateUp) },
                title = " ",
//                scrollBehavior = scrollBehavior,
            )
        },
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize(),
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
//                    .verticalScroll(rememberScrollState())
//                    .nestedScroll(scrollBehavior.nestedScrollConnection)
            ) { page ->
                val url = state.imageUrls[page]
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                ) {
                    LargeImage(
                        url = url.largeUrl,
                        id = url.largeUrl,
                        modifier = Modifier.padding(innerPadding),
                    )
                }
            }
            Row(
                Modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = "${pagerState.currentPage + 1} / ${pagerState.pageCount}",
                )
            }
        }
    }
}

internal class CoverArtsPresenter(
    private val screen: CoverArtsScreen,
    private val navigator: Navigator,
    private val releaseImageRepository: ReleaseImageRepository,
) : Presenter<CoverArtsUiState> {

    @Composable
    override fun present(): CoverArtsUiState {
        var imageUrls by rememberSaveable { mutableStateOf<List<ImageUrls>>(listOf()) }

        LaunchedEffect(Unit) {
            imageUrls = releaseImageRepository.getAllUrls(screen.id)
        }

        fun eventSink(event: CoverArtsUiEvent) {
            when (event) {
                CoverArtsUiEvent.NavigateUp -> {
                    navigator.pop()
                }
            }
        }

        return CoverArtsUiState(
            id = screen.id,
            imageUrls = imageUrls,
            eventSink = ::eventSink,
        )
    }
}

@Stable
internal data class CoverArtsUiState(
    val id: String,
    val imageUrls: List<ImageUrls>,
    val eventSink: (CoverArtsUiEvent) -> Unit = {},
) : CircuitUiState

internal sealed interface CoverArtsUiEvent : CircuitUiEvent {
    data object NavigateUp : CoverArtsUiEvent
}
