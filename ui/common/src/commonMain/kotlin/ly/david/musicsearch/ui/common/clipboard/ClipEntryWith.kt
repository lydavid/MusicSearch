package ly.david.musicsearch.ui.common.clipboard

import androidx.compose.ui.platform.ClipEntry

// https://youtrack.jetbrains.com/issue/CMP-7624
expect fun clipEntryWith(text: String): ClipEntry
