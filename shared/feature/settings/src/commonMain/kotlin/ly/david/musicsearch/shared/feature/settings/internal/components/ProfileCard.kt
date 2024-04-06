package ly.david.musicsearch.shared.feature.settings.internal.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ly.david.ui.core.LocalStrings
import ly.david.ui.core.theme.TextStyles

@Composable
internal fun ProfileCard(
    username: String,
    showLogin: Boolean,
    onLoginClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {},
) {
    val strings = LocalStrings.current

    Card(
        onClick = {
            if (showLogin) {
                onLoginClick()
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
                imageVector = if (showLogin) Icons.Default.Login else Icons.Default.Logout,
                contentDescription = null,
                modifier = Modifier.padding(end = 8.dp),
            )

            Column {
                if (showLogin) {
                    Text(
                        text = strings.loginToMusicBrainz,
                        style = TextStyles.getCardBodyTextStyle(),
                    )
                    Text(
                        text = "This lets you sync your collections",
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
                        text = "Click to logout from MusicBrainz",
                        style = TextStyles.getCardBodySubTextStyle(),
                    )
                }
            }
        }
    }
}
