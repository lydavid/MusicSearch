package ly.david.musicsearch.shared.feature.listens

import ly.david.musicsearch.ui.test.screenshot.ScreenshotTest
import org.junit.Test

class LinkWithMusicBrainzDialogContentScreenshotTest : ScreenshotTest() {

    @Test
    fun manual() {
        snapshot {
            PreviewLinkWithMusicBrainzDialogContentManual()
        }
    }

    @Test
    fun loading() {
        snapshot {
            PreviewLinkWithMusicBrainzDialogContentLoading()
        }
    }

    @Test
    fun noUnmapped() {
        snapshot {
            PreviewLinkWithMusicBrainzDialogContentNoUnmappedIsrc()
        }
    }

    @Test
    fun noMatch() {
        snapshot {
            PreviewLinkWithMusicBrainzDialogContentNoMatchingRecordings()
        }
    }

    @Test
    fun success() {
        snapshot {
            PreviewLinkWithMusicBrainzDialogContentSuccess()
        }
    }

    @Test
    fun successMultiple() {
        snapshot {
            PreviewLinkWithMusicBrainzDialogContentSuccessMultiple()
        }
    }
}
