package ly.david.mbjc.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ly.david.mbjc.R
import ly.david.mbjc.ui.theme.MusicBrainzJetpackComposeTheme

@Composable
fun NavigationDrawer(
    selectedTopLevelDestination: Destination,
    closeDrawer: () -> Unit = {},
    navigateToTopLevelDestination: (Destination) -> Unit = {}
) {
    Column {

        Text(
            text = stringResource(id = R.string.app_name),
            style = MaterialTheme.typography.h5,
            color = MaterialTheme.colors.primary,
            modifier = Modifier.padding(16.dp)
        )

        Divider(modifier = Modifier.padding(bottom = 8.dp))

        NavigationDrawerItem(
            icon = Icons.Default.Search,
            iconDescription = "Navigate to search Music Brainz screen.",
            label = "Search MusicBrainz",
            isSelected = selectedTopLevelDestination == Destination.LOOKUP
        ) {
            navigateToTopLevelDestination(Destination.LOOKUP)
            closeDrawer()
        }
        NavigationDrawerItem(
            icon = Icons.Default.History,
            iconDescription = "Navigate to search history screen.",
            label = "Search History",
            isSelected = selectedTopLevelDestination == Destination.HISTORY
        ) {
            navigateToTopLevelDestination(Destination.HISTORY)
            closeDrawer()
        }
    }
}

@Composable
fun NavigationDrawerItem(
    icon: ImageVector,
    iconDescription: String,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit = {}
) {

    val backgroundColor = if (isSelected) {
        MaterialTheme.colors.primary.copy(alpha = 0.2f)
    } else {
        Color.Transparent
    }

    Surface(
        shape = RoundedCornerShape(topEnd = 32.dp, bottomEnd = 32.dp),
        color = backgroundColor,
        modifier = Modifier.padding(end = 8.dp)
    ) {
        TextButton(onClick = onClick) {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(4.dp)
                    .fillMaxWidth()
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = iconDescription,
                    tint = MaterialTheme.colors.onBackground,
                )
                Spacer(Modifier.width(16.dp))
                Text(
                    text = label,
                    color = MaterialTheme.colors.onBackground,
                    style = MaterialTheme.typography.body1
                )
            }
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
internal fun NavigationDrawerPreview() {
    MusicBrainzJetpackComposeTheme {
        Surface {
            NavigationDrawer(selectedTopLevelDestination = Destination.HISTORY)
        }
    }
}
