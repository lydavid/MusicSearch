package ly.david.musicsearch.ui.common.text

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import ly.david.musicsearch.ui.common.icons.Clear
import ly.david.musicsearch.ui.common.icons.CustomIcons

@Composable
fun SingleLineTextField(
    text: String,
    textLabel: String?,
    textHint: String?,
    onTextChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusRequester = remember { FocusRequester() }
    TextField(
        modifier = modifier
            .fillMaxWidth(),
        shape = RectangleShape,
        value = text,
        label = textLabel?.let {
            {
                Text(
                    text = textLabel,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        },
        placeholder = textHint?.let {
            {
                Text(
                    text = textHint,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        },
        maxLines = 1,
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        trailingIcon = {
            if (text.isEmpty()) return@TextField
            IconButton(onClick = {
                onTextChange("")
                focusRequester.requestFocus()
            }) {
                Icon(
                    CustomIcons.Clear,
                    contentDescription = "Clear",
                )
            }
        },
        onValueChange = { newText ->
            if (!newText.contains("\n")) {
                onTextChange(newText)
            }
        },
    )
}
