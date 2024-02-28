package ly.david.musicsearch.shared.feature.settings.internal

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import ly.david.musicsearch.strings.LocalStrings
import ly.david.ui.core.theme.TextStyles

@Composable
internal actual fun Settings(
    state: SettingsUiState,
    modifier: Modifier,
) {
    val eventSink = state.eventSink

    if (state.showDialog) {
        MyDialog(
            onDismiss = {
                eventSink(SettingsUiEvent.DismissDialog)
            },
            onSubmit = { code ->
                eventSink(SettingsUiEvent.SubmitAuthCode(code))
            },
        )
    }

    Settings(
        state = state,
        showAndroidSettings = false,
        modifier = modifier,
    )
}

@Composable
private fun MyDialog(
    onDismiss: () -> Unit,
    onSubmit: (String) -> Unit,
) {
    val strings = LocalStrings.current
    var authorizationCode by rememberSaveable { mutableStateOf("") }

    Dialog(
        onDismissRequest = {
            onDismiss()
        },
    ) {
        Surface(
            shape = RoundedCornerShape(28.dp),
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
            ) {
                Text(
                    modifier = Modifier,
                    text = "Enter authorization code from your browser:",
                    style = TextStyles.getHeaderTextStyle(),
                )

                TextField(
                    modifier = Modifier
                        .padding(top = 16.dp),
                    shape = RectangleShape,
                    value = authorizationCode,
                    label = { Text("Authorization Code") },
                    placeholder = { Text("Authorization Code") },
                    maxLines = 1,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
//                    trailingIcon = {
//                        if (name.isEmpty()) return@TextField
//                        IconButton(onClick = {
//                            name = ""
//                            focusRequester.requestFocus()
//                        }) {
//                            Icon(Icons.Default.Clear, contentDescription = strings.clearSearch)
//                        }
//                    },
                    onValueChange = { newText ->
                        if (!newText.contains("\n")) {
                            authorizationCode = newText
                        }
                    },
                )

                TextButton(
                    onClick = {
                        onSubmit(authorizationCode)
                        onDismiss()
                    },
                ) {
                    Text(
                        text = strings.ok,
                    )
                }
            }
        }
    }
}
