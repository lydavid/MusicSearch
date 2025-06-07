package ly.david.musicsearch.ui.common.work

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import ly.david.musicsearch.shared.domain.listitem.WorkListItemModel
import ly.david.musicsearch.ui.common.preview.PreviewWithSharedElementTransition

@PreviewLightDark
@Composable
internal fun PreviewWorkListItemModel() {
    PreviewWithSharedElementTransition {
        WorkListItem(
            work = WorkListItemModel(
                id = "1",
                name = "work name",
            ),
        )
    }
}

@PreviewLightDark
@Composable
internal fun PreviewWorkListItemModelAllInfo() {
    PreviewWithSharedElementTransition {
        WorkListItem(
            work = WorkListItemModel(
                id = "343dbbe6-d9ce-3853-8d8d-230734c0424f",
                name = "残酷な天使のテーゼ",
                disambiguation = "Neon Genesis Evangelion",
                type = "Song",
                iswcs = listOf("T-101.261.638-3"),
                language = "jpn",
            ),
        )
    }
}

@PreviewLightDark
@Composable
internal fun PreviewWorkListItemModelVisited() {
    PreviewWithSharedElementTransition {
        WorkListItem(
            work = WorkListItemModel(
                id = "343dbbe6-d9ce-3853-8d8d-230734c0424f",
                name = "残酷な天使のテーゼ",
                disambiguation = "Neon Genesis Evangelion",
                type = "Song",
                iswcs = listOf("T-101.261.638-3"),
                language = "jpn",
                visited = true,
            ),
        )
    }
}
