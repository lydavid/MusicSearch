package ly.david.musicsearch.shared.feature.details.work

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import ly.david.musicsearch.shared.domain.listitem.RelationListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.work.WorkAttributeUiModel
import ly.david.musicsearch.shared.domain.details.WorkDetailsModel
import ly.david.musicsearch.ui.core.theme.PreviewTheme

@PreviewLightDark
@Composable
internal fun PreviewWorkDetailsUi() {
    PreviewTheme {
        Surface {
            WorkDetailsTabUi(
                work = WorkDetailsModel(
                    id = "w1",
                    name = "Work",
                    type = "Song",
                    language = "eng",
                    iswcs = listOf(
                        "T-101.238.335-4",
                        "T-101.238.335-5",
                    ),
                    attributes = listOf(
                        WorkAttributeUiModel(
                            type = "AKM ID",
                            typeId = "a",
                            value = "1234567",
                        ),
                    ),
                    urls = listOf(
                        RelationListItemModel(
                            id = "1",
                            linkedEntity = MusicBrainzEntity.URL,
                            linkedEntityId = "2",
                            label = "license",
                            name = "https://genius.com/Shaggy-bonafide-girl-lyrics",
                        ),
                    ),
                ),
            )
        }
    }
}
