package ly.david.musicsearch.shared.feature.details.alias

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import ly.david.musicsearch.ui.common.preview.PreviewTheme

private val primaryAlias = AliasListItemModel(
    name = "The Apothecary Diaries Season 2 Volume 2 (Original Anime Soundtrack)",
    locale = "en",
    language = "English",
    isPrimary = true,
    type = "Release group name",
    begin = "",
    end = "",
    ended = false,
)

@PreviewLightDark
@Composable
internal fun PreviewAliasListItemPrimary() {
    PreviewTheme {
        Surface {
            AliasListItem(
                alias = primaryAlias,
                filterText = "",
            )
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewAliasListItemPrimaryWithFilter() {
    PreviewTheme {
        Surface {
            AliasListItem(
                alias = primaryAlias,
                filterText = "i",
            )
        }
    }
}

private val endedAlias = AliasListItemModel(
    name = "なみ",
    locale = "ja",
    language = "Japanese",
    isPrimary = false,
    type = "Artist name",
    begin = "2010",
    end = "2015-02",
    ended = true,
)

@PreviewLightDark
@Composable
internal fun PreviewAliasListItemEnded() {
    PreviewTheme {
        Surface {
            AliasListItem(
                alias = endedAlias,
                filterText = "",
            )
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewAliasListItemEndedWithFilter() {
    PreviewTheme {
        Surface {
            AliasListItem(
                alias = endedAlias,
                filterText = "0",
            )
        }
    }
}
