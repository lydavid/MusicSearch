package ly.david.musicsearch.shared.feature.settings.internal

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import ly.david.musicsearch.ui.core.LocalStrings
import ly.david.musicsearch.ui.core.theme.TextStyles

@Composable
internal actual fun Settings(
    state: SettingsUiState,
    modifier: Modifier,
) {
    val loginEventSink = state.loginState.eventSink

    if (state.loginState.showDialog) {
        AuthorizationDialog(
            onDismiss = {
                loginEventSink(LoginUiEvent.DismissDialog)
            },
            onSubmit = { code ->
                loginEventSink(LoginUiEvent.SubmitAuthCode(code))
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
private fun AuthorizationDialog(
    onDismiss: () -> Unit,
    onSubmit: (String) -> Unit,
) {
    val strings = LocalStrings.current
    val focusRequester = remember { FocusRequester() }
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
                        .padding(top = 16.dp)
                        .focusRequester(focusRequester),
                    shape = RectangleShape,
                    value = authorizationCode,
                    label = { Text("Authorization Code") },
                    placeholder = { Text("Authorization Code") },
                    maxLines = 1,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    trailingIcon = {
                        if (authorizationCode.isEmpty()) return@TextField
                        IconButton(onClick = {
                            authorizationCode = ""
                            focusRequester.requestFocus()
                        }) {
                            Icon(
                                Icons.Default.Clear,
                                contentDescription = null,
                            )
                        }
                    },
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
