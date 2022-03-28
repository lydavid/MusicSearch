package ly.david.mbjc.ui.search

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import ly.david.mbjc.data.network.MusicBrainzResource

// https://developer.android.com/reference/kotlin/androidx/compose/material/package-summary#ExposedDropdownMenuBox(kotlin.Boolean,kotlin.Function1,androidx.compose.ui.Modifier,kotlin.Function1)

@OptIn(ExperimentalMaterialApi::class)
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
            readOnly = true,
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
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            options.forEach { selectionOption ->
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = if (selectedOption == selectionOption) {
                        MaterialTheme.colors.primary.copy(alpha = 0.2f)
                    } else {
                        MaterialTheme.colors.background
                    }
                ) {
                    DropdownMenuItem(
                        onClick = {
                            onSelectOption(selectionOption)
                            expanded = false
                        }
                    ) {
                        Text(text = selectionOption.displayText)
                    }
                }
            }
        }
    }
}
