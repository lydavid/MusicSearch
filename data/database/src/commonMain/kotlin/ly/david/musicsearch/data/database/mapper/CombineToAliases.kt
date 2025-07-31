package ly.david.musicsearch.data.database.mapper

import ly.david.musicsearch.data.database.GROUP_CONCAT_DELIMITER
import ly.david.musicsearch.shared.domain.alias.BasicAlias

internal fun combineToPrimaryAliases(
    aliasNames: String?,
    aliasLocales: String?,
): List<BasicAlias> = if (aliasNames != null && aliasLocales != null) {
    val names = aliasNames.split(GROUP_CONCAT_DELIMITER)
    val locales = aliasLocales.split(GROUP_CONCAT_DELIMITER)
    names.zip(locales) { name, locale ->
        BasicAlias(
            name = name,
            locale = locale,
            isPrimary = true,
        )
    }
} else {
    emptyList()
}
