package ly.david.musicsearch.shared.feature.collections.list

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.paging.PagingData
import app.cash.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.flow.MutableStateFlow
import ly.david.musicsearch.core.models.listitem.CollectionListItemModel
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.ui.core.preview.DefaultPreviews
import ly.david.ui.core.theme.PreviewTheme

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@DefaultPreviews
@Composable
internal fun PreviewCollectionList() {
    PreviewTheme {
        val items = MutableStateFlow(
            PagingData.from(
                listOf(
                    CollectionListItemModel(
                        id = "1",
                        isRemote = true,
                        name = "Favorite works",
                        entity = MusicBrainzEntity.WORK,
                    ),
                    CollectionListItemModel(
                        id = "2",
                        isRemote = false,
                        name = "My CD collection",
                        entity = MusicBrainzEntity.RELEASE,
                    ),
                ),
            ),
        )

        CollectionList(
            lazyPagingItems = items.collectAsLazyPagingItems(),
        )
    }
}
