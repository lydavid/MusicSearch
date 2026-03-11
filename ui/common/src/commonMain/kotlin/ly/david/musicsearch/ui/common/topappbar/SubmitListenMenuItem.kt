package ly.david.musicsearch.ui.common.topappbar

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.slack.circuit.overlay.OverlayHost
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ly.david.musicsearch.shared.domain.error.Feedback
import ly.david.musicsearch.shared.domain.listen.SubmitListenFeedback
import ly.david.musicsearch.shared.domain.listen.SubmitListenType
import ly.david.musicsearch.ui.common.icons.CustomIcons
import ly.david.musicsearch.ui.common.icons.HeadphonesAdd
import ly.david.musicsearch.ui.common.screen.SnackbarPopResultV2
import ly.david.musicsearch.ui.common.screen.SubmitListenScreen
import ly.david.musicsearch.ui.common.screen.showInDialogForResult
import ly.david.musicsearch.ui.common.snackbar.FeedbackSnackbarVisuals
import musicsearch.ui.common.generated.resources.Res
import musicsearch.ui.common.generated.resources.failedToSubmitListen
import musicsearch.ui.common.generated.resources.needToLoginToListenBrainzToDo
import musicsearch.ui.common.generated.resources.submitListen
import musicsearch.ui.common.generated.resources.submittedListen
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource

@Composable
fun OverflowMenuScope.SubmitListenMenuItem(
    submitListenType: SubmitListenType,
    overlayHost: OverlayHost,
    coroutineScope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    onSuccess: () -> Unit = {},
) {
    DropdownMenuItem(
        text = {
            Text(text = stringResource(Res.string.submitListen))
        },
        leadingIcon = {
            Icon(
                imageVector = CustomIcons.HeadphonesAdd,
                contentDescription = null,
            )
        },
        onClick = {
            coroutineScope.launch {
                val result: SnackbarPopResultV2<Feedback<SubmitListenFeedback>> = overlayHost.showInDialogForResult(
                    screen = SubmitListenScreen(
                        submitListenType = submitListenType,
                    ),
                )
                result.feedback?.let { feedback ->
                    val message = when (val data = feedback.data) {
                        SubmitListenFeedback.FailToSubmitListens -> getString(Res.string.failedToSubmitListen)
                        SubmitListenFeedback.NeedToLogin -> getString(Res.string.needToLoginToListenBrainzToDo)
                        is SubmitListenFeedback.NetworkException -> data.message
                        SubmitListenFeedback.SubmittedListens -> getString(Res.string.submittedListen)
                    }
                    snackbarHostState.showSnackbar(
                        visuals = FeedbackSnackbarVisuals(
                            message = message,
                            actionLabel = (feedback as? Feedback.Error)?.action?.name,
                            duration = when (feedback) {
                                is Feedback.Loading -> SnackbarDuration.Indefinite
                                is Feedback.Success,
                                is Feedback.Error,
                                is Feedback.Actionable,
                                -> SnackbarDuration.Short
                            },
                            withDismissAction = false,
                            feedback = feedback,
                        ),
                    )
                }
                if (result.feedback is Feedback.Success) {
                    onSuccess()
                }
            }
            closeMenu()
        },
        modifier = modifier,
    )
}
