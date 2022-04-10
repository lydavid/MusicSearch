package ly.david.mbjc.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ly.david.mbjc.R
import ly.david.mbjc.ui.theme.PreviewTheme

@Composable
fun NavigationDrawer(
    selectedTopLevelDestination: Destination,
    closeDrawer: () -> Unit = {},
    navigateToTopLevelDestination: (Destination) -> Unit = {}
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column {
            Text(
                text = stringResource(id = R.string.app_name),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary,
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
        MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
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
                    tint = MaterialTheme.colorScheme.onBackground,
                )
                Spacer(Modifier.width(16.dp))
                Text(
                    text = label,
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.headlineSmall
                )
            }
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
internal fun NavigationDrawerPreview() {
    PreviewTheme {
        Surface {
            NavigationDrawer(selectedTopLevelDestination = Destination.HISTORY)
        }
    }
}
