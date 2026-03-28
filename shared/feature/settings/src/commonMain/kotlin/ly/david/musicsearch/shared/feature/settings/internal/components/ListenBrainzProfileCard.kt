package ly.david.musicsearch.shared.feature.settings.internal.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ly.david.musicsearch.ui.common.dialog.DialogWithCloseButton
import ly.david.musicsearch.ui.common.icons.CustomIcons
import ly.david.musicsearch.ui.common.icons.Login
import ly.david.musicsearch.ui.common.icons.Logout
import ly.david.musicsearch.ui.common.text.TextInput
import ly.david.musicsearch.ui.common.text.buildStringWithSingleLink
import ly.david.musicsearch.ui.common.theme.TextStyles
import musicsearch.ui.common.generated.resources.Res
import musicsearch.ui.common.generated.resources.clickToLogoutFromListenBrainz
import musicsearch.ui.common.generated.resources.enterYourUserToken
import musicsearch.ui.common.generated.resources.findYourListenBrainzUserTokenAt
import musicsearch.ui.common.generated.resources.letsYouModifyYourListens
import musicsearch.ui.common.generated.resources.login
import musicsearch.ui.common.generated.resources.loginToListenBrainz
import musicsearch.ui.common.generated.resources.userToken
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun ListenBrainzProfileCard(
    listenBrainzUrl: String,
    username: String,
    text: String,
    showLogin: Boolean,
    onTextChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onLogoutClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var showDialog by rememberSaveable { mutableStateOf(false) }

    if (showDialog) {
        DialogWithCloseButton(
            onDismiss = { showDialog = false },
        ) {
            TokenInput(
                listenBrainzUrl = listenBrainzUrl,
                text = text,
                onTextChange = onTextChange,
                onSetToken = {
                    showDialog = false
                    onLoginClick()
                },
            )
        }
    }

    Card(
        modifier = modifier,
        onClick = {
            if (showLogin) {
                showDialog = true
            } else {
                onLogoutClick()
            }
        },
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = if (showLogin) CustomIcons.Login else CustomIcons.Logout,
                contentDescription = null,
                modifier = Modifier.padding(end = 8.dp),
            )

            Column {
                if (showLogin) {
                    Text(
                        text = stringResource(Res.string.loginToListenBrainz),
                        style = TextStyles.getCardBodyTextStyle(),
                    )
                    Text(
                        text = stringResource(Res.string.letsYouModifyYourListens),
                        style = TextStyles.getCardBodySubTextStyle(),
                    )
                } else {
                    if (username.isEmpty()) {
                        CircularProgressIndicator(modifier = Modifier.size(16.dp))
                    } else {
                        Text(
                            text = username,
                            style = TextStyles.getCardBodyTextStyle(),
                        )
                    }
                    Text(
                        text = stringResource(Res.string.clickToLogoutFromListenBrainz),
                        style = TextStyles.getCardBodySubTextStyle(),
                    )
                }
            }
        }
    }
}

@Composable
fun TokenInput(
    listenBrainzUrl: String,
    text: String,
    modifier: Modifier = Modifier,
    onTextChange: (String) -> Unit = {},
    onSetToken: () -> Unit = {},
) {
    val settingsUrl = "$listenBrainzUrl/settings"
    TextInput(
        modifier = modifier,
        instructions = buildStringWithSingleLink(
            resourceWithPlaceholder = Res.string.findYourListenBrainzUserTokenAt,
            linkLabel = settingsUrl,
            url = settingsUrl,
        ),
        textLabel = stringResource(Res.string.userToken),
        textHint = stringResource(Res.string.enterYourUserToken),
        text = text,
        buttonText = stringResource(Res.string.login),
        onTextChange = onTextChange,
        onButtonClick = onSetToken,
    )
}
