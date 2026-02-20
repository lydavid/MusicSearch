package ly.david.musicsearch.shared.feature.details.alias

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import kotlinx.collections.immutable.persistentListOf
import ly.david.musicsearch.shared.domain.alias.BasicAlias
import ly.david.musicsearch.ui.common.preview.PreviewTheme

@PreviewLightDark
@Composable
internal fun PreviewAliasesSection() {
    PreviewTheme {
        Surface {
            LazyColumn {
                aliasesSection(
                    filteredAliases = persistentListOf(
                        BasicAlias(
                            name = "Various Artists",
                            locale = "en",
                            isPrimary = true,
                        ),
                        BasicAlias(
                            name = "ヴァリアス・アーティスト",
                            locale = "ja",
                            isPrimary = true,
                        ),
                        BasicAlias(
                            name = "群星",
                            locale = "zh",
                            isPrimary = true,
                        ),
                    ),
                    totalAliases = 50,
                )
            }
        }
    }
}
