package ly.david.musicsearch.shared.feature.collections.create

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.network.collectableEntities
import ly.david.musicsearch.ui.common.theme.LocalStrings
import ly.david.musicsearch.ui.common.ResourceDropdownPicker
import ly.david.musicsearch.ui.common.icons.Clear
import ly.david.musicsearch.ui.common.icons.CustomIcons
import ly.david.musicsearch.ui.common.theme.TextStyles

@Composable
fun CreateNewCollectionDialogContent(
    modifier: Modifier = Modifier,
    defaultEntity: MusicBrainzEntity = MusicBrainzEntity.RELEASE,
    onDismiss: () -> Unit = {},
    onSubmit: (name: String, entity: MusicBrainzEntity) -> Unit = { _, _ -> },
) {
    val strings = LocalStrings.current
    var name by rememberSaveable { mutableStateOf("") }
    var selectedEntity by rememberSaveable { mutableStateOf(defaultEntity) }
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(key1 = Unit) {
        focusRequester.requestFocus()
    }

    Column(
        modifier = modifier.padding(24.dp),
    ) {
        Text(
            modifier = Modifier,
            text = strings.createCollection,
            style = TextStyles.getHeaderTextStyle(),
        )

        TextField(
            modifier = Modifier
                .padding(top = 16.dp)
                .focusRequester(focusRequester),
            shape = RectangleShape,
            value = name,
            label = { Text(strings.name) },
            placeholder = { Text(strings.collectionNamePlaceholder) },
            maxLines = 1,
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(
                onNext = {
                    // TODO: handle next focus
                    focusManager.moveFocus(FocusDirection.Down)
                },
            ),
            trailingIcon = {
                if (name.isEmpty()) return@TextField
                IconButton(onClick = {
                    name = ""
                    focusRequester.requestFocus()
                }) {
                    Icon(
                        CustomIcons.Clear,
                        contentDescription = strings.clearSearch,
                    )
                }
            },
            onValueChange = { newText ->
                if (!newText.contains("\n")) {
                    name = newText
                }
            },
        )

        ResourceDropdownPicker(
            modifier = Modifier
                .padding(top = 16.dp)
                .focusable(),
            options = collectableEntities,
            selectedOption = selectedEntity,
            onSelectOption = {
                selectedEntity = it
            },
        )

        Row(
            modifier = Modifier
                .padding(top = 24.dp)
                .align(Alignment.End),
        ) {
            TextButton(onClick = onDismiss) {
                Text(
                    text = strings.cancel,
                )
            }
            TextButton(
                onClick = {
                    onSubmit(
                        name,
                        selectedEntity,
                    )
                    onDismiss()
                },
                enabled = name.isNotEmpty(),
            ) {
                Text(
                    text = strings.ok,
                )
            }
        }
    }
}
