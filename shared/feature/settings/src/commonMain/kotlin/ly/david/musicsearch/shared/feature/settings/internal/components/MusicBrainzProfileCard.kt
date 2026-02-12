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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ly.david.musicsearch.ui.common.icons.CustomIcons
import ly.david.musicsearch.ui.common.icons.Login
import ly.david.musicsearch.ui.common.icons.Logout
import ly.david.musicsearch.ui.common.theme.TextStyles
import musicsearch.ui.common.generated.resources.Res
import musicsearch.ui.common.generated.resources.loginToMusicBrainz
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun MusicBrainzProfileCard(
    username: String,
    showLogin: Boolean,
    onLoginClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {},
) {
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
                imageVector = if (showLogin) CustomIcons.Login else CustomIcons.Logout,
                contentDescription = null,
                modifier = Modifier.padding(end = 8.dp),
            )

            Column {
                if (showLogin) {
                    Text(
                        text = stringResource(Res.string.loginToMusicBrainz),
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
