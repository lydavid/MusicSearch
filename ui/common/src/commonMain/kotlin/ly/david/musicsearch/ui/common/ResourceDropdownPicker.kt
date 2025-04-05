package ly.david.musicsearch.ui.common

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.unit.dp
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.network.resourceUri
import ly.david.musicsearch.ui.core.LocalStrings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResourceDropdownPicker(
    options: List<MusicBrainzEntity>,
    selectedOption: MusicBrainzEntity,
    modifier: Modifier = Modifier,
    onSelectOption: (MusicBrainzEntity) -> Unit = {},
) {
    val strings = LocalStrings.current
    var expanded by rememberSaveable { mutableStateOf(false) }

    // TODO: this can't properly take up the full size allotted to it
    //  so in landscape mode, it doesn't fill up half the screen
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier,
    ) {
        TextField(
            modifier = Modifier.menuAnchor(),
            readOnly = true,
            shape = RectangleShape,
            value = selectedOption.getName(strings),
            onValueChange = { },
            label = { Text(strings.resource) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded,
                )
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
        )

        // Focusing on this "consumes" a back press, and is independent of onDismissRequest.
        ExposedDropdownMenu(
            modifier = Modifier.semantics {
                testTag = "ExposedDropdownMenu"
            },
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            options.forEach { option ->
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = if (selectedOption == option) {
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                    } else {
                        MaterialTheme.colorScheme.background
                    },
                ) {
                    DropdownMenuItem(
                        modifier = Modifier.semantics {
                            testTag = option.resourceUri
                        },
                        text = {
                            Row(verticalAlignment = CenterVertically) {
                                EntityIcon(
                                    modifier = Modifier.padding(end = 8.dp),
                                    entity = option,
                                )
                                Text(option.getName(strings))
                            }
                        },
                        onClick = {
                            onSelectOption(option)
                            expanded = false
                        },
                    )
                }
            }
        }
    }
}
