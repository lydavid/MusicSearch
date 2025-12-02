package ly.david.musicsearch.ui.common.text

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

private const val SPACER_WEIGHT = 0.1f
private const val CONTENT_WEIGHT = 0.8f

@Composable
fun TextInput(
    instructions: AnnotatedString,
    textLabel: String?,
    textHint: String?,
    text: String,
    buttonText: String,
    modifier: Modifier = Modifier,
    onTextChange: (String) -> Unit = {},
    onButtonClick: () -> Unit = {},
) {
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
            SingleLineTextField(
                text = text,
                textLabel = textLabel,
                textHint = textHint,
                onTextChange = onTextChange,
                modifier = Modifier.padding(top = 16.dp),
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
