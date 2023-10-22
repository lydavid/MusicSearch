package ly.david.ui.common.url

import androidx.lifecycle.ViewModel
import ly.david.musicsearch.domain.url.usecase.OpenInBrowser
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class UrlsSectionViewModel(
    val openInBrowser: OpenInBrowser,
) : ViewModel()
