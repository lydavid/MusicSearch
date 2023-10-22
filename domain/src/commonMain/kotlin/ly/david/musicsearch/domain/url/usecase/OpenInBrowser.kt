package ly.david.musicsearch.domain.url.usecase

interface OpenInBrowser {
    operator fun invoke(url: String)
}
