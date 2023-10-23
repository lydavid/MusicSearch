
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
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
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.github.scribejava.core.model.OAuth2AccessToken
import com.github.scribejava.core.model.OAuthAsyncRequestCallback
import com.github.scribejava.core.oauth.OAuth20Service
import ly.david.musicsearch.domain.url.usecase.OpenInBrowser
import ly.david.musicsearch.shared.di.sharedModule
import ly.david.musicsearch.strings.LocalStrings
import ly.david.ui.core.theme.BaseTheme
import ly.david.ui.core.theme.TextStyles
import org.koin.core.context.startKoin

fun main() = application {
    val windowState = rememberWindowState()

    val koin = startKoin {
        modules(
            sharedModule,
        )
    }.koin

    val service = koin.get<OAuth20Service>()
    val openInBrowser = koin.get<OpenInBrowser>()

    Window(
        onCloseRequest = ::exitApplication,
        state = windowState,
        title = "MusicSearch",
    ) {
        BaseTheme(
            content = {
                MusicSearchApp(
                    service,
                    openInBrowser = {
                        openInBrowser(it)
                    },
                )
            },
        )
    }
}

@Composable
private fun MusicSearchApp(
    service: OAuth20Service,
    openInBrowser: (String) -> Unit,
) {
    val strings = LocalStrings.current
    var showDialog by rememberSaveable { mutableStateOf(false) }

    if (showDialog) {
        MyDialog(
            onDismiss = {
                showDialog = false
            },
            onSubmit = { code ->
                service.getAccessToken(
                    code,
                    object : OAuthAsyncRequestCallback<OAuth2AccessToken> {
                        override fun onCompleted(response: OAuth2AccessToken?) {
                            println(response?.accessToken)
                            println(response?.refreshToken)
                        }

                        override fun onThrowable(t: Throwable?) {
                            println(t)
                        }
                    },
                )
            },
        )
    }

    Card(
        modifier = Modifier
            .padding(16.dp)
            .clickable {
                val url = service.authorizationUrl
                openInBrowser(url)
                showDialog = true
            },
        backgroundColor = MaterialTheme.colors.onBackground,
    ) {
        Text(
            strings.loginToMusicBrainz,
            modifier = Modifier.padding(16.dp),
            style = TextStyles.getHeaderTextStyle(),
            color = MaterialTheme.colors.primary,
        )
    }
}

@Composable
private fun MyDialog(
    onDismiss: () -> Unit = {},
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
