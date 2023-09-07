package ly.david.ui.core.preview

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview

@Preview(name = "Light")
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
actual annotation class DefaultPreviews
