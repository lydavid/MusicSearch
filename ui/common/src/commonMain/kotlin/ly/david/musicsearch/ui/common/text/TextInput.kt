package ly.david.musicsearch.ui.common.text

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ly.david.musicsearch.ui.common.icons.Clear
import ly.david.musicsearch.ui.common.icons.CustomIcons

private const val SPACER_WEIGHT = 0.1f
private const val CONTENT_WEIGHT = 0.8f

@Composable
fun TextInput(
    instructions: AnnotatedString,
    textLabel: String,
    textHint: String,
    text: String,
    buttonText: String,
    modifier: Modifier = Modifier,
    onTextChange: (String) -> Unit = {},
    onButtonClick: () -> Unit = {},
) {
    val focusRequester = remember { FocusRequester() }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        Spacer(modifier = Modifier.weight(SPACER_WEIGHT))

        Column(
            modifier = Modifier
                .weight(CONTENT_WEIGHT)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = instructions,
                textAlign = TextAlign.Center,
            )
            TextField(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth(),
                shape = RectangleShape,
                value = text,
                label = { Text(textLabel) },
                placeholder = { Text(textHint) },
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
            Button(
                modifier = Modifier
                    .padding(top = 16.dp),
                onClick = onButtonClick,
            ) {
                Text(buttonText)
            }
        }
        Spacer(modifier = Modifier.weight(SPACER_WEIGHT))
    }
}
