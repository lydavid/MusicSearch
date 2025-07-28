package ly.david.musicsearch.shared.domain.alias

interface Alias {
    val name: String
    val isPrimary: Boolean?
    val locale: String?
}
