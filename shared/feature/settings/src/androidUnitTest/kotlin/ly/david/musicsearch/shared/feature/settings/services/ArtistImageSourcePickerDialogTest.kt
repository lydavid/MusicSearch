package ly.david.musicsearch.shared.feature.settings.services

import ly.david.musicsearch.shared.feature.settings.internal.services.PreviewArtistImageSourcePickerDialogCustomSpotify
import ly.david.musicsearch.shared.feature.settings.internal.services.PreviewArtistImageSourcePickerDialogDefaultSpotify
import ly.david.musicsearch.shared.feature.settings.internal.services.PreviewArtistImageSourcePickerDialogWikimedia
import ly.david.musicsearch.ui.test.screenshot.ScreenshotTest
import org.junit.Test

class ArtistImageSourcePickerDialogTest : ScreenshotTest(isFullScreen = true) {

    @Test
    fun wikimedia() {
        snapshot {
            PreviewArtistImageSourcePickerDialogWikimedia()
        }
    }

    @Test
    fun defaultSpotify() {
        snapshot {
            PreviewArtistImageSourcePickerDialogDefaultSpotify()
        }
    }

    @Test
    fun customSpotify() {
        snapshot {
            PreviewArtistImageSourcePickerDialogCustomSpotify()
        }
    }
}
