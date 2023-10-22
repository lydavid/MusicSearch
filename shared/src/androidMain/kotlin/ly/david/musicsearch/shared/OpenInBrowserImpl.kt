package ly.david.musicsearch.shared

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import ly.david.musicsearch.domain.url.usecase.OpenInBrowser

actual class OpenInBrowserImpl(
    private val context: Context,
) : OpenInBrowser {
    override fun invoke(url: String) {
        context.startActivity(
            Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(url)
                flags = FLAG_ACTIVITY_NEW_TASK
            },
        )
    }
}
