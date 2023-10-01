package ly.david.ui.settings

import android.os.Build
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ly.david.data.domain.Destination
import ly.david.ui.common.R
import ly.david.ui.common.component.ClickableItem
import ly.david.ui.common.listitem.ListSeparatorHeader
import ly.david.ui.common.text.TextWithHeading
import ly.david.ui.core.preview.DefaultPreviews
import ly.david.ui.core.theme.PreviewTheme
import ly.david.ui.settings.components.ProfileCard
import ly.david.ui.settings.components.SettingSwitch
import ly.david.ui.settings.components.SettingWithDialogChoices

@Composable
internal fun SettingsScreen(
    modifier: Modifier = Modifier,
    username: String = "",
    showLogin: Boolean = true,
    onLoginClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {},
    onDestinationClick: (Destination) -> Unit = {},
    theme: AppPreferences.Theme = AppPreferences.Theme.SYSTEM,
    onThemeChange: (AppPreferences.Theme) -> Unit = {},
    useMaterialYou: Boolean = true,
    onUseMaterialYouChange: (Boolean) -> Unit = {},
    showMoreInfoInReleaseListItem: Boolean = true,
    onShowMoreInfoInReleaseListItemChange: (Boolean) -> Unit = {},
    sortReleaseGroupListItems: Boolean = false,
    onSortReleaseGroupListItemsChange: (Boolean) -> Unit = {},
    isNotificationListenerEnabled: Boolean = false,
    onGoToNotificationListenerSettings: () -> Unit = {},
    versionName: String = "",
    versionCode: Int = 0,
) {
    LazyColumn(modifier = modifier) {
        item {
            ProfileCard(
                username = username,
                showLogin = showLogin,
                onLoginClick = onLoginClick,
                onLogoutClick = onLogoutClick
            )

            SettingWithDialogChoices(
                titleRes = R.string.theme,
                choices = AppPreferences.Theme.values().map { stringResource(id = it.textRes) },
                selectedChoiceIndex = theme.ordinal,
                onSelectChoiceIndex = { onThemeChange(AppPreferences.Theme.values()[it]) },
            )

            val isAndroid12 = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
            if (isAndroid12) {
                SettingSwitch(
                    header = "Use Material You",
                    checked = useMaterialYou,
                    onCheckedChange = onUseMaterialYouChange
                )
            }

            SettingSwitch(
                header = "Show more info in release list items",
                checked = showMoreInfoInReleaseListItem,
                onCheckedChange = onShowMoreInfoInReleaseListItemChange
            )

            SettingSwitch(
                header = "Sort release groups by type",
                checked = sortReleaseGroupListItems,
                onCheckedChange = onSortReleaseGroupListItemsChange
            )

            ListSeparatorHeader(text = stringResource(id = R.string.experimental_search))

            if (isNotificationListenerEnabled) {
                ClickableItem(
                    title = stringResource(id = R.string.now_playing_history),
                    subtitle = stringResource(id = R.string.now_playing_history_subtitle),
                    endIcon = Icons.Default.ChevronRight,
                    onClick = {
                        onDestinationClick(Destination.SETTINGS_NOWPLAYING)
                    },
                )
            } else {
                ClickableItem(
                    title = stringResource(id = R.string.enable_notification_listener),
                    subtitle = stringResource(id = R.string.enable_notification_listener_subtitle),
                    onClick = onGoToNotificationListenerSettings,
                )
            }

            ClickableItem(
                title = stringResource(id = R.string.spotify),
                subtitle = stringResource(id = R.string.spotify_subtitle),
                endIcon = Icons.Default.ChevronRight,
                onClick = {
                    onDestinationClick(Destination.EXPERIMENTAL_SPOTIFY)
                },
            )

            ListSeparatorHeader(text = stringResource(id = R.string.about))

            ClickableItem(
                title = stringResource(id = R.string.open_source_licenses),
                endIcon = Icons.Default.ChevronRight,
                onClick = {
                    onDestinationClick(Destination.SETTINGS_LICENSES)
                },
            )

            val versionKey = stringResource(id = R.string.app_version)
            TextWithHeading(heading = versionKey, text = "$versionName ($versionCode)")

            if (BuildConfig.DEBUG) {
                DevSettingsSection()
            }
        }
    }
}

// region Previews
@DefaultPreviews
@Composable
internal fun PreviewSettingsScreen() {
    PreviewTheme {
        Surface {
            SettingsScreen(
                versionName = "1.0.0",
                versionCode = 1,
            )
        }
    }
}

@DefaultPreviews
@Composable
internal fun PreviewSettingsScreenNotificationListenerEnable() {
    PreviewTheme {
        Surface {
            SettingsScreen(
                isNotificationListenerEnabled = true,
                versionName = "1.0.0",
                versionCode = 1,
            )
        }
    }
}
// endregion
