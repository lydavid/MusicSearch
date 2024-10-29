package ly.david.musicsearch.ui.common.text

import androidx.compose.ui.text.font.FontWeight
import ly.david.musicsearch.shared.domain.listitem.Visitable

val Visitable.fontWeight: FontWeight
    get() = if (visited) FontWeight.Normal else FontWeight.Bold
