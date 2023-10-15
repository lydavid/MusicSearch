package ly.david.ui.collections

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.window.Dialog
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.core.models.network.collectableEntities
import ly.david.ui.common.ExposedDropdownMenuBox
import ly.david.musicsearch.strings.LocalStrings
import ly.david.ui.core.preview.DefaultPreviews
import ly.david.ui.core.theme.PreviewTheme
import ly.david.ui.core.theme.TextStyles

@Composable
fun CreateCollectionDialog(
    onDismiss: () -> Unit = {},
    onSubmit: (name: String, entity: MusicBrainzEntity) -> Unit = { _, _ -> },
) {
    val strings = LocalStrings.current
    var name by rememberSaveable { mutableStateOf("") }
    var selectedEntity by rememberSaveable { mutableStateOf(MusicBrainzEntity.RELEASE) }
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }

    // TODO: crashes on recomposition
//    LaunchedEffect(key1 = Unit) {
//        focusRequester.requestFocus()
//    }

    Dialog(
        onDismissRequest = {
            onDismiss()
        },
    ) {
        Surface(
            shape = RoundedCornerShape(28.dp),
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
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
                            Icon(Icons.Default.Clear, contentDescription = strings.clearSearch)
                        }
                    },
                    onValueChange = { newText ->
                        if (!newText.contains("\n")) {
                            name = newText
                        }
                    },
                )

                ExposedDropdownMenuBox(
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
                            onSubmit(name, selectedEntity)
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
    }
}

@DefaultPreviews
@Composable
private fun Preview() {
    PreviewTheme {
        Surface {
            CreateCollectionDialog()
        }
    }
}
