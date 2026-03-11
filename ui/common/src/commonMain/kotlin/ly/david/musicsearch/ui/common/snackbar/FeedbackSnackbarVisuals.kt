package ly.david.musicsearch.ui.common.snackbar

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarVisuals
import ly.david.musicsearch.shared.domain.error.Feedback
import ly.david.musicsearch.shared.domain.parcelize.CommonParcelable

data class FeedbackSnackbarVisuals<T : CommonParcelable>(
    override val actionLabel: String?,
    override val duration: SnackbarDuration,
    override val message: String,
    override val withDismissAction: Boolean,
    val feedback: Feedback<T>,
) : SnackbarVisuals
