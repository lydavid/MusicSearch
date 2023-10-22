package ly.david.musicsearch.shared

import ly.david.musicsearch.domain.url.usecase.OpenInBrowser
import java.awt.Desktop
import java.net.URI

actual class OpenInBrowserImpl : OpenInBrowser {
    override fun invoke(url: String) {
        Desktop.getDesktop().browse(URI.create(url))
    }
}
