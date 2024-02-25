package ly.david.musicsearch.shared.feature.settings.internal.licenses

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.useResource
import com.mikepenz.aboutlibraries.ui.compose.LibrariesContainer

@Composable
internal actual fun Licenses(
    modifier: Modifier,
) {
    LibrariesContainer(
        aboutLibsJson = useResource("aboutlibraries.json") {
            it.bufferedReader().readText()
        },
        modifier = modifier.fillMaxSize(),
    )
}
