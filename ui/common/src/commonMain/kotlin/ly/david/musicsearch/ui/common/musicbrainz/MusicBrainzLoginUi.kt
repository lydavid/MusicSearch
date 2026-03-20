package ly.david.musicsearch.ui.common.musicbrainz

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import ly.david.musicsearch.ui.common.icons.Clear
import ly.david.musicsearch.ui.common.icons.CustomIcons
import ly.david.musicsearch.ui.common.theme.TextStyles
import musicsearch.ui.common.generated.resources.Res
import musicsearch.ui.common.generated.resources.ok
import org.jetbrains.compose.resources.stringResource

@Composable
fun MusicBrainzLoginUi(
    state: MusicBrainzLoginUiState,
    onSuccessfulLogin: () -> Unit = {},
    onError: suspend (String) -> Unit = {},
) {
    val loginEventSink = state.eventSink

    if (state.showDialog) {
        AuthorizationDialog(
            onDismiss = {
                loginEventSink(MusicBrainzLoginUiEvent.DismissDialog)
            },
            onSubmit = { code ->
                loginEventSink(MusicBrainzLoginUiEvent.SubmitAuthCode(code))
            },
        )
    }

    state.successfulLoginAt?.let { successfulLoginAt ->
        LaunchedEffect(successfulLoginAt) {
            onSuccessfulLogin()
        }
    }

    state.errorMessage?.let { message ->
        LaunchedEffect(message) {
            onError(message)
            loginEventSink(MusicBrainzLoginUiEvent.DismissError)
        }
    }
}

@Composable
private fun AuthorizationDialog(
    onDismiss: () -> Unit,
    onSubmit: (String) -> Unit,
) {
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
                                CustomIcons.Clear,
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
                        text = stringResource(Res.string.ok),
                    )
                }
            }
        }
    }
}
