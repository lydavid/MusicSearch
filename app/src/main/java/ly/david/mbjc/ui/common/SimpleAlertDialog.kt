package ly.david.mbjc.ui.common

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import ly.david.mbjc.ui.theme.MusicBrainzJetpackComposeTheme

@Composable
fun SimpleAlertDialog(
    title: String,
    confirmText: String,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        title = { Text(title) },
        onDismissRequest = {
            onDismiss()
        },
        confirmButton = {
            TextButton(onClick = { onDismiss() }) {
                Text(confirmText)
            }
        }
    )
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SimpleAlertDialogPreview() {
    MusicBrainzJetpackComposeTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            var showAlertDialog by rememberSaveable { mutableStateOf(false) }

            Button(onClick = { showAlertDialog = true }) {
                Text(text = "Click me")
            }
            if (showAlertDialog) {
                SimpleAlertDialog(title = "Title", confirmText = "OK") {
                    showAlertDialog = false
                }
            }
        }
    }
}
