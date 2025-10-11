package ly.david.musicsearch.ui.common.work

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import kotlinx.collections.immutable.persistentListOf
import ly.david.musicsearch.shared.domain.listitem.WorkListItemModel
import ly.david.musicsearch.ui.common.preview.PreviewWithTransitionAndOverlays

@PreviewLightDark
@Composable
internal fun PreviewWorkListItemModel() {
    PreviewWithTransitionAndOverlays {
        WorkListItem(
            work = WorkListItemModel(
                id = "4202da8f-2f57-4186-a5c8-80ed64988a16",
                name = "aLIEz",
                // previews doesn't render "German" but app does
                languages = persistentListOf("jpn", "eng", "deu"),
                iswcs = persistentListOf("T-915.750.016-8"),
            ),
        )
    }
}

@PreviewLightDark
@Composable
internal fun PreviewWorkListItemModelAllInfo() {
    PreviewWithTransitionAndOverlays {
        WorkListItem(
            work = WorkListItemModel(
                id = "343dbbe6-d9ce-3853-8d8d-230734c0424f",
                name = "残酷な天使のテーゼ",
                disambiguation = "Neon Genesis Evangelion",
                type = "Song",
                iswcs = persistentListOf("T-101.261.638-3"),
                languages = persistentListOf("jpn"),
            ),
        )
    }
}

@PreviewLightDark
@Composable
internal fun PreviewWorkListItemModelVisited() {
    PreviewWithTransitionAndOverlays {
        WorkListItem(
            work = WorkListItemModel(
                id = "c4ebe5b5-6965-4b8a-9f5e-7e543fc2acf3",
                name = "イニシエノウタ",
                disambiguation = "NieR",
                type = "Song",
                languages = persistentListOf("qaa"),
                visited = true,
            ),
        )
    }
}
