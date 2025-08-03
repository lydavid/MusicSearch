package ly.david.musicsearch.ui.common.locale

import androidx.compose.ui.text.intl.Locale
import kotlinx.collections.immutable.persistentListOf
import ly.david.musicsearch.shared.domain.alias.BasicAlias
import ly.david.musicsearch.shared.domain.details.ArtistDetailsModel
import org.junit.Assert
import org.junit.Test

class NameWithDisambiguationAndAliasesExtTest {
    @Test
    fun match() {
        val nameWithDisambiguationAndAliases = ArtistDetailsModel(
            id = "89ad4ac3-39f7-470e-963a-56509c546377",
            name = "Various Artists",
            disambiguation = "add compilations to this artist",
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

        // Fallback to language
        Assert.assertEquals(
            "群星",
            nameWithDisambiguationAndAliases.getAliasForLocale(
                systemLocale = Locale("zh-Hant-HK"),
            ),
        )

        // Match language
        Assert.assertEquals(
            "ヴァリアス・アーティスト",
            nameWithDisambiguationAndAliases.getAliasForLocale(
                systemLocale = Locale("ja"),
            ),
        )

        // Fallback to en, but because en is the same as name, we don't use it
        Assert.assertEquals(
            null,
            nameWithDisambiguationAndAliases.getAliasForLocale(
                systemLocale = Locale("ab"),
            ),
        )
    }
}
