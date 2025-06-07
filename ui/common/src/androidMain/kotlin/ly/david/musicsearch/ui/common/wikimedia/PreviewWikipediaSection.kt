package ly.david.musicsearch.ui.common.wikimedia

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import ly.david.musicsearch.shared.domain.wikimedia.WikipediaExtract
import ly.david.musicsearch.ui.common.theme.PreviewTheme

@PreviewLightDark
@Composable
internal fun PreviewWikipediaSection() {
    PreviewTheme {
        Surface {
            WikipediaSection(
                extract = WikipediaExtract(
                    extract = "Tricot (Japanese: \u30c8\u30ea\u30b3, Hepburn: toriko, ) is a Japanese rock band from Kyoto. The band was formed in 2010 by vocalist and guitarist Ikumi \"Ikkyu\" Nakajima, guitarist Motoko \"Motifour\" Kida, and bassist Hiromi \"Hirohiro\" Sagane. Known for their intricate rhythms and visual identity, they have released seven studio albums. Their musical style has been described by Rolling Stone as \"adrenalized math rock sped up and given pop's candy coating\".",
                    wikipediaUrl = "https://en.wikipedia.org/wiki/Tricot_(band)",
                ),
            )
        }
    }
}
