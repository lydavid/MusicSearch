package ly.david.musicsearch.shared.feature.details.alias

import ly.david.musicsearch.shared.domain.DOT_SEPARATOR
import ly.david.musicsearch.shared.domain.alias.Alias
import ly.david.musicsearch.shared.domain.common.transformThisIfNotNullOrEmpty
import ly.david.musicsearch.shared.domain.getLifeSpanForDisplay

data class AliasListItemModel(
    override val name: String,
    override val locale: String,
    override val isPrimary: Boolean,
    val language: String?,
    val type: String? = null,
    override val begin: String = "",
    override val end: String = "",
    override val ended: Boolean = false,
) : Alias

internal fun AliasListItemModel.getFormattedTypeAndLifeSpan(
    primaryLabel: String,
): String {
    val displayLanguage = language.transformThisIfNotNullOrEmpty { "$it ($locale)" }
    val typeAndLifeSpan = listOfNotNull(
        type,
        getLifeSpanForDisplay().takeIf { it.isNotEmpty() },
        displayLanguage.takeIf { it.isNotEmpty() },
        primaryLabel.takeIf { isPrimary },
    ).joinToString(DOT_SEPARATOR)

    return typeAndLifeSpan
}
