package ly.david.musicsearch.shared.feature.settings.internal

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import ly.david.ui.common.listitem.ListSeparatorHeader

@Composable
internal fun DevSettingsSection() {
    Column {
        ListSeparatorHeader(text = "Dev Settings")
    }
}
