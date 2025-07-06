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
                id = "4202da8f-2f57-4186-a5c8-80ed64988a16",
                name = "aLIEz",
                // previews doesn't render "German" but app does
                languages = listOf("jpn", "eng", "deu"),
                iswcs = listOf("T-915.750.016-8"),
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
                languages = listOf("jpn"),
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
                id = "c4ebe5b5-6965-4b8a-9f5e-7e543fc2acf3",
                name = "イニシエノウタ",
                disambiguation = "NieR",
                type = "Song",
                languages = listOf("qaa"),
                visited = true,
            ),
        )
    }
}
