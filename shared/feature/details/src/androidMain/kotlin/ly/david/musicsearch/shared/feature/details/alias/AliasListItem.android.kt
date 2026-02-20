package ly.david.musicsearch.shared.feature.details.alias

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import ly.david.musicsearch.shared.domain.alias.AliasType
import ly.david.musicsearch.shared.domain.alias.BasicAlias
import ly.david.musicsearch.ui.common.preview.PreviewTheme

@PreviewLightDark
@Composable
internal fun PreviewAliasListItemPrimary() {
    PreviewTheme {
        Surface {
            AliasListItem(
                alias = BasicAlias(
                    name = "The Apothecary Diaries Season 2 Volume 2 (Original Anime Soundtrack)",
                    locale = "en",
                    isPrimary = true,
                    type = AliasType.RELEASE_GROUP_NAME,
                    begin = "",
                    end = "",
                    ended = false,
                ),
            )
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewAliasListItemEnded() {
    PreviewTheme {
        Surface {
            AliasListItem(
                alias = BasicAlias(
                    name = "なみ",
                    locale = "ja",
                    isPrimary = false,
                    type = AliasType.ARTIST_NAME,
                    begin = "2010",
                    end = "2015-02",
                    ended = true,
                ),
            )
        }
    }
}
