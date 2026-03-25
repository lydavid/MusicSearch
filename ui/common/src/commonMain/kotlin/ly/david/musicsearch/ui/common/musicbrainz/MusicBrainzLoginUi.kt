package ly.david.musicsearch.ui.common.musicbrainz

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.AnnotatedString
import ly.david.musicsearch.ui.common.dialog.DialogWithCloseButton
import ly.david.musicsearch.ui.common.text.TextInput
import musicsearch.ui.common.generated.resources.Res
import musicsearch.ui.common.generated.resources.authorizationCode
import musicsearch.ui.common.generated.resources.enterAuthorizationCode
import musicsearch.ui.common.generated.resources.login
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
    var authorizationCode by rememberSaveable { mutableStateOf("") }

    DialogWithCloseButton(
        onDismiss = onDismiss,
    ) {
        TextInput(
            instructions = AnnotatedString(stringResource(Res.string.enterAuthorizationCode)),
            textLabel = stringResource(Res.string.authorizationCode),
            textHint = stringResource(Res.string.authorizationCode),
            text = authorizationCode,
            buttonText = stringResource(Res.string.login),
            onTextChange = { newText ->
                authorizationCode = newText
            },
            onButtonClick = {
                onSubmit(authorizationCode)
                onDismiss()
            },
        )
    }
}
