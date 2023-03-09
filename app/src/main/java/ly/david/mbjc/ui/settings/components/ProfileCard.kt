package ly.david.mbjc.ui.settings.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ly.david.mbjc.ui.common.preview.DefaultPreviews
import ly.david.mbjc.ui.theme.PreviewTheme
import ly.david.mbjc.ui.theme.TextStyles

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ProfileCard(
    username: String,
    onLoginClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {},
) {

    val showLogin = username.isEmpty()

    Card(
        onClick = {
            if (showLogin) {
                onLoginClick()
            } else {
                onLogoutClick()
            }
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (showLogin) Icons.Default.Login else Icons.Default.Logout,
                contentDescription = null,
                modifier = Modifier.padding(end = 8.dp)
            )

            Column {
                // TODO: state where user has gotten auth, but we're stilling retrieving their username
                if (showLogin) {
                    Text(
                        text = "Login to MusicBrainz",
                        style = TextStyles.getCardBodyTextStyle()
                    )
                    Text(
                        text = "This lets you sync your collections",
                        style = TextStyles.getCardBodySubTextStyle(),
                    )
                } else {
                    Text(
                        text = username,
                        style = TextStyles.getCardBodyTextStyle(),
                    )
                    Text(
                        text = "Click to logout from MusicBrainz",
                        style = TextStyles.getCardBodySubTextStyle(),
                    )
                }
            }
        }
    }
}

@DefaultPreviews
@Composable
private fun LoggedOut() {
    PreviewTheme {
        Surface {
            ProfileCard(username = "")
        }
    }
}

@DefaultPreviews
@Composable
private fun LoggedIn() {
    PreviewTheme {
        Surface {
            ProfileCard(username = "bob")
        }
    }
}
