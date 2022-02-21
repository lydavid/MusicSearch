package ly.david.musicbrainzjetpackcompose.ui.artist

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ly.david.musicbrainzjetpackcompose.data.Artist
import ly.david.musicbrainzjetpackcompose.ui.common.ScrollableTopAppBar

// TODO:
@Composable
fun ArtistScreenScaffold(
    artist: Artist,
    onReleaseGroupClick: (String) -> Unit = {},
    onBack: () -> Unit
) {

    // TODO: setting to show more options. By default, we can keep it as just title/year,
    //  but we should be able to see all the relevant info from this screen too like we do from web
    //  each row will probably just be label: data

    Scaffold(
        topBar = { ScrollableTopAppBar(title = artist.name, onBack = onBack) },
//        bottomBar = {
        // TODO: meant for main navigation in app, so this nested screen shouldn't use it
        //  instead, it should use tabs, which don't belong in topbar
        //  however, it won't fit too many
        // https://developer.android.com/reference/kotlin/androidx/compose/material/package-summary#Tab(kotlin.Boolean,kotlin.Function0,androidx.compose.ui.Modifier,kotlin.Boolean,kotlin.Function0,kotlin.Function0,androidx.compose.foundation.interaction.MutableInteractionSource,androidx.compose.ui.graphics.Color,androidx.compose.ui.graphics.Color)
        // https://developer.android.com/reference/kotlin/androidx/compose/material/package-summary#ScrollableTabRow(kotlin.Int,androidx.compose.ui.Modifier,androidx.compose.ui.graphics.Color,androidx.compose.ui.graphics.Color,androidx.compose.ui.unit.Dp,kotlin.Function1,kotlin.Function0,kotlin.Function0)
        // TODO: apparently, we can get scrolling, so maybe we can fit everything
        // See Reddit is Fun app: check out the behaviour to hide top bar and tabs on scroll
        // https://material.io/components/tabs#usage
//            BottomNavigation(
//                backgroundColor = Color.White
//            ) {
        // TODO:  should have tabs for Overview (release groups),  releases, recordings, ...
//            }
//        }
    ) { innerPadding ->
        ReleaseGroupsByArtistScreen(
            modifier = Modifier.padding(innerPadding),
            artistId = artist.id,
            onReleaseGroupClick = onReleaseGroupClick
        )
    }
}
