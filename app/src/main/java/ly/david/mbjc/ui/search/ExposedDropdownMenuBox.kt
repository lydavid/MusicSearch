package ly.david.mbjc.ui.search

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.unit.dp
import ly.david.data.network.MusicBrainzResource
import ly.david.data.network.resourceUri
import ly.david.mbjc.ui.common.ResourceIcon
import ly.david.mbjc.ui.common.getDisplayTextRes
import ly.david.ui.common.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ExposedDropdownMenuBox(
    modifier: Modifier = Modifier,
    options: List<MusicBrainzResource>,
    selectedOption: MusicBrainzResource,
    onSelectOption: (MusicBrainzResource) -> Unit
) {

    var expanded by rememberSaveable { mutableStateOf(false) }

    // TODO: this can't properly take up the full size allotted to it
    //  so in landscape mode, it doesn't fill up half the screen
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        TextField(
            modifier = Modifier.menuAnchor(),
            readOnly = true,
            shape = RectangleShape,
            value = stringResource(id = selectedOption.getDisplayTextRes()),
            onValueChange = { },
            label = { Text(stringResource(id = R.string.resource)) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors()
        )

        // Focusing on this "consumes" a back press, and is independent of onDismissRequest.
        ExposedDropdownMenu(
            modifier = Modifier.semantics {
                testTag = "ExposedDropdownMenu"
            },
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = if (selectedOption == option) {
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                    } else {
                        MaterialTheme.colorScheme.background
                    }
                ) {
                    DropdownMenuItem(
                        modifier = Modifier.semantics {
                            testTag = option.resourceUri
                        },
                        text = {
                            Row(verticalAlignment = CenterVertically) {
                                ResourceIcon(
                                    modifier = Modifier.padding(end = 8.dp),
                                    resource = option
                                )
                                Text(text = stringResource(id = option.getDisplayTextRes()))
                            }
                        },
                        onClick = {
                            onSelectOption(option)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}
