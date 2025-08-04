package ly.david.musicsearch.shared.feature.details.alias

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import ly.david.musicsearch.shared.domain.alias.BasicAlias
import ly.david.musicsearch.shared.domain.common.ifNotNullOrEmpty
import ly.david.musicsearch.ui.common.listitem.CollapsibleListSeparatorHeader
import ly.david.musicsearch.ui.common.preview.PreviewTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

internal fun LazyListScope.aliasesSection(
    aliases: ImmutableList<BasicAlias>,
    collapsed: Boolean = false,
    onCollapseExpand: () -> Unit = {},
) {
    aliases.ifNotNullOrEmpty {
        stickyHeader {
            CollapsibleListSeparatorHeader(
                text = "Aliases",
                collapsed = collapsed,
                onClick = onCollapseExpand,
            )
        }
    }
    if (!collapsed) {
        items(aliases) { alias ->
            AliasListItem(
                alias = alias,
            )
        }
    }
}

@Preview
@Composable
internal fun PreviewAliasesSection() {
    PreviewTheme {
        Surface {
            LazyColumn {
                aliasesSection(
                    aliases = persistentListOf(
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
                )
            }
        }
    }
}
