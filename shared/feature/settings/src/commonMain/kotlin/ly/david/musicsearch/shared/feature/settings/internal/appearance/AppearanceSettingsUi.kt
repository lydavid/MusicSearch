package ly.david.musicsearch.shared.feature.settings.internal.appearance

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import com.github.skydoves.colorpicker.compose.ColorEnvelope
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import kotlinx.collections.immutable.toImmutableList
import ly.david.musicsearch.shared.domain.DEFAULT_SEED_COLOR_INT
import ly.david.musicsearch.shared.domain.DEFAULT_SEED_COLOR_LONG
import ly.david.musicsearch.shared.domain.preferences.AppPreferences
import ly.david.musicsearch.shared.feature.settings.internal.components.SettingSwitch
import ly.david.musicsearch.shared.feature.settings.internal.components.SettingWithDialogChoices
import ly.david.musicsearch.ui.common.topappbar.ScrollableTopAppBar
import musicsearch.ui.common.generated.resources.Res
import musicsearch.ui.common.generated.resources.appearance
import musicsearch.ui.common.generated.resources.dark
import musicsearch.ui.common.generated.resources.light
import musicsearch.ui.common.generated.resources.system
import musicsearch.ui.common.generated.resources.theme
import org.jetbrains.compose.resources.stringResource

@Composable
internal expect fun AppearanceSettingsUi(
    state: AppearanceSettingsUiState,
    modifier: Modifier = Modifier,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AppearanceSettingsUi(
    state: AppearanceSettingsUiState,
    modifier: Modifier = Modifier,
    isAndroid12Plus: Boolean = false,
) {
    val eventSink = state.eventSink

    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets(0),
        topBar = {
            ScrollableTopAppBar(
                showBackButton = true,
                title = stringResource(Res.string.appearance),
                onBack = {
                    eventSink(AppearanceSettingsUiEvent.NavigateUp)
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding),
        ) {
            SettingWithDialogChoices(
                title = stringResource(Res.string.theme),
                choices = AppPreferences.Theme.entries.map { it.getText() }.toImmutableList(),
                selectedChoiceIndex = state.theme.ordinal,
                onSelectChoiceIndex = {
                    eventSink(AppearanceSettingsUiEvent.UpdateTheme(AppPreferences.Theme.entries[it]))
                },
            )

            val useMaterialYou = state.useMaterialYou
            if (isAndroid12Plus) {
                SettingSwitch(
                    header = "Use Material You",
                    checked = useMaterialYou,
                    onCheckedChange = {
                        eventSink(AppearanceSettingsUiEvent.UpdateUseMaterialYou(it))
                    },
                )
            }

            CustomColorPickerSection(
                state = state,
                useMaterialYou = useMaterialYou,
                eventSink = eventSink,
            )
        }
    }
}

@Composable
private fun ColumnScope.CustomColorPickerSection(
    state: AppearanceSettingsUiState,
    useMaterialYou: Boolean,
    eventSink: (AppearanceSettingsUiEvent) -> Unit,
) {
    val controller = rememberColorPickerController()
    val seedColor = Color(state.seedColor)
    var hexColor by rememberSaveable { mutableStateOf("") }
    if (!useMaterialYou) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(16.dp),
        ) {
            Text(
                text = "Seed color: #${hexColor.uppercase()}",
            )
            Spacer(Modifier.weight(1f))
            TextButton(
                onClick = {
                    eventSink(AppearanceSettingsUiEvent.SetSeedColor(DEFAULT_SEED_COLOR_INT))
                    controller.selectByColor(Color(DEFAULT_SEED_COLOR_LONG), fromUser = false)
                },
            ) {
                Text(
                    text = "Reset",
                )
            }
        }
    }

    AnimatedVisibility(
        visible = !useMaterialYou,
        enter = scaleIn(),
        exit = scaleOut(),
    ) {
        HsvColorPicker(
            modifier = Modifier
                .fillMaxWidth()
                .height(450.dp)
                .padding(16.dp),
            controller = controller,
            initialColor = seedColor,
            onColorChanged = { colorEnvelope: ColorEnvelope ->
                hexColor = colorEnvelope.hexCode
                eventSink(AppearanceSettingsUiEvent.SetSeedColor(colorEnvelope.color.toArgb()))
            },
        )
    }
}

@Composable
private fun AppPreferences.Theme.getText(): String =
    stringResource(
        when (this) {
            AppPreferences.Theme.LIGHT -> Res.string.light
            AppPreferences.Theme.DARK -> Res.string.dark
            AppPreferences.Theme.SYSTEM -> Res.string.system
        },
    )
