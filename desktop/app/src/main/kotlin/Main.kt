
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.github.scribejava.apis.TwitterApi
import com.github.scribejava.core.builder.ServiceBuilder
import com.github.scribejava.core.model.OAuthRequest
import com.github.scribejava.core.model.Verb
import ly.david.musicsearch.core.preferences.di.preferencesDataStoreModule
import ly.david.ui.core.theme.BaseTheme
import ly.david.ui.core.theme.TextStyles
import org.koin.core.context.startKoin

fun main() = application {
    val windowState = rememberWindowState()

    startKoin {
        modules(preferencesDataStoreModule)
    }

    Window(
        onCloseRequest = ::exitApplication,
        state = windowState,
        title = "MusicSearch",
    ) {
        BaseTheme(
            content = {
                Card(
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable {
//                            val url = "https://kotlinlang.org/lp/multiplatform/"
//                            Desktop.getDesktop().browse(URI.create(url))
                            doOAuth()
                        },
                    backgroundColor = MaterialTheme.colors.onBackground,
                ) {
                    Text(
                        "Hello world!",
                        modifier = Modifier.padding(16.dp),
                        style = TextStyles.getHeaderTextStyle(),
                        color = MaterialTheme.colors.primary,
                    )
                }
            },
        )
    }
}

private fun doOAuth() {
    val service = ServiceBuilder("key")
        .apiSecret("secret")
        .build(TwitterApi.instance())
    val requestToken = service.requestToken
    val authUrl = service.getAuthorizationUrl(requestToken)
    val accessToken = service.getAccessToken(requestToken, "verifier you got from the user/callback")
    val request = OAuthRequest(Verb.GET, "https://api.twitter.com/1.1/account/verify_credentials.json")
    service.signRequest(accessToken, request)
    val response = service.execute(request)
    println(response.body)
}
