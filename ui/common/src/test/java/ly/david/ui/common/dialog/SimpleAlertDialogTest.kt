package ly.david.ui.common.dialog

import androidx.compose.ui.res.stringResource
import com.google.testing.junit.testparameterinjector.TestParameterInjector
import ly.david.ui.common.PaparazziScreenshotTest
import ly.david.ui.common.R
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(TestParameterInjector::class)
class SimpleAlertDialogTest : PaparazziScreenshotTest(isFullScreen = true) {

    @Test
    fun long() {
        snapshot {
            SimpleAlertDialog(
                title = stringResource(id = R.string.delete_search_history_confirmation),
                confirmText = stringResource(id = R.string.yes),
                dismissText = stringResource(id = R.string.no)
            )
        }
    }
}
