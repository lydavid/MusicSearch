package ly.david.musicsearch.shared.feature.details.alias

import androidx.compose.runtime.Composable
import ly.david.musicsearch.shared.domain.alias.BasicAlias
import ly.david.musicsearch.ui.common.work.getDisplayLanguage

@Composable
internal fun BasicAlias.toAliasListItemModel(): AliasListItemModel {
    return AliasListItemModel(
        name = name,
        type = type?.getDisplayString(),
        isPrimary = isPrimary,
        locale = locale,
        language = locale.getDisplayLanguage(),
        begin = begin,
        end = end,
        ended = ended,
    )
}
