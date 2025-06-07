package ly.david.musicsearch.ui.common.listitem

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import ly.david.musicsearch.shared.domain.LifeSpanUiModel
import ly.david.musicsearch.ui.common.preview.PreviewTheme

@PreviewLightDark
@Composable
internal fun PreviewLifeSpanTextDifferentBeginAndEnd() {
    PreviewTheme {
        Surface {
            LifeSpanText(
                lifeSpan = LifeSpanUiModel(
                    begin = "2022-12-15",
                    end = "2022-12-16",
                ),
                heading = "Date",
                beginHeading = "Start Date",
                endHeading = "End Date",
            )
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewLifeSpanTextSameBeginAndEnd() {
    PreviewTheme {
        Surface {
            LifeSpanText(
                lifeSpan = LifeSpanUiModel(
                    begin = "2022-12-15",
                    end = "2022-12-15",
                ),
                heading = "Date",
                beginHeading = "Start Date",
                endHeading = "End Date",
            )
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewLifeSpanTextBeginOnly() {
    PreviewTheme {
        Surface {
            LifeSpanText(
                lifeSpan = LifeSpanUiModel(
                    begin = "2022-12-15",
                    end = null,
                ),
                heading = "Date",
                beginHeading = "Start Date",
                endHeading = "End Date",
            )
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewLifeSpanTextEndOnly() {
    PreviewTheme {
        Surface {
            LifeSpanText(
                lifeSpan = LifeSpanUiModel(
                    begin = null,
                    end = "2022-12-15",
                ),
                heading = "Date",
                beginHeading = "Start Date",
                endHeading = "End Date",
            )
        }
    }
}
