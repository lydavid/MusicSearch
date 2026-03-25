package ly.david.musicsearch.ui.common.dialog

import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog

@Composable
fun BasicDialog(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier.Companion,
    content: @Composable () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismiss,
    ) {
        Surface(
            modifier = modifier,
            shape = AlertDialogDefaults.shape,
            color = AlertDialogDefaults.containerColor,
            tonalElevation = AlertDialogDefaults.TonalElevation,
        ) {
            content()
        }
    }
}
