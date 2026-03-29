package ly.david.musicsearch.shared.feature.details.alias

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import kotlinx.collections.immutable.persistentListOf
import ly.david.musicsearch.ui.common.preview.PreviewTheme

private val aliases = persistentListOf(
    AliasListItemModel(
        name = "Various Artists",
        type = null,
        locale = "en",
        language = "English",
        isPrimary = true,
        begin = "",
        end = "",
        ended = false,
    ),
    AliasListItemModel(
        name = "ヴァリアス・アーティスト",
        type = null,
        locale = "ja",
        language = "Japanese",
        isPrimary = true,
        begin = "",
        end = "",
        ended = false,
    ),
    AliasListItemModel(
        name = "群星",
        type = null,
        locale = "zh",
        language = "Chinese",
        isPrimary = true,
        begin = "",
        end = "",
        ended = false,
    ),
)

@PreviewLightDark
@Composable
internal fun PreviewAliasesSection() {
    PreviewTheme {
        Surface {
            LazyColumn {
                aliasesSection(
                    aliases = aliases,
                    primaryLabel = "Primary",
                    filterText = "",
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewAliasesSectionWithFilter() {
    PreviewTheme {
        Surface {
            LazyColumn {
                aliasesSection(
                    aliases = aliases,
                    primaryLabel = "Primary",
                    filterText = "en",
                )
            }
        }
    }
}
