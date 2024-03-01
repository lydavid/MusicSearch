package ly.david.musicsearch.shared.feature.collections

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import ly.david.musicsearch.core.models.listitem.CollectionListItemModel
import ly.david.musicsearch.strings.LocalStrings
import ly.david.ui.common.paging.ScreenWithPagingLoadingAndError
import ly.david.ui.common.topappbar.TopAppBarWithFilter
import ly.david.ui.core.preview.DefaultPreviews
import ly.david.ui.core.theme.PreviewTheme

//@DefaultPreviews
//@Composable
//private fun Preview() {
//    PreviewTheme {
//        var showLocal by rememberSaveable { mutableStateOf(true) }
//        var showRemote by rememberSaveable { mutableStateOf(true) }
//
//        Surface {
//            CollectionsFilterChipsBar(
//                showLocal = showLocal,
//                onShowLocalToggle = { showLocal = it },
//                showRemote = showRemote,
//                onShowRemoteToggle = { showRemote = it },
//            )
//        }
//    }
//}
