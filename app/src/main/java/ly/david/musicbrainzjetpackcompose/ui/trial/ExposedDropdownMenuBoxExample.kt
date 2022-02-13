package ly.david.musicbrainzjetpackcompose.ui.trial

import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import ly.david.musicbrainzjetpackcompose.QueryResources

// https://developer.android.com/reference/kotlin/androidx/compose/material/package-summary#ExposedDropdownMenuBox(kotlin.Boolean,kotlin.Function1,androidx.compose.ui.Modifier,kotlin.Function1)

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ExposedDropdownMenuBoxExample(
    modifier: Modifier = Modifier,
    options: List<QueryResources>,
    selectedOption: QueryResources,
    onSelectOption: (QueryResources) -> Unit
) {



    var expanded by remember { mutableStateOf(false) }



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
                DropdownMenuItem(
                    onClick = {
//                        selectedOptionText = selectionOption
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
