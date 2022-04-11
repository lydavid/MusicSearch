package ly.david.mbjc.ui.search

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import ly.david.mbjc.data.network.MusicBrainzResource

// https://developer.android.com/reference/kotlin/androidx/compose/material/package-summary#ExposedDropdownMenuBox(kotlin.Boolean,kotlin.Function1,androidx.compose.ui.Modifier,kotlin.Function1)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ExposedDropdownMenuBox(
    modifier: Modifier = Modifier,
    options: List<MusicBrainzResource>,
    selectedOption: MusicBrainzResource,
    onSelectOption: (MusicBrainzResource) -> Unit
) {

    var expanded by rememberSaveable { mutableStateOf(false) }

    // We want to react on tap/press on TextField to show menu
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        },
        modifier = modifier
    ) {
        TextField(
            modifier = modifier.fillMaxWidth(),
            readOnly = true,
            shape = RectangleShape,
            value = selectedOption.displayText,
            onValueChange = { },
            label = { Text("Resource") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors()
        )
        ExposedDropdownMenu(
            modifier = modifier,
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            options.forEach { selectionOption ->
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = if (selectedOption == selectionOption) {
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                    } else {
                        MaterialTheme.colorScheme.background
                    }
                ) {
                    DropdownMenuItem(
                        text = { Text(text = selectionOption.displayText) },
                        onClick = {
                            onSelectOption(selectionOption)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}
