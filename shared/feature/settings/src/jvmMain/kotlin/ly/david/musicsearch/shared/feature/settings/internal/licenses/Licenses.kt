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
    // TODO: Currently we need to generate and store this json file in desktop:app's resources
    //  which means desktop:app also needs to use the aboutlibraries plugin
    //  ./gradlew desktop:app:exportLibraryDefinitions -PaboutLibraries.exportPath=src/main/resources/
    LibrariesContainer(
        aboutLibsJson = useResource("aboutlibraries.json") {
            it.bufferedReader().readText()
        },
        modifier = modifier.fillMaxSize(),
    )
}
