package ly.david.musicsearch.shared.feature.settings.internal.appearance

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.nestedscroll.nestedScroll
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
import ly.david.musicsearch.ui.common.scaffold.AppScaffold
import ly.david.musicsearch.ui.common.topappbar.ScrollableTopAppBar
import musicsearch.ui.common.generated.resources.Res
import musicsearch.ui.common.generated.resources.appearance
import musicsearch.ui.common.generated.resources.dark
import musicsearch.ui.common.generated.resources.light
import musicsearch.ui.common.generated.resources.reset
import musicsearch.ui.common.generated.resources.scrollToHideTopAppBar
import musicsearch.ui.common.generated.resources.seedColor
import musicsearch.ui.common.generated.resources.system
import musicsearch.ui.common.generated.resources.theme
import musicsearch.ui.common.generated.resources.useMaterialYou
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

    AppScaffold(
        modifier = modifier,
        scrollToHideTopAppBar = state.scrollToHideTopAppBar,
        snackbarHostState = SnackbarHostState(),
        topBar = { scrollBehavior ->
            ScrollableTopAppBar(
                showBackButton = true,
                title = stringResource(Res.string.appearance),
                scrollBehavior = scrollBehavior,
                onBack = {
                    eventSink(AppearanceSettingsUiEvent.NavigateUp)
                },
            )
        },
    ) { innerPadding, scrollBehavior ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .verticalScroll(rememberScrollState()),
        ) {
            SettingWithDialogChoices(
                title = stringResource(Res.string.theme),
                choices = AppPreferences.Theme.entries.map { it.getText() }.toImmutableList(),
                selectedChoiceIndex = state.theme.ordinal,
                onSelectChoiceIndex = {
                    eventSink(AppearanceSettingsUiEvent.UpdateTheme(AppPreferences.Theme.entries[it]))
                },
            )

            SettingSwitch(
                header = stringResource(Res.string.scrollToHideTopAppBar),
                checked = state.scrollToHideTopAppBar,
                onCheckedChange = {
                    eventSink(AppearanceSettingsUiEvent.UpdateScrollToHideTopAppBar(it))
                },
            )

            val useMaterialYou = state.useMaterialYou
            if (isAndroid12Plus) {
                SettingSwitch(
                    header = stringResource(Res.string.useMaterialYou),
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

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
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
                text = stringResource(Res.string.seedColor) + ": #${hexColor.uppercase()}",
            )
            Spacer(Modifier.weight(1f))
            TextButton(
                onClick = {
                    eventSink(AppearanceSettingsUiEvent.SetSeedColor(DEFAULT_SEED_COLOR_INT))
                    controller.selectByColor(Color(DEFAULT_SEED_COLOR_LONG), fromUser = false)
                },
            ) {
                Text(
                    text = stringResource(Res.string.reset),
                )
            }
        }
    }

    val windowSizeClass: WindowSizeClass = calculateWindowSizeClass()
    val padding = if (windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact) {
        16.dp
    } else {
        64.dp
    }

    AnimatedVisibility(
        visible = !useMaterialYou,
        enter = scaleIn(),
        exit = scaleOut(),
    ) {
        HsvColorPicker(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = padding)
                .height(450.dp),
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
